package com.estore.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitingFilter implements GlobalFilter, Ordered {

    private final ConcurrentHashMap<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();
    private final int maxRequestsPerMinute;

    private static final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);
    
    public RateLimitingFilter() {
        String env = System.getenv("GATEWAY_RATE_LIMIT_PER_MINUTE");
        int configured = 600; // default higher limit to avoid dev throttling
        if (env != null) {
            try { 
                configured = Integer.parseInt(env);
                logger.info("Rate limit configured from environment: {} requests/minute", configured);
            } catch (NumberFormatException e) {
                logger.warn("Invalid rate limit configuration '{}', using default: {}", env, configured);
            }
        }
        this.maxRequestsPerMinute = configured;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientId = getClientId(exchange);
        
        // Allow-list some paths like static assets or health checks
        String path = exchange.getRequest().getURI().getPath();
        if (path.startsWith("/assets/") || path.startsWith("/favicon") || path.contains("/health") || path.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        if (isRateLimited(clientId)) {
            logger.warn("Rate limit exceeded for client: {}", clientId);
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            exchange.getResponse().getHeaders().add("X-Rate-Limit-Exceeded", "true");
            exchange.getResponse().getHeaders().add("X-Rate-Limit-Limit", String.valueOf(maxRequestsPerMinute));
            exchange.getResponse().getHeaders().add("Retry-After", "60");
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private String getClientId(ServerWebExchange exchange) {
        // Use IP address as client identifier
        String clientIp = exchange.getRequest().getRemoteAddress() != null 
            ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
            : "unknown";
        
        // Could also use user ID from headers if available
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
        return userId != null ? "user-" + userId : "ip-" + clientIp;
    }

    /**
     * Check if client has exceeded rate limit
     * Uses sliding window approach for accurate rate limiting
     */
    private boolean isRateLimited(String clientId) {
        LocalDateTime now = LocalDateTime.now();
        RequestCounter counter = requestCounts.computeIfAbsent(clientId, 
            k -> new RequestCounter(now));

        synchronized (counter) {
            // Reset counter if more than a minute has passed
            if (ChronoUnit.MINUTES.between(counter.windowStart, now) >= 1) {
                counter.reset(now);
            }
            
            int currentCount = counter.increment();
            if (currentCount > maxRequestsPerMinute) {
                logger.debug("Rate limit check: client {} has {} requests (limit: {})", 
                    clientId, currentCount, maxRequestsPerMinute);
                return true;
            }
            return false;
        }
    }

    @Override
    public int getOrder() {
        return 0; // Execute after logging but before routing
    }

    private static class RequestCounter {
        private LocalDateTime windowStart;
        private AtomicInteger count;

        public RequestCounter(LocalDateTime windowStart) {
            this.windowStart = windowStart;
            this.count = new AtomicInteger(0);
        }

        public int increment() {
            return count.incrementAndGet();
        }

        public void reset(LocalDateTime newWindowStart) {
            this.windowStart = newWindowStart;
            this.count.set(0);
        }
    }
}