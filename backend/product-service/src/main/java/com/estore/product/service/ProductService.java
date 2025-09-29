package com.estore.product.service;

import com.estore.product.entity.Product;
import com.estore.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findByStatus("ACTIVE");
    }

    public List<Product> getProductsByCollection(String collection) {
        return productRepository.findByCollection(collection);
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(String id, Product product) {
        product.setId(id);
        return productRepository.save(product);
    }

    public List<Product> searchProducts(String searchTerm) {
        return productRepository.findByNameContainingIgnoreCase(searchTerm);
    }

    public List<Product> getProductsByGender(String gender) {
        return productRepository.findByGender(gender);
    }
}