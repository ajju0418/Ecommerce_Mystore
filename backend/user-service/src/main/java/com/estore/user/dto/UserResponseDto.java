package com.estore.user.dto;

import java.time.LocalDateTime;

public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String gender;
    private String token;
    private LocalDateTime createdAt;

    public UserResponseDto() {}

    public UserResponseDto(Long id, String username, String email, String phone, String gender, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}