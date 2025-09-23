package com.estore.user.service;

import com.estore.user.entity.User;
import com.estore.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataInitializationService implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Sample users from hardcoded data
            userRepository.save(new User("kishan123", "kishankr1122@gmail.com", "1234567890", "male", passwordEncoder.encode("kishan123")));
            userRepository.save(new User("bhargav", "bhargav@gmail.com", "9876543210", "male", passwordEncoder.encode("bhargav")));
            userRepository.save(new User("sneha99", "sneha@example.com", "9988776655", "female", passwordEncoder.encode("sneha99")));
            
            System.out.println("Sample users initialized");
        }
    }
}