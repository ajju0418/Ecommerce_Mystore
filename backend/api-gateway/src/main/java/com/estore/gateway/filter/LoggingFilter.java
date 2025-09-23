package com.estore.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = UUID.randomUUID().toString();
        
        // Log incoming request
        logger.info("=== INCOMING REQUEST [{}] ===", requestId);
        logger.info("Method: {}", request.getMethod());
        logger.info("URI: {}", request.getURI());
        logger.info("Path: {}", request.getPath());
        logger.info("Headers: {}", request.getHeaders());
        logger.info("Remote Address: {}", request.getRemoteAddress());
        logger.info("Timestamp: {}", LocalDateTime.now());
        
        // Add request ID to headers for tracking
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-Request-ID", requestId)
                .build();
        
        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(modifiedRequest)
                .build();

        return chain.filter(modifiedExchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            
            // Log outgoing response
            logger.info("=== OUTGOING RESPONSE [{}] ===", requestId);
            logger.info("Status Code: {}", response.getStatusCode());
            logger.info("Headers: {}", response.getHeaders());
            logger.info("Timestamp: {}", LocalDateTime.now());
            logger.info("=== END REQUEST [{}] ===", requestId);
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Execute before other filters
    }
}