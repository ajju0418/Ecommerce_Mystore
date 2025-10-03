package com.estore.product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.estore.product.entity.Product;
import com.estore.product.repository.ProductRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            initializeProducts();
        }
    }

    private void initializeProducts() {
        // Shirts Category
        createProduct("SHIRT001", "Arrow Classic White Shirt", 39.99, 49.99, "https://m.media-amazon.com/images/I/61DP9X8QgWL._SY741_.jpg", 4.5, "Clothing", "Arrow", "Classic fit white formal shirt, 100% cotton", 60, "ACTIVE", "Shirts", "Men");
        createProduct("SHIRT002", "Van Heusen Slim Blue Shirt", 44.99, 54.99, "https://m.media-amazon.com/images/I/61Wd5xQOHCL._SX679_.jpg", 4.4, "Clothing", "Van Heusen", "Slim fit blue shirt, wrinkle-free fabric", 50, "ACTIVE", "Shirts", "Men");
        createProduct("SHIRT003", "Peter England Checked Shirt", 34.99, 44.99, "https://m.media-amazon.com/images/I/81iqN93d4kL._SX679_.jpg", 4.3, "Clothing", "Peter England", "Casual checked shirt with button-down collar", 55, "ACTIVE", "Shirts", "Men");

        // T-Shirts Category
        createProduct("TSHIRT001", "Puma Graphic Tee", 24.99, 29.99, "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_450,h_450/global/586668/01/mod01/fnd/IND/fmt/png/Graphic-Tee", 4.3, "Clothing", "Puma", "Cotton crew neck t-shirt with graphic print", 80, "ACTIVE", "T-Shirts", "Unisex");
        createProduct("TSHIRT002", "H&M Basic Black Tee", 19.99, 24.99, "https://m.media-amazon.com/images/I/51CbQi8QLzL._SX679_.jpg", 4.2, "Clothing", "H&M", "Basic black t-shirt, regular fit", 100, "ACTIVE", "T-Shirts", "Unisex");
        createProduct("TSHIRT003", "Adidas Performance Logo Tee", 29.99, 34.99, "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/3cbc05f49b8f4697b0b4aad6009a4db0_9366/Essentials_Logo_T-Shirt_Black_GK9121_01_laydown.jpg", 4.4, "Clothing", "Adidas", "Performance t-shirt with logo print, moisture-wicking fabric", 90, "ACTIVE", "T-Shirts", "Unisex");

        // Jeans Category
        createProduct("JEANS001", "Levi's 511 Slim Jeans", 59.99, 69.99, "https://lsco.scene7.com/is/image/lsco/045114406-front-pdp-lse", 4.6, "Clothing", "Levi's", "Slim fit stretch jeans, classic 5-pocket styling", 70, "ACTIVE", "Jeans", "Men");
        createProduct("JEANS002", "Pepe Skinny Fit Jeans", 54.99, 64.99, "https://m.media-amazon.com/images/I/71eTwOJAsBL._SY741_.jpg", 4.4, "Clothing", "Pepe Jeans", "Skinny fit navy jeans, soft denim", 60, "ACTIVE", "Jeans", "Women");
        createProduct("JEANS003", "Wrangler Regular Fit Jeans", 49.99, 59.99, "https://m.media-amazon.com/images/I/71DXSyxCu-L._SY741_.jpg", 4.3, "Clothing", "Wrangler", "Regular fit straight leg jeans, durable denim", 65, "ACTIVE", "Jeans", "Men");

        // Jackets Category
        createProduct("JACKET001", "Woodland Bomber Jacket", 89.99, 109.99, "https://m.media-amazon.com/images/I/61LRNG5jIkL._SY741_.jpg", 4.5, "Clothing", "Woodland", "Bomber jacket with ribbed cuffs and hem", 40, "ACTIVE", "Jackets", "Men");
        createProduct("JACKET002", "Zara Faux Leather Jacket", 99.99, 129.99, "https://static.zara.net/photos///2023/I/0/1/p/3042/300/800/2/w/850/3042300800_1_1_1.jpg", 4.6, "Clothing", "Zara", "Faux leather biker jacket, zip closure", 35, "ACTIVE", "Jackets", "Women");
        createProduct("JACKET003", "Columbia Waterproof Jacket", 129.99, 149.99, "https://columbia.scene7.com/is/image/ColumbiaSportswear2/1800661_010_f?wid=768&hei=806&v=1652732144", 4.7, "Clothing", "Columbia", "Waterproof breathable jacket with hood, perfect for outdoor activities", 30, "ACTIVE", "Jackets", "Unisex");
        
        // Shoes Category
        createProduct("SHOES001", "Nike Air Max Running Shoes", 119.99, 139.99, "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/7fbc5e94-8d49-4730-a280-f19d3cfad0b0/air-max-90-mens-shoes-6n3vKB.png", 4.7, "Footwear", "Nike", "Air Max cushioning for maximum comfort, mesh upper for breathability", 45, "ACTIVE", "Shoes", "Men");
        createProduct("SHOES002", "Adidas Ultraboost Women's Shoes", 149.99, 169.99, "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/fbaf991a78bc4896a3e9ad7800abcec6_9366/Ultraboost_22_Shoes_Black_GZ0127_01_standard.jpg", 4.8, "Footwear", "Adidas", "Responsive Boost midsole, Primeknit upper, Continental rubber outsole", 40, "ACTIVE", "Shoes", "Women");
        createProduct("SHOES003", "Puma Casual Sneakers", 79.99, 99.99, "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/374915/01/sv01/fnd/IND/fmt/png/Rebound-Joy-SoftFoam+-Shoes", 4.5, "Footwear", "Puma", "SoftFoam+ sockliner for superior cushioning, synthetic leather upper", 50, "ACTIVE", "Shoes", "Unisex");
        
        // Accessories Category
        createProduct("ACC001", "Fossil Leather Wallet", 49.99, 59.99, "https://fossil.scene7.com/is/image/FossilPartners/ML4161200_main?$sfcc_fos_medium$", 4.6, "Accessories", "Fossil", "Genuine leather bifold wallet with RFID protection", 75, "ACTIVE", "Accessories", "Men");
        createProduct("ACC002", "Ray-Ban Aviator Sunglasses", 149.99, 179.99, "https://images.ray-ban.com/is/image/RayBan/805289602057__STD__shad__qt.png?impolicy=RB_Product&width=1024&bgc=%23f2f2f2", 4.8, "Accessories", "Ray-Ban", "Classic aviator sunglasses with polarized lenses", 30, "ACTIVE", "Accessories", "Unisex");
        createProduct("ACC003", "Michael Kors Crossbody Bag", 199.99, 249.99, "https://michaelkors.scene7.com/is/image/MichaelKors/35F0GTVC3L-0001_1?wid=1300&hei=1624&fmt=jpeg&qlt=90&op_sharpen=0&resMode=sharp2&op_usm=0.9,1.0,8,0", 4.7, "Accessories", "Michael Kors", "Saffiano leather crossbody bag with adjustable strap", 25, "ACTIVE", "Accessories", "Women");
        
        // Pants Category
        createProduct("PANTS001", "Dockers Khaki Pants", 49.99, 59.99, "https://m.media-amazon.com/images/I/71RR8wUJR7L._AC_UX569_.jpg", 4.4, "Clothing", "Dockers", "Classic fit khaki pants with stretch waistband", 55, "ACTIVE", "Pants", "Men");
        createProduct("PANTS002", "H&M Slim Fit Chinos", 39.99, 49.99, "https://lp2.hm.com/hmgoepprod?set=quality%5B79%5D%2Csource%5B%2F00%2F75%2F0075e0c80c55adbe45c3c9c40cbc68530d0e01a8.jpg%5D%2Corigin%5Bdam%5D%2Ccategory%5B%5D%2Ctype%5BDESCRIPTIVESTILLLIFE%5D%2Cres%5Bm%5D%2Chmver%5B2%5D&call=url[file:/product/main]", 4.3, "Clothing", "H&M", "Slim fit cotton chinos, versatile for casual or formal wear", 60, "ACTIVE", "Pants", "Men");
        createProduct("PANTS003", "Zara High Waist Trousers", 59.99, 69.99, "https://static.zara.net/photos///2023/I/0/1/p/7385/242/800/2/w/850/7385242800_1_1_1.jpg", 4.5, "Clothing", "Zara", "High waist tailored trousers with belt loops", 45, "ACTIVE", "Pants", "Women");
        
        // Sweaters Category
        createProduct("SWEATER001", "Gap Cable Knit Sweater", 59.99, 69.99, "https://www.gap.com/webcontent/0028/629/084/cn28629084.jpg", 4.4, "Clothing", "Gap", "Cable knit sweater in soft cotton blend", 40, "ACTIVE", "Sweaters", "Unisex");
        createProduct("SWEATER002", "Ralph Lauren V-Neck Sweater", 89.99, 99.99, "https://www.rlmedia.io/is/image/PoloGSI/s7-1442590_lifestyle?$rl_df_pdp_5_7_lif$", 4.6, "Clothing", "Ralph Lauren", "V-neck sweater in merino wool, signature embroidered logo", 35, "ACTIVE", "Sweaters", "Men");
        createProduct("SWEATER003", "Zara Oversized Turtleneck", 69.99, 79.99, "https://static.zara.net/photos///2023/I/0/1/p/5802/124/704/2/w/850/5802124704_1_1_1.jpg", 4.5, "Clothing", "Zara", "Oversized turtleneck sweater in chunky knit", 30, "ACTIVE", "Sweaters", "Women");
        
        // Hoodies Category
        createProduct("HOODIE001", "Nike Sportswear Club Hoodie", 54.99, 64.99, "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/5c87ff1e-e4e7-4494-9f9a-9aa1b1aedcb6/sportswear-club-fleece-pullover-hoodie-Gw4Nwq.png", 4.6, "Clothing", "Nike", "Fleece lined hoodie with front kangaroo pocket", 65, "ACTIVE", "Hoodies", "Unisex");
        createProduct("HOODIE002", "Adidas Originals Trefoil Hoodie", 64.99, 74.99, "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/fbd5b8f0a16347b7b71aab7800b3f240_9366/Trefoil_Hoodie_Black_DT7964_01_laydown.jpg", 4.7, "Clothing", "Adidas", "Cotton blend hoodie with Trefoil logo print", 60, "ACTIVE", "Hoodies", "Unisex");
        createProduct("HOODIE003", "Champion Reverse Weave Hoodie", 59.99, 69.99, "https://m.media-amazon.com/images/I/61MVdCYfbOL._AC_UX569_.jpg", 4.5, "Clothing", "Champion", "Reverse Weave technology to resist shrinkage, heavyweight fabric", 55, "ACTIVE", "Hoodies", "Unisex");
    }

    private void createProduct(String id, String name, Double price, Double originalPrice, String imageUrl, Double rating, 
                             String category, String brand, String description, Integer stock, String status, String collection, String gender) {
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
        
        productRepository.save(product);
    }
}