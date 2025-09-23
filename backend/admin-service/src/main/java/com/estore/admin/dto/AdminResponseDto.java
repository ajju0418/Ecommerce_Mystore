package com.estore.admin.dto;

import java.time.LocalDateTime;

public class AdminResponseDto {
    private Long id;
    private String username;
    private String fullName;
    private LocalDateTime lastLogin;

    public AdminResponseDto() {}

    public AdminResponseDto(Long id, String username, String fullName, LocalDateTime lastLogin) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.lastLogin = lastLogin;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
}