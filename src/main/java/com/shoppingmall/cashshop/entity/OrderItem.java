package com.shoppingmall.cashshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "order_item")
@Data @EqualsAndHashCode(callSuper = false)
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ToString.Exclude //순환 참조 방지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int orderQuantity;

    public static OrderItem createOrderItem(Item item, int orderQuantity) {

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderQuantity(orderQuantity);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(orderQuantity);
        return orderItem;
    }

    public int getTotalPrice(){
        return orderPrice * orderQuantity;
    }
    public void cancel() {this.getItem().addStock(orderQuantity);}
}
