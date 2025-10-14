package com.estore.product.service;

import com.estore.product.entity.Product;
import com.estore.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;

@Service
public class ProductFilterService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProductsByCategory(String category) {
        List<Product> result = productRepository.findByCategory(category);
        return result != null ? result : Collections.emptyList();
    }

    public List<Product> getProductsByBrand(String brand) {
        List<Product> result = productRepository.findByBrand(brand);
        return result != null ? result : Collections.emptyList();
    }

    public List<Product> getProductsByBrandAndCategory(String brand, String category) {
        List<Product> result = productRepository.findByBrandAndCategory(brand, category);
        return result != null ? result : Collections.emptyList();
    }

    public List<Product> getOutOfStockProducts() {
        List<Product> result = productRepository.findByStockLessThanEqualOrStatus(0, "OUT_OF_STOCK");
        return result != null ? result : Collections.emptyList();
    }

    public List<Product> getInactiveProducts() {
        List<Product> result = productRepository.findByStatus("INACTIVE");
        return result != null ? result : Collections.emptyList();
    }
}
