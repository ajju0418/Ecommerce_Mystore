package com.estore.product.service;

import com.estore.product.entity.Product;
import com.estore.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Given
        Product product1 = new Product("1", "Test Shirt", 29.99, "test.jpg", 4.5, "shirts", "men");
        Product product2 = new Product("2", "Test Pants", 49.99, "pants.jpg", 4.0, "pants", "women");
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        // When
        List<Product> result = productService.getAllProducts();

        // Then
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_WhenExists_ShouldReturnProduct() {
        // Given
        Product product = new Product("1", "Test Shirt", 29.99, "test.jpg", 4.5, "shirts", "men");
        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        // When
        Optional<Product> result = productService.getProductById("1");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Shirt", result.get().getName());
    }

    @Test
    void saveProduct_ShouldReturnSavedProduct() {
        // Given
        Product product = new Product("1", "Test Shirt", 29.99, "test.jpg", 4.5, "shirts", "men");
        when(productRepository.save(product)).thenReturn(product);

        // When
        Product result = productService.saveProduct(product);

        // Then
        assertEquals("Test Shirt", result.getName());
        verify(productRepository, times(1)).save(product);
    }
}