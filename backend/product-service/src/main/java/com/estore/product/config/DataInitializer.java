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
        if (productRepository.count() == 0) {
            initializeProducts();
        }
    }

    private void initializeProducts() {
        // Shirts
        productRepository.save(createProduct("SHIRT001", "Arrow Classic White Shirt", 1199.99, 1999.99, "https://m.media-amazon.com/images/I/61DP9X8QgWL._SY741_.jpg", 4.5, "Shirts", "Arrow", "Classic fit white formal shirt, 100% cotton", 60, "ACTIVE", "Men's", "Men"));
        productRepository.save(createProduct("SHIRT002", "Van Heusen Slim Blue Shirt", 1067.99, 1779.99, "https://m.media-amazon.com/images/I/61Wd5xQOHCL._SX679_.jpg", 4.4, "Shirts", "Van Heusen", "Slim fit blue shirt, wrinkle-free fabric", 50, "ACTIVE", "Casual", "Men"));
        productRepository.save(createProduct("SHIRT003", "Peter England Checked Shirt", 935.99, 1559.99, "https://m.media-amazon.com/images/I/81iqN93d4kL._SX679_.jpg", 4.3, "Shirts", "Peter England", "Casual checked shirt with button-down collar", 55, "ACTIVE", "Casual", "Men"));
        productRepository.save(createProduct("SHIRT004", "Tommy Hilfiger Oxford Shirt", 1535.99, 2559.99, "https://www.tommy.com/dw/image/v2/AAQA_PRD/on/demandware.static/-/Sites-tommy-master-catalog/default/dw3d5f6d0c/products/78J8440-YCI/78J8440-YCI_MAIN.jpg", 4.6, "Shirts", "Tommy Hilfiger", "Regular fit oxford shirt with subtle logo", 45, "ACTIVE", "Premium", "Men"));
        productRepository.save(createProduct("SHIRT005", "U.S. Polo Assn. Striped Shirt", 677.99, 1129.99, "https://uspoloassn.com/media/catalog/product/cache/6b6b6e9b3e8a3f7f8b56f0b9f68ad7a6/usp232.jpg", 4.3, "Shirts", "U.S. Polo Assn.", "Casual striped woven shirt", 50, "ACTIVE", "Casual", "Men"));

        // T-Shirts
        productRepository.save(createProduct("TSHIRT001", "Puma Graphic Tee", 714.59, 1190.99, "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_450,h_450/global/586668/01/mod01/fnd/IND/fmt/png/Graphic-Tee.jpg", 4.3, "T-Shirts", "Puma", "Cotton crew neck t-shirt with graphic print", 80, "ACTIVE", "Casual", "Unisex"));
        productRepository.save(createProduct("TSHIRT002", "H&M Basic Black Tee", 594.59, 990.99, "https://m.media-amazon.com/images/I/51CbQi8QLzL._SX679_.jpg", 4.2, "T-Shirts", "H&M", "Basic black t-shirt, regular fit", 100, "ACTIVE", "Casual", "Unisex"));
        productRepository.save(createProduct("TSHIRT003", "Adidas Performance Logo Tee", 774.59, 1290.99, "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/3cbc05f49b8f4697b0b4aad6009a4db0_9366/Essentials_Logo_T-Shirt_Black_GK9121_01_laydown.jpg", 4.4, "T-Shirts", "Adidas", "Performance t-shirt with logo print, moisture-wicking fabric", 90, "ACTIVE", "Sports", "Unisex"));
        productRepository.save(createProduct("TSHIRT004", "Tommy Hilfiger Logo Tee", 719.99, 1199.99, "https://www.tommy.com/dw/image/v2/AAQA_PRD/on/demandware.static/-/Sites-tommy-master-catalog/default/dwek-xx/products/78JA566-YCI/78JA566-YCI_MAIN.jpg", 4.5, "T-Shirts", "Tommy Hilfiger", "Slim fit logo tee", 70, "ACTIVE", "New Arrivals", "Unisex"));
        productRepository.save(createProduct("TSHIRT005", "Nike Dri-FIT Tee", 899.99, 1499.99, "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/13b8a8f6-d9b3-4a2f-b3a9-f0c2a2a6c4ef/dri-fit-short-sleeve-t-shirt-Q8W1MZ.jpg", 4.7, "T-Shirts", "Nike", "Dri-FIT performance tee", 85, "ACTIVE", "Sports", "Unisex"));

        // Jeans
        productRepository.save(createProduct("JEANS001", "Levi's 511 Slim Jeans", 1499.99, 2499.99, "https://lsco.scene7.com/is/image/lsco/045114406-front-pdp-lse.jpg", 4.6, "Jeans", "Levi's", "Slim fit stretch jeans, classic 5-pocket styling", 70, "ACTIVE", "Casual", "Men"));
        productRepository.save(createProduct("JEANS002", "Pepe Skinny Fit Jeans", 1319.99, 2199.99, "https://m.media-amazon.com/images/I/71eTwOJAsBL._SY741_.jpg", 4.4, "Jeans", "Pepe Jeans", "Skinny fit navy jeans, soft denim", 60, "ACTIVE", "Casual", "Women"));
        productRepository.save(createProduct("JEANS003", "Wrangler Regular Fit Jeans", 719.99, 1199.99, "https://m.media-amazon.com/images/I/71DXSyxCu-L._SY741_.jpg", 4.3, "Jeans", "Wrangler", "Regular fit straight leg jeans, durable denim", 65, "ACTIVE", "Casual", "Men"));
        productRepository.save(createProduct("JEANS004", "Levi's 512 Slim Taper Jeans", 1319.99, 2199.99, "https://lsco.scene7.com/is/image/lsco/045125406-front-pdp-lse.jpg", 4.5, "Jeans", "Levi's", "Slim taper with stretch", 60, "ACTIVE", "Casual", "Men"));
        productRepository.save(createProduct("JEANS005", "GAP Mid Rise Skinny Jeans", 899.99, 1499.99, "https://www.gap.com/webcontent/0028/409/549/cn28409549.jpg", 4.2, "Jeans", "Gap", "Mid-rise skinny jeans for women", 50, "ACTIVE", "Women's", "Women"));

        // Jackets
        productRepository.save(createProduct("JACKET001", "Woodland Bomber Jacket", 779.99, 1299.99, "https://m.media-amazon.com/images/I/61LRNG5jIkL._SY741_.jpg", 4.5, "Jackets", "Woodland", "Bomber jacket with ribbed cuffs and hem", 40, "ACTIVE", "Casual", "Men"));
        productRepository.save(createProduct("JACKET002", "Zara Faux Leather Jacket", 809.99, 1349.99, "https://static.zara.net/photos///2023/I/0/1/p/3042/300/800/2/w/850/3042300800_1_1_1.jpg", 4.6, "Jackets", "Zara", "Faux leather biker jacket, zip closure", 35, "ACTIVE", "Premium", "Women"));
        productRepository.save(createProduct("JACKET003", "Columbia Waterproof Jacket", 839.99, 1399.99, "https://columbia.scene7.com/is/image/ColumbiaSportswear2/1800661_010_f.jpg", 4.7, "Jackets", "Columbia", "Waterproof breathable jacket with hood, perfect for outdoor activities", 30, "ACTIVE", "Sports", "Unisex"));
        productRepository.save(createProduct("JACKET004", "The North Face Insulated Jacket", 899.99, 1499.99, "https://images.thenorthface.com/is/image/TheNorthFace/NF0A4R8X_MAIN.jpg", 4.8, "Jackets", "The North Face", "Insulated parka for cold weather", 25, "ACTIVE", "Premium", "Unisex"));
        productRepository.save(createProduct("JACKET005", "Levi's Denim Trucker Jacket", 719.99, 1199.99, "https://lsco.scene7.com/is/image/lsco/723340496-front-pdp-lse.jpg", 4.6, "Jackets", "Levi's", "Classic denim trucker jacket", 40, "ACTIVE", "Casual", "Men"));

        // Shoes
        productRepository.save(createProduct("SHOES001", "Nike Air Max Running Shoes", 779.99, 1299.99, "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/7fbc5e94-8d49-4730-a280-f19d3cfad0b0/air-max-90-mens-shoes-6n3vKB.jpg", 4.7, "Shoes", "Nike", "Air Max cushioning for maximum comfort, mesh upper for breathability", 45, "ACTIVE", "Sports", "Men"));
        productRepository.save(createProduct("SHOES002", "Adidas Ultraboost Women's Shoes", 797.99, 1329.99, "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/fbaf991a78bc4896a3e9ad7800abcec6_9366/Ultraboost_22_Shoes_Black_GZ0127_01_standard.jpg", 4.8, "Shoes", "Adidas", "Responsive Boost midsole, Primeknit upper, Continental rubber outsole", 40, "ACTIVE", "Premium", "Women"));
        productRepository.save(createProduct("SHOES003", "Puma Casual Sneakers", 707.99, 1179.99, "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/374915/01/sv01/fnd/IND/fmt/png/Rebound-Joy-SoftFoam+-Shoes.jpg", 4.5, "Shoes", "Puma", "SoftFoam+ sockliner for superior cushioning, synthetic leather upper", 50, "ACTIVE", "Casual", "Unisex"));
        productRepository.save(createProduct("SHOES004", "New Balance 574 Core", 677.99, 1129.99, "https://nb.scene7.com/is/image/NB/m5740nb_01_i.jpg", 4.5, "Shoes", "New Balance", "Everyday running-inspired sneaker", 60, "ACTIVE", "Casual", "Unisex"));
        productRepository.save(createProduct("SHOES005", "ASICS Gel-Kayano Running Shoes", 731.99, 1219.99, "https://asics.scene7.com/is/image/asics/1011A840_001_SR_RT_GLB.jpg", 4.7, "Shoes", "ASICS", "Stability running shoe with GEL cushioning", 35, "ACTIVE", "Sports", "Men"));

        // Accessories
        productRepository.save(createProduct("ACC001", "Fossil Leather Wallet", 899.99, 1499.99, "https://fossil.scene7.com/is/image/FossilPartners/ML4161200_main.jpg", 4.6, "Accessories", "Fossil", "Genuine leather bifold wallet with RFID protection", 75, "ACTIVE", "Accessories", "Men"));
        productRepository.save(createProduct("ACC002", "Ray-Ban Aviator Sunglasses", 719.99, 1199.99, "https://images.ray-ban.com/is/image/RayBan/805289602057__STD__shad__qt.jpg", 4.8, "Accessories", "Ray-Ban", "Classic aviator sunglasses with polarized lenses", 30, "ACTIVE", "Premium", "Unisex"));
        productRepository.save(createProduct("ACC003", "Michael Kors Crossbody Bag", 839.99, 1399.99, "https://michaelkors.scene7.com/is/image/MichaelKors/35F0GTVC3L-0001_1.jpg", 4.7, "Accessories", "Michael Kors", "Saffiano leather crossbody bag with adjustable strap", 25, "ACTIVE", "Premium", "Women"));
        productRepository.save(createProduct("ACC004", "Coach Leather Card Case", 719.99, 1199.99, "https://www.coach.com/dw/image/v2/AAQM_PRD/on/demandware.static/-/Sites-coach-master/default/dw1234abcd/products/88513_OLH_MAIN.jpg", 4.6, "Accessories", "Coach", "Slim leather card case", 80, "ACTIVE", "Premium", "Unisex"));
        productRepository.save(createProduct("ACC005", "Beats Studio3 Wireless Headphones", 809.99, 1349.99, "https://www.beatsbydre.com/content/dam/brands/beats/landing-pages/studio3/products/studio3-gallery-1.jpg", 4.7, "Accessories", "Beats", "Noise cancelling over-ear headphones", 20, "ACTIVE", "Premium", "Unisex"));

        // Hoodies
        productRepository.save(createProduct("HOODIE001", "Nike Sportswear Club Hoodie", 839.99, 1399.99, "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/5c87ff1e-e4e7-4494-9f9a-9aa1b1aedcb6/sportswear-club-fleece-pullover-hoodie-Gw4Nwq.jpg", 4.6, "Hoodies", "Nike", "Fleece lined hoodie with front kangaroo pocket", 65, "ACTIVE", "Sports", "Unisex"));
        productRepository.save(createProduct("HOODIE002", "Adidas Originals Trefoil Hoodie", 899.99, 1499.99, "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/fbd5b8f0a16347b7b71aab7800b3f240_9366/Trefoil_Hoodie_Black_DT7964_01_laydown.jpg", 4.7, "Hoodies", "Adidas", "Cotton blend hoodie with Trefoil logo print", 60, "ACTIVE", "Casual", "Unisex"));
        productRepository.save(createProduct("HOODIE003", "Champion Reverse Weave Hoodie", 839.99, 1399.99, "https://m.media-amazon.com/images/I/61MVdCYfbOL._AC_UX569_.jpg", 4.5, "Hoodies", "Champion", "Reverse Weave technology to resist shrinkage, heavyweight fabric", 55, "ACTIVE", "Casual", "Unisex"));
        productRepository.save(createProduct("HOODIE004", "Patagonia P-6 Logo Uprisal Hoodie", 719.99, 1199.99, "https://www.patagonia.com/on/demandware.static/-/Sites-patagonia-master/default/dw6f6c8d6f/images/hi-res/39575_BLK.jpg", 4.7, "Hoodies", "Patagonia", "Recycled fleece hoodie with logo", 40, "ACTIVE", "Premium", "Unisex"));
        productRepository.save(createProduct("HOODIE005", "Under Armour Rival Fleece Hoodie", 671.99, 1119.99, "https://www.underarmour.com/dw/image/v2/AASK_PRD/on/demandware.static/-/Sites-underarmour-master-catalog/default/dw9f4d5a99/images/1327544-001/1327544-001_A.jpg", 4.5, "Hoodies", "Under Armour", "Warm fleece hoodie, loose fit", 65, "ACTIVE", "Sports", "Unisex"));
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