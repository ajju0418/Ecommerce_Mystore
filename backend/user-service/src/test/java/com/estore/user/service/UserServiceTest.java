package com.estore.user.service;

import com.estore.user.dto.UserResponseDto;
import com.estore.user.entity.User;
import com.estore.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_ShouldReturnUserResponseDtoList() {
        // Given
        User user1 = new User("john_doe", "john@example.com", "1234567890", "Male", "encodedPassword", "USER");
        user1.setId(1L);
        User user2 = new User("jane_doe", "jane@example.com", "0987654321", "Female", "encodedPassword", "USER");
        user2.setId(2L);
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findByRoleNot("ADMIN")).thenReturn(users);

        // When
        List<UserResponseDto> result = userService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals("john_doe", result.get(0).getUsername());
        verify(userRepository, times(1)).findByRoleNot("ADMIN");
    }

    @Test
    void getUserById_WhenExists_ShouldReturnUserResponseDto() {
        // Given
        User user = new User("john_doe", "john@example.com", "1234567890", "Male", "encodedPassword", "USER");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        UserResponseDto result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> userService.getUserById(999L));
        
        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void deleteUser_WhenExists_ShouldDeleteUser() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}