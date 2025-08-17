package com.epam.shiftsync.service;

import com.epam.shiftsync.dto.auth.RegisterRequest;
import com.epam.shiftsync.entity.User;
import com.epam.shiftsync.repository.UserRepository;
import com.epam.shiftsync.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock 
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks 
    private AuthService authService;

    @Test
    void register_shouldSaveUserWithHashedPasswordAndEmployeeRole() {
        RegisterRequest request = new RegisterRequest("Test", "User", "test@example.com", "password123");
        String hashedPassword = "hashed_password_string";
        
        when(passwordEncoder.encode("password123")).thenReturn(hashedPassword);
        
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        authService.register(request);

        
        verify(userRepository, times(1)).save(argThat(user -> {
            assertEquals("Test", user.getFirstName());
            assertEquals("test@example.com", user.getEmail());
            assertEquals("EMPLOYEE", user.getRole());
            assertEquals(hashedPassword, user.getPasswordHash()); 
            return true;
        }));
    }
}