package com.shoppingmall.cashshop;

import com.shoppingmall.cashshop.constant.ItemSellStatus;
import com.shoppingmall.cashshop.entity.Item;
import com.shoppingmall.cashshop.entity.Member;
import com.shoppingmall.cashshop.entity.Order;
import com.shoppingmall.cashshop.entity.OrderItem;
import com.shoppingmall.cashshop.repository.ItemRepository;
import com.shoppingmall.cashshop.repository.MemberRepository;
import com.shoppingmall.cashshop.repository.OrderItemRepositroy;
import com.shoppingmall.cashshop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderCascadeTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createaItem(){
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){
        //1. Order 생성
        Order order = new Order();

        for(int i = 0; i< 3; i++){
            //2. Item 생성 및 저장
            Item item = this.createaItem();
            itemRepository.save(item);

            //3. OrderItem 생성 및 초기화
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrderQuantity(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order); // 외래키 값 지정

            //4. Order에 OrderItem add (총 3개)
            order.getOrderItems().add(orderItem);
        }

        //5. 3개의 OrderItem 객체를 담고 있는 Order 객체 저장
        orderRepository.saveAndFlush(order);
        em.clear();

        Order saveOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, saveOrder.getOrderItems().size());
    }

    //고아객체 제거 테스트
    @Autowired
    MemberRepository memberRepository;

    public Order createOrder(){
        Order order = new Order();
        for(int i = 0; i< 3; i++){
            Item item = this.createaItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrderQuantity(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        Long orderItem_id = order.getOrderItems().get(0).getId();
        order.getOrderItems().remove(0);
        em.flush();
        assertEquals(Optional.empty(), orderRepository.findById(orderItem_id));
    }

    @Autowired
    OrderItemRepositroy orderItemRepositroy;

    @Test
    @DisplayName("즉시 로딩 테스트")
    public void eagerLoadingTest() {
        Order order = this.createOrder();
        Long orderItem_id = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();
        OrderItem orderItem = orderItemRepositroy.findById(orderItem_id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.createOrder();
        Long orderItem_id = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();
        OrderItem orderItem = orderItemRepositroy.findById(orderItem_id)
                .orElseThrow(EntityNotFoundException::new);
        System.out.printf("Order Class : %s%n", orderItem.getOrder().getClass());
        System.out.println("=========================");
        orderItem.getOrder().getOrderDate();
        System.out.println("=========================");
    }
}
