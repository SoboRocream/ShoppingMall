package com.shoppingmall.cashshop;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shoppingmall.cashshop.constant.ItemSellStatus;
import com.shoppingmall.cashshop.entity.Item;
import com.shoppingmall.cashshop.entity.QItem;
import com.shoppingmall.cashshop.repository.ItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

//    @Test
//    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        for(int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품%d".formatted(i));
            item.setPrice(10000+i*10);
            item.setItemDetail("테스트 상품 상세 설명%d".formatted(i));
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
            //System.out.println(savedItem.toString());
        }

        for(int i = 6; i <= 10; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품%d".formatted(i));
            item.setPrice(10000+i*10);
            item.setItemDetail("테스트 상품 상세 설명%d".formatted(i));
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
            //System.out.println(savedItem.toString());
        }

    }

//    @Test //Query Method
//    @DisplayName("상품명 조회 테스트")
//    public void findByItemNameTest(){
//        this.createItemTest();
//        List<Item> itemList = this.itemRepository.findByItemName("테스트 상품1");
//        for(Item item : itemList){
//            System.out.println(item.toString());
//        }
//    }

//    @Test //@Query
//    @DisplayName("상품명 조회 테스트")
//    public void findByItemDetailTest(){
//        this.createItemTest();
//        List<Item> itemList = this.itemRepository.findByItemDetail("테스트 상품 상세 설명");
//        for(Item item : itemList){
//            System.out.println(item.toString());
//        }
//    }

//    @Test //@Query - NativeQuery
//    @DisplayName("상품명 조회 테스트")
//    public void findByItemDetailTest(){
//        this.createItemTest();
//        List<Item> itemList = this.itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
//        for(Item item : itemList){
//            System.out.println(item.toString());
//        }
//    }

//    @PersistenceContext //어노테이션으로 Bean 주입
//    EntityManager entityManager;
//
//    @Test
//    @DisplayName("QueryDSL 조회 테스트 1")
//    public void queryDSLTest() {
//        this.createItemTest();
//        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager); //쿼리의 동적 생성을 위한 JPAQueryFactory 객체 생성
//        QItem qItem = QItem.item; //Querydsl을 통해 쿼리를 생성하기 위해 Qdomain 객체 생성
//
//        //QueryDsl 쿼리문들 받을 JPAQuery 객체 생성
//        //SQL 문자열이 아닌 자바 소스코드를 이용해 쿼리 생성
//        //select 조회, where 조건문, orderBy 정렬
//        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
//                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
//                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명"+ "%"))
//                .orderBy(qItem.price.desc());
//        List<Item> itemList = query.fetch();
//
//        for(Item item : itemList) {
//            System.out.println(item.toString());
//        }
//    }

    @Test
    @DisplayName("상품 QueryDsl 조회 테스트 2")
    public void queryDslTest2() {
        this.createItemTest();

        BooleanBuilder booleanBuilder = new BooleanBuilder(); //쿼리문의 where 역할을 수행하는 Predicate를 담는 객체 생성
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10030;
        String itemSellStat = "SELL";

        //where 조건부 설정
        booleanBuilder.and(item.itemDetail.like("%"+itemDetail+"%"));
        booleanBuilder.and(item.price.gt(price)); //greater than
        booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));

        //0번째 페이지, 5개의 데이터
        //findAll() 메소드의 매개변수로 predicate, pagable 전달
        //반환된 결과는 Page<Item> 타입으로 받음
        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult.getTotalElements());

        //반환된 페이지에서 content 부분만을 List에 담기
        List<Item> resultList = itemPagingResult.getContent();
        for(Item resultItem : resultList) {
            System.out.println(resultItem.toString());
        }



    }



}
