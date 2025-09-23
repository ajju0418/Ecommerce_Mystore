package com.estore.product.service;

import com.estore.product.entity.Product;
import com.estore.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            initializeProducts();
        }
    }

    private void initializeProducts() {
        List<Product> products = Arrays.asList(
            // Deal of the Day Products
            new Product("1", "Premium Cotton T-Shirt", 899.0, "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=300&h=300&fit=crop", 4.5, "Deal of the Day", "T-Shirts", "Men"),
            new Product("2", "Denim Jacket", 2499.0, "https://images.unsplash.com/photo-1544966503-7cc5ac882d5f?w=300&h=300&fit=crop", 4.3, "Deal of the Day", "Jackets", "Men"),
            new Product("3", "Floral Summer Dress", 1899.0, "https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=300&h=300&fit=crop", 4.7, "Deal of the Day", "Dresses", "Women"),
            new Product("4", "Leather Boots", 3299.0, "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=300&h=300&fit=crop", 4.4, "Deal of the Day", "Footwear", "Unisex"),
            new Product("5", "Casual Blazer", 2799.0, "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&h=300&fit=crop", 4.2, "Deal of the Day", "Blazers", "Men"),

            // New Arrivals Products
            new Product("6", "Silk Blouse", 1599.0, "https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=300&h=300&fit=crop", 4.6, "New Arrivals", "Blouses", "Women"),
            new Product("7", "Chino Pants", 1299.0, "https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=300&h=300&fit=crop", 4.1, "New Arrivals", "Pants", "Men"),
            new Product("8", "Knit Sweater", 1799.0, "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=300&h=300&fit=crop", 4.5, "New Arrivals", "Sweaters", "Women"),
            new Product("9", "Canvas Sneakers", 2199.0, "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=300&h=300&fit=crop", 4.3, "New Arrivals", "Footwear", "Unisex"),
            new Product("10", "Polo Shirt", 999.0, "https://images.unsplash.com/photo-1586790170083-2f9ceadc732d?w=300&h=300&fit=crop", 4.0, "New Arrivals", "Shirts", "Men"),

            // Sale Products
            new Product("11", "Vintage Jeans", 1499.0, "https://images.unsplash.com/photo-1542272604-787c3835535d?w=300&h=300&fit=crop", 4.2, "Sale", "Jeans", "Women"),
            new Product("12", "Striped Shirt", 799.0, "https://images.unsplash.com/photo-1571945153237-4929e783af4a?w=300&h=300&fit=crop", 3.9, "Sale", "Shirts", "Men"),
            new Product("13", "Maxi Dress", 1699.0, "https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=300&h=300&fit=crop", 4.4, "Sale", "Dresses", "Women"),
            new Product("14", "Leather Belt", 599.0, "https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=300&h=300&fit=crop", 4.1, "Sale", "Accessories", "Unisex"),
            new Product("15", "Hoodie", 1199.0, "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=300&h=300&fit=crop", 4.3, "Sale", "Hoodies", "Unisex"),

            // Regular Products for Products List
            new Product("16", "Business Suit", 4999.0, "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&h=300&fit=crop", 4.8, "Premium", "Suits", "Men"),
            new Product("17", "Evening Gown", 3499.0, "https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=300&h=300&fit=crop", 4.7, "Premium", "Gowns", "Women"),
            new Product("18", "Sports Shoes", 2799.0, "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=300&h=300&fit=crop", 4.5, "Sports", "Footwear", "Unisex"),
            new Product("19", "Casual Shirt", 899.0, "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=300&h=300&fit=crop", 4.0, "Casual", "Shirts", "Men"),
            new Product("20", "Summer Top", 699.0, "https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=300&h=300&fit=crop", 4.2, "Casual", "Tops", "Women")
        );

        // Set additional properties for each product
        products.forEach(product -> {
            product.setStock(100);
            product.setStatus("ACTIVE");
            product.setBrand("E-Store Brand");
            product.setDescription("High quality " + product.getName().toLowerCase() + " perfect for any occasion.");
            product.setOriginalPrice(product.getPrice() + 200.0);
        });

        productRepository.saveAll(products);
        System.out.println("Initialized " + products.size() + " products in the database");
    }
}