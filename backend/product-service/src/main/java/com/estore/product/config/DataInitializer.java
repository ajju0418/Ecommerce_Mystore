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
        // Electronics Category
        createProduct("ELEC001", "iPhone 15 Pro", 999.99, 1199.99, "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-15-pro-finish-select-202309-6-1inch-naturaltitanium.jpg", 4.8, "Electronics", "Apple", "Latest iPhone 15 Pro with titanium design, A17 Pro chip, and advanced camera system", 50, "ACTIVE", "Smartphones", "Unisex");
        createProduct("ELEC002", "Samsung Galaxy S24 Ultra", 1199.99, 1399.99, "https://images.samsung.com/is/image/samsung/p6pim/in/2401/gallery/in-galaxy-s24-ultra-s928-sm-s928bztqins-thumb-539573073.jpg", 4.7, "Electronics", "Samsung", "Premium Galaxy S24 Ultra with S Pen, 200MP camera, and AI features", 45, "ACTIVE", "Smartphones", "Unisex");
        createProduct("ELEC003", "MacBook Air M3", 1299.99, 1499.99, "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/macbook-air-midnight-select-20220606.jpg", 4.9, "Electronics", "Apple", "Ultra-thin MacBook Air with M3 chip, 13-inch Liquid Retina display", 30, "ACTIVE", "Laptops", "Unisex");
        createProduct("ELEC004", "Dell XPS 13 Plus", 1199.99, 1399.99, "https://i.dell.com/is/image/DellContent/content/dam/ss2/product-images/dell-client-products/notebooks/xps-notebooks/xps-13-9320/media-gallery/notebook-xps-13-9320-nt-blue-gallery-4.psd", 4.6, "Electronics", "Dell", "Premium ultrabook with 12th Gen Intel processors and InfinityEdge display", 25, "ACTIVE", "Laptops", "Unisex");
        createProduct("ELEC005", "Sony WH-1000XM5", 399.99, 449.99, "https://www.sony.com/image/5d02da5df552836db894c04e7e6e5e8e?fmt=pjpeg&wid=330&bgcolor=FFFFFF&bgc=FFFFFF", 4.8, "Electronics", "Sony", "Industry-leading noise canceling wireless headphones with 30-hour battery", 60, "ACTIVE", "Audio", "Unisex");

        // Clothing Category
        createProduct("CLOTH001", "Nike Air Max 270", 149.99, 179.99, "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/awjogtdnqxniqqk0wpgf/air-max-270-mens-shoes-KkLcGR.png", 4.5, "Clothing", "Nike", "Comfortable lifestyle shoes with Max Air unit for all-day comfort", 80, "ACTIVE", "Footwear", "Men");
        createProduct("CLOTH002", "Adidas Ultraboost 22", 179.99, 199.99, "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/fbaf991a78bc4896a3e9ad7800abcec6_9366/Ultraboost_22_Shoes_Black_GZ0127_01_standard.jpg", 4.6, "Clothing", "Adidas", "Premium running shoes with BOOST midsole and Primeknit upper", 70, "ACTIVE", "Footwear", "Unisex");
        createProduct("CLOTH003", "Levi's 501 Original Jeans", 89.99, 109.99, "https://lsco.scene7.com/is/image/lsco/005010114-front-pdp-lse", 4.4, "Clothing", "Levi's", "Classic straight-fit jeans with button fly, the original blue jean since 1873", 100, "ACTIVE", "Denim", "Unisex");
        createProduct("CLOTH004", "H&M Cotton T-Shirt", 19.99, 24.99, "https://lp2.hm.com/hmgoepprod?set=source[/82/31/8231a8b7c6d5e4f3g2h1i0j9k8l7m6n5.jpg],origin[dam],category[men_tshirtstanks_shortsleeve],type[DESCRIPTIVESTILLLIFE],res[m],hmver[2]&call=url[file:/product/main]", 4.2, "Clothing", "H&M", "Basic cotton t-shirt in regular fit, soft and comfortable fabric", 150, "ACTIVE", "Tops", "Unisex");
        createProduct("CLOTH005", "Zara Structured Blazer", 129.99, 159.99, "https://static.zara.net/photos///2023/V/02/1/p/7545/420/800/2/w/850/7545420800_1_1_1.jpg", 4.3, "Clothing", "Zara", "Professional structured blazer with lapel collar and front button fastening", 40, "ACTIVE", "Formal", "Women");

        // Shirts Category
        createProduct("SHIRT001", "Arrow Classic White Shirt", 39.99, 49.99, "https://assets.ajio.com/medias/sys_master/root/20230624/0Qw2/6496e2e3eebac147fcf1e2e2/-473Wx593H-465336049-white-MODEL.jpg", 4.5, "Clothing", "Arrow", "Classic fit white formal shirt, 100% cotton", 60, "ACTIVE", "Shirts", "Men");
        createProduct("SHIRT002", "Van Heusen Slim Blue Shirt", 44.99, 54.99, "https://assets.ajio.com/medias/sys_master/root/20230624/0Qw2/6496e2e3eebac147fcf1e2e2/-473Wx593H-465336049-blue-MODEL.jpg", 4.4, "Clothing", "Van Heusen", "Slim fit blue shirt, wrinkle-free fabric", 50, "ACTIVE", "Shirts", "Men");

        // T-Shirts Category
        createProduct("TSHIRT001", "Puma Graphic Tee", 24.99, 29.99, "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_450,h_450/global/586668/01/mod01/fnd/IND/fmt/png/Graphic-Tee", 4.3, "Clothing", "Puma", "Cotton crew neck t-shirt with graphic print", 80, "ACTIVE", "T-Shirts", "Unisex");
        createProduct("TSHIRT002", "H&M Basic Black Tee", 19.99, 24.99, "https://lp2.hm.com/hmgoepprod?set=source[/45/67/4567b8c9d0e1f2g3h4i5j6k7l8m9n0.jpg],origin[dam],category[men_tshirtstanks_shortsleeve],type[DESCRIPTIVESTILLLIFE],res[m],hmver[2]&call=url[file:/product/main]", 4.2, "Clothing", "H&M", "Basic black t-shirt, regular fit", 100, "ACTIVE", "T-Shirts", "Unisex");

        // Jeans Category
        createProduct("JEANS001", "Levi's 511 Slim Jeans", 59.99, 69.99, "https://lsco.scene7.com/is/image/lsco/045114406-front-pdp-lse", 4.6, "Clothing", "Levi's", "Slim fit stretch jeans, classic 5-pocket styling", 70, "ACTIVE", "Jeans", "Men");
        createProduct("JEANS002", "Pepe Skinny Fit Jeans", 54.99, 64.99, "https://assets.ajio.com/medias/sys_master/root/20230624/0Qw2/6496e2e3eebac147fcf1e2e2/-473Wx593H-465336049-navy-WOMEN.jpg", 4.4, "Clothing", "Pepe Jeans", "Skinny fit navy jeans, soft denim", 60, "ACTIVE", "Jeans", "Women");

        // Jackets Category
        createProduct("JACKET001", "Woodland Bomber Jacket", 89.99, 109.99, "https://assets.ajio.com/medias/sys_master/root/20230624/0Qw2/6496e2e3eebac147fcf1e2e2/-473Wx593H-465336049-green-JACKET.jpg", 4.5, "Clothing", "Woodland", "Bomber jacket with ribbed cuffs and hem", 40, "ACTIVE", "Jackets", "Men");
        createProduct("JACKET002", "Zara Faux Leather Jacket", 99.99, 129.99, "https://static.zara.net/photos///2023/I/0/1/p/3042/300/800/2/w/850/3042300800_1_1_1.jpg", 4.6, "Clothing", "Zara", "Faux leather biker jacket, zip closure", 35, "ACTIVE", "Jackets", "Women");
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