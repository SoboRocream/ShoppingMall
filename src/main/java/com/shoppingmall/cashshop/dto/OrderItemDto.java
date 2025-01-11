package com.shoppingmall.cashshop.dto;

import com.shoppingmall.cashshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemName = orderItem.getItem().getItemName();
        this.quantity = orderItem.getOrderQuantity();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemName;
    private int quantity;
    private int orderPrice;
    private String imgUrl;
}
