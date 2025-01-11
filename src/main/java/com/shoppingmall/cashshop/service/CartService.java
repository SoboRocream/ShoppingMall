package com.shoppingmall.cashshop.service;

import com.shoppingmall.cashshop.dto.CartDetailDto;
import com.shoppingmall.cashshop.dto.CartItemDto;
import com.shoppingmall.cashshop.dto.CartOrderDto;
import com.shoppingmall.cashshop.dto.OrderDto;
import com.shoppingmall.cashshop.entity.Cart;
import com.shoppingmall.cashshop.entity.CartItem;
import com.shoppingmall.cashshop.entity.Item;
import com.shoppingmall.cashshop.entity.Member;
import com.shoppingmall.cashshop.repository.CartItemRepository;
import com.shoppingmall.cashshop.repository.CartRepository;
import com.shoppingmall.cashshop.repository.ItemRepository;
import com.shoppingmall.cashshop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto, String email) {
        Member member = memberRepository.findByMemberEmail(email);
        Cart cart = cartRepository.findByMember_memberId(member.getMemberId());

        if(cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(cartItem == null) {
            cartItem = CartItem.createCartItem(cart, item, cartItemDto.getQuantity());
            cartItemRepository.save(cartItem);
        }
        else {
            cartItem.addQuantity(cartItemDto.getQuantity());
        }
        return cart.getId();
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByMemberEmail(email);
        Cart cart = cartRepository.findByMember_memberId(member.getMemberId());
        if(cart == null) {
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId,String email) {
        Member currnetMember = memberRepository.findByMemberEmail(email);

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();

        return StringUtils.equals(currnetMember.getMemberEmail(), savedMember.getMemberEmail());
    }

    public void updateCartItemQuantity(Long cartItemId, int quantity){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateQuantity(quantity);
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {

        List<OrderDto> orderDtoList = new ArrayList<>();

        for(CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setQuantity(cartItem.getQuantity());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList, email);

        for(CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }
}
