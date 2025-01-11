package com.shoppingmall.cashshop.repository;

import com.shoppingmall.cashshop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    //User's Purchase History query
    @Query("select o from Order o " +
            "where o.member.memberEmail = :email " +
            "order by o.orderDate desc")
    List<Order> findOrder(@Param("email") String email, Pageable pageable);

    //User's orders
    @Query("select count(o) from Order o where o.member.memberEmail = :email")
    Long countOrder(@Param("email") String email);
}
