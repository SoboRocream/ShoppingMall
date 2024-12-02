package com.shoppingmall.cashshop.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shoppingmall.cashshop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainItemDto {
    private Long id;
    private String itemName;
    private String itemDetail;
    private String imgUrl;
    private Integer price;
    private ItemSellStatus itemSellStatus;

    @QueryProjection
    public MainItemDto(Long id, String itemName, String itemDetail,
                       String imgUrl, Integer price, ItemSellStatus itemSellStatus) {
        this.id = id;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.itemSellStatus = itemSellStatus;
    }
}
