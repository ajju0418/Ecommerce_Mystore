package com.estore.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Gateway routing configuration
 * Defines routes to microservices and CORS settings
 */
@Configuration
public class GatewayConfig {

    private static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);

    /**
     * Configure custom routes for microservices
     * Uses load balancer for service discovery
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        logger.info("Configuring gateway routes for microservices");
        
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f.circuitBreaker(config -> config
                            .setName("user-service-cb")
                            .setFallbackUri("forward:/fallback/user-service")))
                        .uri("lb://user-service"))
                
                // Product Service Routes
                .route("product-service", r -> r
                        .path("/api/products/**")
                        .filters(f -> f.circuitBreaker(config -> config
                            .setName("product-service-cb")
                            .setFallbackUri("forward:/fallback/product-service")))
                        .uri("lb://product-service"))
                
                // Cart Service Routes
                .route("cart-service", r -> r
                        .path("/api/cart/**")
                        .filters(f -> f.circuitBreaker(config -> config
                            .setName("cart-service-cb")
                            .setFallbackUri("forward:/fallback/cart-service")))
                        .uri("lb://cart-service"))
                
                // Order Service Routes
                .route("order-service", r -> r
                        .path("/api/orders/**")
                        .filters(f -> f.circuitBreaker(config -> config
                            .setName("order-service-cb")
                            .setFallbackUri("forward:/fallback/order-service")))
                        .uri("lb://order-service"))
                
                // Payment Service Routes
                .route("payment-service", r -> r
                        .path("/api/payments/**")
                        .uri("lb://payment-service"))
                
                // Admin Service Routes
                .route("admin-service", r -> r
                        .path("/api/admin/**")
                        .filters(f -> f.circuitBreaker(config -> config
                            .setName("admin-service-cb")
                            .setFallbackUri("forward:/fallback/admin-service")))
                        .uri("lb://admin-service"))
                
                // Health Check Routes for all services
                .route("user-health", r -> r
                        .path("/health/user-service")
                        .uri("lb://user-service/actuator/health"))
                
                .route("product-health", r -> r
                        .path("/health/product-service")
                        .uri("lb://product-service/actuator/health"))
                
                .route("cart-health", r -> r
                        .path("/health/cart-service")
                        .uri("lb://cart-service/actuator/health"))
                
                .route("order-health", r -> r
                        .path("/health/order-service")
                        .uri("lb://order-service/actuator/health"))
                
                .route("payment-health", r -> r
                        .path("/health/payment-service")
                        .uri("lb://payment-service/actuator/health"))
                
                .route("admin-health", r -> r
                        .path("/health/admin-service")
                        .uri("lb://admin-service/actuator/health"))
                
                .build();
    }

    /**
     * Configure CORS filter for cross-origin requests
     * Allows frontend applications to communicate with gateway
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        logger.info("Configuring CORS filter for gateway");
        
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "http://localhost:3000"
        ));
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        corsConfig.setAllowedHeaders(Arrays.asList(
            "Authorization", "Content-Type", "X-Requested-With", "X-Request-ID"
        ));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}