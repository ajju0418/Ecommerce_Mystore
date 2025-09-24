package com.estore.product.service;

import com.estore.product.entity.Product;
import com.estore.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class BrandProductDataInitializer implements CommandLineRunner {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) {
        // Remove static brand product initialization
        // No brand products will be seeded automatically
    }

    private void addBrandProducts(String brand) {
        String[] categories = {"Shirts", "T-Shirts", "Jeans", "Jackets", "Accessories", "Deal of the Day", "Best Seller"};
        String[] types = {"Men", "Women"};
        // Realistic product names for each category
        String[][] productNames = {
            {"Slim Fit Shirt", "Classic Oxford Shirt", "Linen Casual Shirt", "Checked Shirt", "Formal White Shirt", "Printed Shirt", "Denim Shirt"}, // Shirts
            {"Graphic Tee", "V-Neck T-Shirt", "Crew Neck Tee", "Striped T-Shirt", "Solid Color Tee", "Logo T-Shirt", "Long Sleeve Tee"}, // T-Shirts
            {"Skinny Jeans", "Straight Fit Jeans", "Ripped Jeans", "High Waist Jeans", "Bootcut Jeans", "Slim Tapered Jeans", "Classic Blue Jeans"}, // Jeans
            {"Bomber Jacket", "Denim Jacket", "Leather Jacket", "Puffer Jacket", "Windcheater", "Blazer Jacket", "Varsity Jacket"}, // Jackets
            {"Leather Belt", "Canvas Cap", "Woolen Scarf", "Aviator Sunglasses", "Sports Watch", "Backpack", "Wallet"}, // Accessories
            {"Flash Sale Shirt", "Limited Edition Tee", "Hot Deal Jeans", "Special Price Jacket", "Exclusive Cap", "Deal Scarf", "Promo Wallet"}, // Deal of the Day
            {"Top Rated Shirt", "Best Seller Tee", "Popular Jeans", "Trending Jacket", "Must Have Cap", "Favorite Scarf", "Hit Wallet"} // Best Seller
        };
        for (int i = 0; i < 30; i++) {
            String id = UUID.randomUUID().toString();
            int catIdx = i % categories.length;
            int nameIdx = i % productNames[catIdx].length;
            String category = categories[catIdx];
            String type = types[i % types.length];
            String name = brand + " " + productNames[catIdx][nameIdx];
            double price = 999 + (i * 10);
            String imageUrl = "https://via.placeholder.com/200x200?text=" + brand.replace(" ", "+") + "+" + (name.replace(" ", "+"));
            double rating = 3.5 + (i % 5) * 0.3;
            Product p = new Product(id, name, price, imageUrl, rating, category, type, type);
            p.setBrand(brand);
            p.setDescription("High quality " + name + " from " + brand + ".");
            p.setStock(100 + i);
            p.setStatus("ACTIVE");
            productRepository.save(p);
        }
    }
}
