package com.shoppingmall.cashshop.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {

    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long itemId;

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
    @Max(value = 100, message = "최소 주문 수량은 100개 입니다.")
    private int quantity;

    @Override
    public String toString() {
        return "OrderDto{" +
                "itemId=" + itemId +
                ", quantity=" + quantity +
                '}';
    }
}
