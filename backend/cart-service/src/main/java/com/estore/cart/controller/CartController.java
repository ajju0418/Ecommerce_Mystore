package com.estore.cart.controller;

import com.estore.cart.dto.AddToCartDto;
import com.estore.cart.dto.CartResponseDto;
import com.estore.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addToCart(@RequestBody AddToCartDto addToCartDto) {
        try {
            CartResponseDto response = cartService.addToCart(addToCartDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDto> getUserCart(@PathVariable Long userId) {
        try {
            CartResponseDto response = cartService.getUserCart(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDto> getUserCartAlternate(@PathVariable Long userId) {
        try {
            CartResponseDto response = cartService.getUserCart(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/cart")
    public ResponseEntity<CartResponseDto> getUserCartForFrontend(@PathVariable Long userId) {
        try {
            CartResponseDto response = cartService.getUserCart(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<CartResponseDto> updateCartItemQuantity(
            @PathVariable Long userId, 
            @PathVariable String productId, 
            @RequestParam Integer quantity) {
        try {
            CartResponseDto response = cartService.updateCartItemQuantity(userId, productId, quantity);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<CartResponseDto> removeFromCart(
            @PathVariable Long userId, 
            @PathVariable String productId) {
        try {
            CartResponseDto response = cartService.removeFromCart(userId, productId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Cart Service is running");
    }
}