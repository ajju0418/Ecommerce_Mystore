package com.estore.product.service;

import com.estore.product.entity.Product;
import com.estore.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class ProductDataInitializationService implements CommandLineRunner {
    
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            initializeProducts();
            System.out.println("Product data initialized with " + productRepository.count() + " products");
        }
    }

    private void initializeProducts() {
        productRepository.save(new Product("1", "Oxford Cotton Shirt", 1560.0, "https://th.bing.com/th/id/OIP.jsDkBquBynGaYPt8GV5TaAHaHa?w=250&h=250&c=8&rs=1&qlt=70&o=7&cb=thws4&dpr=1.5&pid=3.1&rm=3", 4.1, "New Arrivals", "Shirts", "Men"));
        productRepository.save(new Product("5", "Linen Button-Up Shirt", 1120.0, "https://th.bing.com/th/id/OIP.Y__aeWa76DoLyrvCcEJVNAHaHa?w=250&h=250&c=8&rs=1&qlt=70&o=7&cb=thws4&dpr=1.5&pid=3.1&rm=3", 4.7, "Deal of the Day", "Shirts", "Women"));
        productRepository.save(new Product("9", "Casual Denim Shirt", 1590.0, "https://www.bing.com/th/id/OIP.GZd2gtiBcLNwMudCHksWVgHaJf?w=220&h=283&c=8&rs=1&qlt=70&o=7&cb=thws4&dpr=1.5&pid=3.1&rm=3", 3.7, "Deal of the Day", "Shirts", "Men"));
        productRepository.save(new Product("10", "Silk Scarf", 1450.0, "https://th.bing.com/th/id/OIP._-pGy0KKwqHZs5GjxzM3CwHaHa?w=250&h=250&c=8&rs=1&qlt=70&o=7&cb=thws4&dpr=1.5&pid=3.1&rm=3", 4.4, "Support", "Accessories", "Women"));
        productRepository.save(new Product("11", "Urban Fit Tee", 1760.0, "https://i5.walmartimages.com/seo/Gildan-Crew-Neck-Cotton-T-Shirt-Men_4ce6f96e-7f3a-4de6-a3b5-a8451b41b7ed.c58ab222c534ef2d99bc88a8577e2167.jpeg?odnHeight=372&odnWidth=372&odnBg=FFFFFF", 3.9, "New Arrivals", "T-Shirts", "Men"));
        productRepository.save(new Product("12", "Pleated Trousers", 890.0, "https://thfvnext.bing.com/th/id/OIP.vyzk4DhjflnOwai6VM68AwHaJ4?w=202&h=269&c=7&r=0&o=7&cb=thfvnext&dpr=1.5&pid=1.7&rm=3", 4.1, "Sale", "Trousers", "Women"));
        productRepository.save(new Product("13", "Pearl Necklace", 1190.0, "https://thfvnext.bing.com/th/id/OIP.mGo99saA9N-PYHHiJ_gjiQHaHa?w=184&h=184&c=7&r=0&o=7&cb=thfvnext&dpr=1.5&pid=1.7&rm=3", 4.3, "Best Seller", "Accessories", "Women"));
        productRepository.save(new Product("14", "Striped Shirt Dress", 1380.0, "https://thfvnext.bing.com/th/id/OIP.DgVRfPR9YoDUVDIaJ_LG8AAAAA?cb=thfvnext&rs=1&pid=ImgDetMain&o=7&rm=3", 4.0, "Support", "Shirts", "Women"));
        productRepository.save(new Product("15", "Boho Graphic Tee", 990.0, "https://thfvnext.bing.com/th/id/OIP.Nx_LxjkNhQ7vnrZ3SQ4ZUwHaE8?cb=thfvnext&rs=1&pid=ImgDetMain&o=7&rm=3", 4.2, "New Arrivals", "T-Shirts", "Women"));
        productRepository.save(new Product("16", "Silver Pendant", 1530.0, "https://thfvnext.bing.com/th/id/OIP.JqAU49GCNWYws7Cm436yKgHaHN?w=207&h=201&c=7&r=0&o=7&cb=thfvnext&dpr=1.5&pid=1.7&rm=3", 3.8, "Sale", "Accessories", "Women"));
        productRepository.save(new Product("17", "Cargo Pants", 1720.0, "https://thfvnext.bing.com/th/id/OIP.H84VuUmO4IOQiRNO3LnRPQHaLL?w=130&h=196&c=7&r=0&o=7&cb=thfvnext&dpr=1.5&pid=1.7&rm=3", 4.6, "Deal of the Day", "Trousers", "Men"));
        productRepository.save(new Product("18", "Chambray Shirt", 1110.0, "https://assets.myntassets.com/h_200,w_200,c_fill,g_auto/h_1440,q_100,w_1080/v1/assets/images/17852642/2022/7/18/b839fec2-eb20-4831-8bcc-541ee8d6b9b01658139170541-Blackberrys-Men-Green--Grey-Slim-Fit-Printed-Casual-Shirt-73-1.jpg", 4.0, "Best Seller", "Shirts", "Men"));
        productRepository.save(new Product("19", "Retro Logo Tee", 1270.0, "https://thfvnext.bing.com/th?q=T-Shirt+Pic+for+HD&w=120&h=120&c=1&rs=1&qlt=70&o=7&cb=1&dpr=1.5&pid=InlineBlock&rm=3&mkt=en-IN&cc=IN&setlang=en&adlt=strict&t=1&mw=247", 4.3, "Support", "T-Shirts", "Men"));
        productRepository.save(new Product("20", "Beaded Bracelet", 1600.0, "https://thfvnext.bing.com/th/id/OIP.gtuUhw7H0qNqucaMIEDLKQHaJ4?w=138&h=183&c=7&r=0&o=7&cb=thfvnext&dpr=1.5&pid=1.7&rm=3", 4.5, "New Arrivals", "Accessories", "Women"));
        productRepository.save(new Product("25", "Floral Shirt", 1180.0, "https://thfvnext.bing.com/th/id/OIP.OE3fnNrU8OaZ-zcW1IuCJQHaJ4?cb=thfvnext&pid=ImgDet&w=178&h=237&c=7&dpr=1.5&o=7&rm=3", 4.6, "Deal of the Day", "Shirts", "Women"));
        productRepository.save(new Product("26", "Basic Cotton Tee", 1010.0, "https://thfvnext.bing.com/th/id/OIP.nsWi4J7KPsdxUNuvIGP5mwHaJ4?w=147&h=187&c=7&r=0&o=5&cb=thfvnext&dpr=1.5&pid=1.7", 4.1, "Sale", "T-Shirts", "Men"));
        productRepository.save(new Product("33", "Layered Charm Bracelet", 1210.0, "https://thfvnext.bing.com/th/id/OIP.eGo4DZm8VJ9-opWLsvcrawHaHa?w=184&h=184&c=7&r=0&o=7&cb=thfvnext&dpr=1.5&pid=1.7&rm=3", 4.2, "Best Seller", "Accessories", "Women"));
        productRepository.save(new Product("35", "Vintage Logo Tee", 980.0, "https://thfvnext.bing.com/th/id/OIP.gPXd1tEKgMiVEnizg9OpIwAAAA?cb=thfvnext&rs=1&pid=ImgDetMain&o=7&rm=3", 4.3, "New Arrivals", "T-Shirts", "Women"));
        productRepository.save(new Product("40", "Stacked Rings Set", 1620.0, "https://thfvnext.bing.com/th/id/OIP.4nP3YK9-BHhiOSu-GZLlpAHaHa?w=173&h=180&c=7&r=0&o=7&cb=thfvnext&dpr=1.5&pid=1.7&rm=3", 4.6, "New Arrivals", "Accessories", "Women"));
        productRepository.save(new Product("45", "Floral Print Shirt", 1160.0, "https://thfvnext.bing.com/th/id/OIP.rXg8uC1lSicGX58kc5MmFQHaJ4?cb=thfvnext&pid=ImgDet&w=178&h=237&c=7&dpr=1.5&o=7&rm=3", 4.6, "Deal of the Day", "Shirts", "Women"));
    }
}