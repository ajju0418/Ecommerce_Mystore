package com.estore.admin.controller;

import com.estore.admin.dto.AdminLoginDto;
import com.estore.admin.dto.AdminResponseDto;
import com.estore.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {
    
    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<AdminResponseDto> login(@RequestBody AdminLoginDto loginDto) {
        try {
            AdminResponseDto response = adminService.login(loginDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<AdminResponseDto> getProfile(@PathVariable String username) {
        try {
            AdminResponseDto response = adminService.getAdminProfile(username);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Admin Auth Service is running");
    }
}