package com.estore.product.repository;

import com.estore.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByCollection(String collection);
    List<Product> findByType(String type);
    List<Product> findByGender(String gender);
    List<Product> findByStatus(String status);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategory(String category);
    List<Product> findByBrand(String brand);
    List<Product> findByBrandAndCategory(String brand, String category);
}