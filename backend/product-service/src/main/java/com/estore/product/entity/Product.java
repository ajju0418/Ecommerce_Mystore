package com.estore.product.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;
    
    private Double rating;
    private Integer quantity;
    private String category;
    private Double originalPrice;
    private String brand;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private Integer stock;
    private String status;
    private String collection;
    private String type;
    private String gender;

    public Product() {}

    public Product(String id, String name, Double price, String imageUrl, Double rating, 
                   String collection, String type, String gender) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.collection = collection;
        this.type = type;
        this.gender = gender;
        this.status = "ACTIVE";
        this.stock = 100;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCollection() { return collection; }
    public void setCollection(String collection) { this.collection = collection; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}