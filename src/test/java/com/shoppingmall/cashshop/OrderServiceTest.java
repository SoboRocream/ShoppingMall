package com.shoppingmall.cashshop;

import com.shoppingmall.cashshop.constant.ItemSellStatus;
import com.shoppingmall.cashshop.constant.OrderStatus;
import com.shoppingmall.cashshop.dto.OrderDto;
import com.shoppingmall.cashshop.entity.Item;
import com.shoppingmall.cashshop.entity.Member;
import com.shoppingmall.cashshop.entity.Order;
import com.shoppingmall.cashshop.entity.OrderItem;
import com.shoppingmall.cashshop.repository.ItemRepository;
import com.shoppingmall.cashshop.repository.MemberRepository;
import com.shoppingmall.cashshop.repository.OrderRepository;
import com.shoppingmall.cashshop.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    public Item saveitem(){
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member savemember(){
        Member member = new Member();
        member.setMemberEmail("test@1234.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void order() {
        Item item = saveitem();
        Member member = savemember();

        OrderDto orderDto = new OrderDto();
        orderDto.setQuantity(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getMemberEmail());

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItems();

        int totalPrice = orderDto.getQuantity() * item.getPrice();

        assertEquals(totalPrice, order.getTotalPrice());
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder() {
        Item item = saveitem();
        Member member = savemember();

        OrderDto orderDto = new OrderDto();
        orderDto.setQuantity(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getMemberEmail());

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
        assertEquals(100, item.getStockNumber());
    }

}
