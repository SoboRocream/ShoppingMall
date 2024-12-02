package com.shoppingmall.cashshop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoppingmall.cashshop.constant.ItemSellStatus;
import com.shoppingmall.cashshop.dto.ItemSearchDto;
import com.shoppingmall.cashshop.dto.MainItemDto;
import com.shoppingmall.cashshop.dto.QMainItemDto;
import com.shoppingmall.cashshop.entity.Item;
import com.shoppingmall.cashshop.entity.QItem;
import com.shoppingmall.cashshop.entity.QItemImg;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression regDtsAfter(String searchDataType) {
        if (searchDataType == null || "all".equals(searchDataType)) {
            return null;
        }

        LocalDateTime dateTime = LocalDateTime.now();

        switch (searchDataType) {
            case "1d":
                dateTime = dateTime.minusDays(1);
                break;
            case "1w":
                dateTime = dateTime.minusWeeks(1);
                break;
            case "1m":
                dateTime = dateTime.minusMonths(1);
                break;
            case "6m":
                dateTime = dateTime.minusMonths(6);
                break;
            default:
                return null; // 예상하지 못한 값에 대해 null 반환
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return null; // 검색어가 없으면 조건 추가 안 함
        }

        if ("itemName".equals(searchBy)) {
            return QItem.item.itemName.like("%" + searchQuery + "%");
        } else if ("createdBy".equals(searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        // 동적 조건 생성
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(regDtsAfter(itemSearchDto.getSearchDateType()));
        builder.and(searchSellStatusEq(itemSearchDto.getSearchSellStatus()));
        builder.and(searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()));

        // 결과 목록 조회
        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(builder)
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 조회
        long total = queryFactory
                .select(QItem.item.count())
                .from(QItem.item)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
    private BooleanExpression itemNameLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null
                : QItem.item.itemName.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        // 기본값을 설정하는 부분
        if (itemSearchDto.getSearchQuery() == null) {
            itemSearchDto.setSearchQuery(""); // 기본값으로 빈 문자열 설정
        }
        if (itemSearchDto.getSortField() == null) {
            itemSearchDto.setSortField("id"); // 기본값으로 id으로 설정
        }
        if (itemSearchDto.getSortOrder() == null) {
            itemSearchDto.setSortOrder("desc"); // 기본값으로 내림차순 설정
        }

        QueryResults<MainItemDto> result = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.itemName,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price,
                                item.itemSellStatus)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .orderBy(getSortOrder(itemSearchDto.getSortField(), itemSearchDto.getSortOrder()), item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<MainItemDto> content = result.getResults();
        long total = result.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier<?> getSortOrder(String sortField, String sortOrder) {
        if (sortField == null || sortField.isEmpty()) {
            return QItem.item.id.desc(); // 기본 정렬 조건
        }

        PathBuilder<Item> entityPath = new PathBuilder<>(Item.class, "item");
        Order order = "asc".equalsIgnoreCase(sortOrder) ? Order.ASC : Order.DESC;

        // 필드의 타입을 명시적으로 지정하여 getComparable 호출
        ComparableExpressionBase<?> expression = entityPath.getComparable(sortField, Comparable.class);

        return new OrderSpecifier<>(order, expression);
    }

}
