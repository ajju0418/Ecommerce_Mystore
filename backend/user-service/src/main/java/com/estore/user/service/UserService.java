package com.estore.user.service;

import com.estore.user.dto.UserLoginDto;
import com.estore.user.dto.UserRegistrationDto;
import com.estore.user.dto.UserResponseDto;
import com.estore.user.entity.User;
import com.estore.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserResponseDto registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User(
            registrationDto.getUsername(),
            registrationDto.getEmail(),
            registrationDto.getPhone(),
            registrationDto.getGender(),
            passwordEncoder.encode(registrationDto.getPassword())
        );
        
        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser.getId(), savedUser.getUsername(), 
                                 savedUser.getEmail(), savedUser.getPhone(), savedUser.getGender());
    }

    public UserResponseDto loginUser(UserLoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        return new UserResponseDto(user.getId(), user.getUsername(), 
                                 user.getEmail(), user.getPhone(), user.getGender());
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(user -> new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getGender()))
            .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        return new UserResponseDto(user.getId(), user.getUsername(), 
                                 user.getEmail(), user.getPhone(), user.getGender());
    }
}