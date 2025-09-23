package com.estore.cart.service;

import com.estore.cart.dto.AddToCartDto;
import com.estore.cart.dto.CartItemDto;
import com.estore.cart.dto.CartResponseDto;
import com.estore.cart.entity.Cart;
import com.estore.cart.entity.CartItem;
import com.estore.cart.repository.CartRepository;
import com.estore.cart.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;

    // Static product price map for demonstration
    private static final Map<String, Double> productPrices = new HashMap<>();
    static {
        productPrices.put("1", 499.0);
        productPrices.put("2", 799.0);
        productPrices.put("3", 299.0);
        productPrices.put("4", 999.0);
        // Add more productId-price pairs as needed
    }

    public CartResponseDto addToCart(AddToCartDto addToCartDto) {
        Cart cart = cartRepository.findByUserId(addToCartDto.getUserId())
                .orElse(new Cart(addToCartDto.getUserId()));
        
        if (cart.getId() == null) {
            cart = cartRepository.save(cart);
        }
        
        CartItem existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), addToCartDto.getProductId())
                .orElse(null);
        
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + addToCartDto.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem(cart, addToCartDto.getProductId(), addToCartDto.getQuantity());
            cartItemRepository.save(newItem);
        }
        
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        
        return getUserCart(addToCartDto.getUserId());
    }

    public CartResponseDto getUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(new Cart(userId));
        if (cart.getId() == null) {
            cart = cartRepository.save(cart);
        }
        List<CartItemDto> items = cart.getCartItems() != null ?
                cart.getCartItems().stream()
                        .map(item -> {
                            Double price = productPrices.getOrDefault(item.getProductId(), 0.0);
                            return new CartItemDto(item.getId(), item.getProductId(), item.getQuantity(), price);
                        })
                        .collect(Collectors.toList()) : List.of();
        double totalAmount = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        return new CartResponseDto(cart.getId(), cart.getUserId(), items, totalAmount);
    }

    public CartResponseDto updateCartItemQuantity(Long userId, String productId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));
        
        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
        
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        
        return getUserCart(userId);
    }

    public CartResponseDto removeFromCart(Long userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));
        
        cartItemRepository.delete(cartItem);
        
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        
        return getUserCart(userId);
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart != null && cart.getCartItems() != null) {
            cartItemRepository.deleteAll(cart.getCartItems());
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(cart);
        }
    }
}