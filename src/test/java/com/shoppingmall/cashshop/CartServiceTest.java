package com.shoppingmall.cashshop;

import com.shoppingmall.cashshop.constant.ItemSellStatus;
import com.shoppingmall.cashshop.dto.CartItemDto;
import com.shoppingmall.cashshop.entity.CartItem;
import com.shoppingmall.cashshop.entity.Item;
import com.shoppingmall.cashshop.entity.Member;
import com.shoppingmall.cashshop.repository.CartItemRepository;
import com.shoppingmall.cashshop.repository.ItemRepository;
import com.shoppingmall.cashshop.repository.MemberRepository;
import com.shoppingmall.cashshop.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

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
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Item item = saveitem();
        Member member = savemember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setItemId(item.getId());
        cartItemDto.setQuantity(5);

        Long cartItemId = cartService.addCart(cartItemDto, member.getMemberEmail());
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDto.getQuantity(), cartItem.getQuantity());
    }
}
