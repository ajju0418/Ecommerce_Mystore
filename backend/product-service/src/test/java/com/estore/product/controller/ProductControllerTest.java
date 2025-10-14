package com.estore.product.controller;

import com.estore.product.entity.Product;
import com.estore.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getAllProducts_ShouldReturnProductList() throws Exception {
        // Given
        Product product1 = new Product("1", "Test Shirt", 29.99, "test.jpg", 4.5, "shirts", "men");
        Product product2 = new Product("2", "Test Pants", 49.99, "pants.jpg", 4.0, "pants", "women");
        List<Product> products = Arrays.asList(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test Shirt"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById_WhenExists_ShouldReturnProduct() throws Exception {
        // Given
        Product product = new Product("1", "Test Shirt", 29.99, "test.jpg", 4.5, "shirts", "men");
        when(productService.getProductById("1")).thenReturn(Optional.of(product));

        // When & Then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Shirt"));
    }

    @Test
    void getProductById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given
        when(productService.getProductById("999")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void healthCheck_ShouldReturnHealthMessage() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product Service is running"));
    }
}