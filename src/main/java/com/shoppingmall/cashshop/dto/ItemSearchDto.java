package com.shoppingmall.cashshop.dto;

import com.shoppingmall.cashshop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    private String searchDateType;
    private ItemSellStatus searchSellStatus;
    private String searchBy;
    private String searchQuery = "";
    private String sortField;       // 정렬 기준 (price, itemName 등)
    private String sortOrder;       // 정렬 방향 (asc, desc)
}
