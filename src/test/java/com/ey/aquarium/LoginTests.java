package com.ey.aquarium;


import com.ey.aquarium.dto.JwtResponse;
import com.ey.aquarium.dto.LoginRequest;
import com.ey.aquarium.dto.RegisterRequest;
import com.ey.aquarium.model.User;
import com.ey.aquarium.model.User.Role;
import com.ey.aquarium.repository.UserRepository;
import com.ey.aquarium.security.JwtUtil;
import com.ey.aquarium.security.UserDetailsServiceImpl;
import com.ey.aquarium.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthService authService;

    private User user;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("noel");
        user.setEmail("noel@test.com");
        user.setPassword("encodedPass");

        user.setActive(true);

        loginRequest = new LoginRequest();
        loginRequest.setUsername("noel");
        loginRequest.setPassword("password123");

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("noel");
        registerRequest.setEmail("noel@test.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("Noel");
        registerRequest.setLastName("Muppasani");


        userDetails = org.springframework.security.core.userdetails.User
                .withUsername("noel")
                .password("encodedPass")
                .authorities("ROLE_USER")
                .build();
    }

 

   

    @Test
    void testLoginUserNotFound() {
        when(userDetailsService.loadUserByUsername("noel")).thenReturn(userDetails);
        when(userRepository.findByUsername("noel")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest));

        assertEquals("User not found", ex.getMessage());
    }



   
    @Test
    void testRegisterUsernameAlreadyExists() {
        when(userRepository.existsByUsername("noel")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.register(registerRequest));

        assertEquals("Username is already taken!", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegisterEmailAlreadyExists() {
        when(userRepository.existsByUsername("noel")).thenReturn(false);
        when(userRepository.existsByEmail("noel@test.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.register(registerRequest));

        assertEquals("Email is already in use!", ex.getMessage());
        verify(userRepository, never()).save(any());
    }
}



