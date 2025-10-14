package com.estore.cart.service;

import com.estore.cart.dto.AddToCartDto;
import com.estore.cart.dto.CartResponseDto;
import com.estore.cart.entity.Cart;
import com.estore.cart.entity.CartItem;
import com.estore.cart.repository.CartRepository;
import com.estore.cart.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void addToCart_NewItem_ShouldCreateNewCartItem() {
        // Given
        AddToCartDto addToCartDto = new AddToCartDto();
        addToCartDto.setUserId(1L);
        addToCartDto.setProductId("product1");
        addToCartDto.setQuantity(2);

        Cart cart = new Cart(1L);
        cart.setId(1L);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(1L, "product1")).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(new CartItem());

        // When
        CartResponseDto result = cartService.addToCart(addToCartDto);

        // Then
        assertNotNull(result);
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void addToCart_ExistingItem_ShouldUpdateQuantity() {
        // Given
        AddToCartDto addToCartDto = new AddToCartDto();
        addToCartDto.setUserId(1L);
        addToCartDto.setProductId("product1");
        addToCartDto.setQuantity(3);

        Cart cart = new Cart(1L);
        cart.setId(1L);
        
        CartItem existingItem = new CartItem(cart, "product1", 2);
        existingItem.setId(1L);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(1L, "product1")).thenReturn(Optional.of(existingItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(existingItem);

        // When
        CartResponseDto result = cartService.addToCart(addToCartDto);

        // Then
        assertNotNull(result);
        assertEquals(5, existingItem.getQuantity()); // 2 + 3
        verify(cartItemRepository, times(1)).save(existingItem);
    }

    @Test
    void getUserCart_ShouldReturnCartResponse() {
        // Given
        Long userId = 1L;
        Cart cart = new Cart(userId);
        cart.setId(1L);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        // When
        CartResponseDto result = cartService.getUserCart(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(cartRepository, times(1)).findByUserId(userId);
    }

    @Test
    void removeFromCart_ShouldDeleteCartItem() {
        // Given
        Long userId = 1L;
        String productId = "product1";
        Cart cart = new Cart(userId);
        cart.setId(1L);
        CartItem cartItem = new CartItem(cart, productId, 2);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(1L, productId)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        CartResponseDto result = cartService.removeFromCart(userId, productId);

        // Then
        assertNotNull(result);
        verify(cartItemRepository, times(1)).delete(cartItem);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void clearCart_ShouldDeleteAllCartItems() {
        // Given
        Long userId = 1L;
        Cart cart = new Cart(userId);
        cart.setId(1L);
        
        // Add cart items to trigger the save behavior
        CartItem item1 = new CartItem(cart, "product1", 2);
        CartItem item2 = new CartItem(cart, "product2", 1);
        cart.setCartItems(java.util.Arrays.asList(item1, item2));

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        cartService.clearCart(userId);

        // Then
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartItemRepository, times(1)).deleteAll(cart.getCartItems());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void updateCartItemQuantity_ShouldUpdateQuantity() {
        // Given
        Long userId = 1L;
        String productId = "product1";
        Integer newQuantity = 5;
        Cart cart = new Cart(userId);
        cart.setId(1L);
        CartItem cartItem = new CartItem(cart, productId, 2);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(1L, productId)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        CartResponseDto result = cartService.updateCartItemQuantity(userId, productId, newQuantity);

        // Then
        assertNotNull(result);
        assertEquals(newQuantity, cartItem.getQuantity());
        verify(cartItemRepository, times(1)).save(cartItem);
    }
}