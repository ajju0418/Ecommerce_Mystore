package com.estore.gateway.filter;

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

    public RateLimitingFilter() {
        String env = System.getenv("GATEWAY_RATE_LIMIT_PER_MINUTE");
        int configured = 600; // default higher limit to avoid dev throttling
        if (env != null) {
            try { configured = Integer.parseInt(env); } catch (NumberFormatException ignored) {}
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
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            exchange.getResponse().getHeaders().add("X-Rate-Limit-Exceeded", "true");
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

    private boolean isRateLimited(String clientId) {
        LocalDateTime now = LocalDateTime.now();
        RequestCounter counter = requestCounts.computeIfAbsent(clientId, 
            k -> new RequestCounter(now));

        // Reset counter if more than a minute has passed
        if (ChronoUnit.MINUTES.between(counter.windowStart, now) >= 1) {
            counter.reset(now);
        }

        return counter.increment() > maxRequestsPerMinute;
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