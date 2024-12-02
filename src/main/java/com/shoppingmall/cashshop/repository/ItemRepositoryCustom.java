package com.shoppingmall.cashshop.repository;

import com.shoppingmall.cashshop.dto.ItemSearchDto;
import com.shoppingmall.cashshop.dto.MainItemDto;
import com.shoppingmall.cashshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
