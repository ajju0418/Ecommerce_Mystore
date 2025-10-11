package com.estore.payment.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("[JWT Filter] Processing request: " + request.getRequestURI());
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("[JWT Filter] Authorization header: " + (authHeader != null ? "Present" : "Missing"));
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("[JWT Filter] Token extracted, length: " + token.length());
            try {
                String username = jwtUtil.validateAndGetUsername(token);
                String role = jwtUtil.getRole(token);
                System.out.println("[JWT Filter] Token valid - Username: " + username + ", Role: " + role);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + (role != null ? role : "USER")))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("[JWT Filter] Authentication set successfully");
            } catch (Exception e) {
                System.err.println("[JWT Filter] Token validation failed: " + e.getMessage());
                // Invalid token -> proceed without authentication
            }
        } else {
            System.out.println("[JWT Filter] No valid Authorization header found");
        }
        filterChain.doFilter(request, response);
    }
}