package com.shoppingmall.cashshop.entity;

import com.shoppingmall.cashshop.constant.ItemSellStatus;
import com.shoppingmall.cashshop.dto.ItemFormDto;
import com.shoppingmall.cashshop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "item")
@Data @EqualsAndHashCode(callSuper = false)
public class Item extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private long id; //상품 코드

    @Column(nullable = false, length = 50)
    private String itemName; //상품명

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Column(name = "price", nullable = false)
    private int price; //상품 가격

    @Column(nullable = false)
    private int stockNumber; //재고 수량

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;
        if(restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }

}
