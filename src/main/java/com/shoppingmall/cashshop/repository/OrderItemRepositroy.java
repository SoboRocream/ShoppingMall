package com.shoppingmall.cashshop.repository;

import com.shoppingmall.cashshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepositroy extends JpaRepository<OrderItem, Long> {
}
