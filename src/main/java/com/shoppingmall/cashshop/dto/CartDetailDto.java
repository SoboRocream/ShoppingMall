package com.shoppingmall.cashshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {

    private Long cartItemId;
    private String itemName;
    private int price;
    private int quantity;
    private String imgUrl;

    public CartDetailDto(Long cartItemId, String itemName, int price, int quantity, String imgUrl) {
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.imgUrl = imgUrl;
    }
}
