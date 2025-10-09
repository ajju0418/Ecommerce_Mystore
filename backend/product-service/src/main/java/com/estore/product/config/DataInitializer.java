package com.estore.product.config;

import com.estore.product.entity.Product;
import com.estore.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        addNewProducts();
    }

    private void addNewProducts() {
        // Clear existing products and add new ones with proper categories and collections
        
        // T-Shirts - Casual Collection
        addProductIfNotExists("TSHIRT001", "Nike Dri-FIT T-Shirt", 899.99, 1299.99, "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/16e0c5a4-3c63-4681-9c4e-7d6b2c8f9e0a/dri-fit-mens-fitness-t-shirt-FN3226.png", 4.5, "T-Shirts", "Nike", "Moisture-wicking performance t-shirt", 75, "ACTIVE", "Casual", "Men");
        addProductIfNotExists("TSHIRT002", "Adidas Originals Tee", 799.99, 1199.99, "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/8f2c5d4e3b1a4c6d7e8f9a0b1c2d3e4f/adicolor-classics-trefoil-tee-black.jpg", 4.4, "T-Shirts", "Adidas", "Classic trefoil logo t-shirt", 80, "ACTIVE", "Casual", "Unisex");
        addProductIfNotExists("TSHIRT003", "H&M Basic Cotton Tee", 499.99, 799.99, "https://lp2.hm.com/hmgoepprod?set=source[/13/c4/13c4f8e9d2a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9.jpg],origin[dam],category[men_tshirtstanks_shortsleeve],type[DESCRIPTIVESTILLLIFE],res[m],hmver[2]&call=url[file:/product/main]", 4.2, "T-Shirts", "H&M", "Soft cotton basic t-shirt", 100, "ACTIVE", "Casual", "Men");
        
        // Jeans - Casual Collection
        addProductIfNotExists("JEANS001", "Levi's 501 Original Jeans", 1899.99, 2999.99, "https://lsco.scene7.com/is/image/lsco/005010114-front-pdp-lse?fmt=jpeg&qlt=70&resMode=bisharp&fit=crop,1&op_usm=0.6,0.6,8&wid=750&hei=1000", 4.7, "Jeans", "Levi's", "Classic straight fit jeans", 60, "ACTIVE", "Casual", "Men");
        addProductIfNotExists("JEANS002", "Zara Skinny Jeans", 1599.99, 2299.99, "https://static.zara.net/photos///2024/V/0/2/p/4365/420/427/2/w/850/4365420427_6_1_1.jpg", 4.3, "Jeans", "Zara", "High-waisted skinny fit jeans", 45, "ACTIVE", "Casual", "Women");
        addProductIfNotExists("JEANS003", "Gap Relaxed Fit Jeans", 1399.99, 1999.99, "https://www.gap.com/webcontent/0054/388/847/cn54388847.jpg", 4.4, "Jeans", "Gap", "Comfortable relaxed fit denim", 55, "ACTIVE", "Casual", "Men");
        
        // Shoes - Sports Collection
        addProductIfNotExists("SHOES001", "Nike Air Max 270", 2999.99, 4499.99, "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/awjogtdnqxniqqk0wpgf/air-max-270-mens-shoes-KkLcGR.png", 4.6, "Shoes", "Nike", "Lifestyle sneakers with Max Air cushioning", 40, "ACTIVE", "Sports", "Men");
        addProductIfNotExists("SHOES002", "Adidas Ultraboost 22", 3499.99, 4999.99, "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/fbaf991dce0344138e0aad8d00d748a1_9366/Ultraboost_22_Shoes_Black_GZ0127_01_standard.jpg", 4.8, "Shoes", "Adidas", "Premium running shoes with Boost technology", 35, "ACTIVE", "Sports", "Unisex");
        addProductIfNotExists("SHOES003", "Converse Chuck Taylor All Star", 1299.99, 1899.99, "https://www.converse.com/dw/image/v2/BCZC_PRD/on/demandware.static/-/Sites-cnv-master-catalog/default/dw8f2c5d4e/images/a_107/M9160_A_107X1.jpg", 4.5, "Shoes", "Converse", "Classic canvas high-top sneakers", 70, "ACTIVE", "Casual", "Unisex");
        
        // Jackets - Premium Collection
        addProductIfNotExists("JACKET001", "North Face Puffer Jacket", 3999.99, 5999.99, "https://images.thenorthface.com/is/image/TheNorthFace/NF0A3C8D_JK3_hero?$638x745$", 4.7, "Jackets", "The North Face", "Insulated winter puffer jacket", 25, "ACTIVE", "Premium", "Unisex");
        addProductIfNotExists("JACKET002", "Zara Leather Jacket", 4999.99, 7499.99, "https://static.zara.net/photos///2024/V/0/2/p/0706/302/800/2/w/850/0706302800_6_1_1.jpg", 4.6, "Jackets", "Zara", "Genuine leather biker jacket", 20, "ACTIVE", "Premium", "Men");
        addProductIfNotExists("JACKET003", "Uniqlo Down Jacket", 2999.99, 4299.99, "https://image.uniqlo.com/UQ/ST3/AsianCommon/imagesgoods/429412/item/goods_09_429412.jpg", 4.5, "Jackets", "Uniqlo", "Ultra light down jacket, packable", 30, "ACTIVE", "Casual", "Unisex");
        
        // Hoodies - Casual Collection
        addProductIfNotExists("HOODIE001", "Champion Reverse Weave Hoodie", 1799.99, 2699.99, "https://www.champion.com/dw/image/v2/AAFS_PRD/on/demandware.static/-/Sites-champion-master-catalog/default/dw8f2c5d4e/images/GF89H/GF89H_Y07647_1.jpg", 4.6, "Hoodies", "Champion", "Classic pullover hoodie with kangaroo pocket", 50, "ACTIVE", "Casual", "Unisex");
        addProductIfNotExists("HOODIE002", "Nike Tech Fleece Hoodie", 2299.99, 3299.99, "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/8f2c5d4e-3b1a-4c6d-7e8f-9a0b1c2d3e4f/tech-fleece-mens-full-zip-hoodie-CU4489.png", 4.7, "Hoodies", "Nike", "Premium tech fleece full-zip hoodie", 40, "ACTIVE", "Sports", "Men");
        addProductIfNotExists("HOODIE003", "H&M Oversized Hoodie", 999.99, 1599.99, "https://lp2.hm.com/hmgoepprod?set=source[/8f/2c/8f2c5d4e3b1a4c6d7e8f9a0b1c2d3e4f5a6b7c8d.jpg],origin[dam],category[men_hoodiessweatshirts_hoodies],type[DESCRIPTIVESTILLLIFE],res[m],hmver[2]&call=url[file:/product/main]", 4.3, "Hoodies", "H&M", "Relaxed fit oversized hoodie", 65, "ACTIVE", "Casual", "Unisex");
        
        // Accessories - Premium Collection
        addProductIfNotExists("WATCH001", "Apple Watch Series 9", 9999.99, 12999.99, "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/watch-s9-gps-aluminum-pink-sport-band-pink-pdp-image-position-1__WWEN.jpg", 4.8, "Accessories", "Apple", "Smart watch with health monitoring", 15, "ACTIVE", "Premium", "Unisex");
        addProductIfNotExists("BAG001", "Nike Brasilia Backpack", 799.99, 1299.99, "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/8f2c5d4e-3b1a-4c6d-7e8f-9a0b1c2d3e4f/brasilia-training-backpack-9-0-XL-2VnqHZ.png", 4.4, "Accessories", "Nike", "Durable training backpack with multiple compartments", 85, "ACTIVE", "Sports", "Unisex");
        addProductIfNotExists("SUNGLASSES001", "Ray-Ban Aviator Classic", 2999.99, 4499.99, "https://assets.ray-ban.com/is/image/RayBan/8056597177290__STD__shad__qt.png", 4.7, "Accessories", "Ray-Ban", "Classic aviator sunglasses with UV protection", 30, "ACTIVE", "Premium", "Unisex");
    }
    
    private void addProductIfNotExists(String id, String name, Double price, Double originalPrice, String imageUrl, 
                                     Double rating, String category, String brand, String description, 
                                     Integer stock, String status, String collection, String gender) {
        if (!productRepository.existsById(id)) {
            productRepository.save(createProduct(id, name, price, originalPrice, imageUrl, rating, category, brand, description, stock, status, collection, gender));
        }
    }

    private Product createProduct(String id, String name, Double price, Double originalPrice, String imageUrl, 
                                Double rating, String category, String brand, String description, 
                                Integer stock, String status, String collection, String gender) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setOriginalPrice(originalPrice);
        product.setImageUrl(imageUrl);
        product.setRating(rating);
        product.setCategory(category);
        product.setBrand(brand);
        product.setDescription(description);
        product.setStock(stock);
        product.setStatus(status);
        product.setCollection(collection);
        product.setGender(gender);
        return product;
    }
}