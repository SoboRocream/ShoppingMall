package com.shoppingmall.cashshop.dto;

import com.shoppingmall.cashshop.constant.ItemSellStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ItemDto {

    private Long id; //상품 코드
    private String itemName; //상품명
    private Integer price; //상품 가격//재고 수량
    private String itemDetail; //상품 상세 설명
    public ItemSellStatus itemSellStatus;
    private LocalDateTime regTime; // 상품 등록 시간
    private LocalDateTime updateTime; // 상품 수정 시간
}
