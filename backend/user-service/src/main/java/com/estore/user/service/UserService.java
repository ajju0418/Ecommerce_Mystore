package com.estore.user.service;

import com.estore.user.dto.UserLoginDto;
import com.estore.user.dto.UserRegistrationDto;
import com.estore.user.dto.UserResponseDto;
import com.estore.user.dto.ResetPasswordDto;
import com.estore.user.entity.User;
import com.estore.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.estore.user.security.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtUtil jwtUtil;

    public UserResponseDto registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        String role = registrationDto.getRole();
        if (role == null || role.isEmpty()) {
            if ("admin".equalsIgnoreCase(registrationDto.getUsername())) {
                role = "ADMIN";
            } else {
                role = "USER";
            }
        }
        User user = new User(
            registrationDto.getUsername(),
            registrationDto.getEmail(),
            registrationDto.getPhone(),
            registrationDto.getGender(),
            passwordEncoder.encode(registrationDto.getPassword()),
            role
        );
        
        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser.getId(), savedUser.getUsername(), 
                                 savedUser.getEmail(), savedUser.getPhone(), savedUser.getGender(), savedUser.getCreatedAt());
    }

    public UserResponseDto loginUser(UserLoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        String role = "USER";
        if ("admin".equalsIgnoreCase(user.getUsername())) {
            role = "ADMIN";
        }
        String token = jwtUtil.generateToken(user.getUsername(), role);

        UserResponseDto dto = new UserResponseDto(user.getId(), user.getUsername(), 
                                 user.getEmail(), user.getPhone(), user.getGender(), user.getCreatedAt());
        dto.setToken(token);
        return dto;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findByRoleNot("ADMIN").stream()
            .map(user -> new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getGender(), user.getCreatedAt()))
            .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        return new UserResponseDto(user.getId(), user.getUsername(), 
                                 user.getEmail(), user.getPhone(), user.getGender(), user.getCreatedAt());
    }

    public void deleteUser(Long userId) {
        System.out.println("deleteUser called for ID: " + userId);
        if (!userRepository.existsById(userId)) {
            System.err.println("User not found with ID: " + userId);
            throw new RuntimeException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
        System.out.println("User deleted from repository: " + userId);
    }

    public UserResponseDto updateUser(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setGender(updatedUser.getGender());
        // Optionally update password if provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getPhone(), savedUser.getGender(), savedUser.getCreatedAt());
    }

    public void resetPassword(ResetPasswordDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or phone"));
        if (user.getPhone() == null || !user.getPhone().equals(dto.getPhone())) {
            throw new RuntimeException("Invalid username or phone");
        }
        if (dto.getNewPassword() == null || dto.getNewPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}