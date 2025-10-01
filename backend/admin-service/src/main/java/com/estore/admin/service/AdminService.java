package com.estore.admin.service;

import com.estore.admin.dto.AdminResponseDto;
import com.estore.admin.entity.Admin;
import com.estore.admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;





    public AdminResponseDto getAdminProfile(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        return new AdminResponseDto(admin.getId(), admin.getUsername(), 
                                  admin.getFullName(), admin.getLastLogin());
    }
}