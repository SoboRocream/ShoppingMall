package com.shoppingmall.cashshop.repository;

import com.shoppingmall.cashshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMember_memberId(Long memberId);
}
