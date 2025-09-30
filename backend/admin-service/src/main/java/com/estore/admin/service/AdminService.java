package com.estore.admin.service;

import com.estore.admin.dto.AdminResponseDto;
import com.estore.admin.entity.Admin;
import com.estore.admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public void initializeDefaultAdmin() {
        if (!adminRepository.existsByUsername("admin")) {
            String encodedPassword = passwordEncoder.encode("admin123");
            Admin defaultAdmin = new Admin("admin", encodedPassword, "System Administrator");
            adminRepository.save(defaultAdmin);
        }
    }



    public AdminResponseDto getAdminProfile(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        return new AdminResponseDto(admin.getId(), admin.getUsername(), 
                                  admin.getFullName(), admin.getLastLogin());
    }
}