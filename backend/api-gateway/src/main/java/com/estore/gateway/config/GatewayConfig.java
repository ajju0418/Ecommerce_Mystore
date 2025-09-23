package com.estore.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .uri("lb://user-service"))
                
                // Product Service Routes
                .route("product-service", r -> r
                        .path("/api/products/**")
                        .uri("lb://product-service"))
                
                // Cart Service Routes
                .route("cart-service", r -> r
                        .path("/api/cart/**")
                        .uri("lb://cart-service"))
                
                // Order Service Routes
                .route("order-service", r -> r
                        .path("/api/orders/**")
                        .uri("lb://order-service"))
                
                // Payment Service Routes
                .route("payment-service", r -> r
                        .path("/api/payments/**")
                        .uri("lb://payment-service"))
                
                // Admin Service Routes
                .route("admin-service", r -> r
                        .path("/api/admin/**")
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

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}