package com.ey.aquarium.controller;


import com.ey.aquarium.dto.JwtResponse;
import com.ey.aquarium.dto.LoginRequest;
import com.ey.aquarium.dto.RegisterRequest;
import com.ey.aquarium.model.User;
import com.ey.aquarium.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	 @Autowired
	    private AuthService authService;
	    
	    @PostMapping("/login")
	    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
	        try {
	            JwtResponse jwtResponse = authService.login(loginRequest);
	            return ResponseEntity.ok(jwtResponse);
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
	        }
	    }
	    
	    @PostMapping("/register")
	    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
	        try {
	            User user = authService.register(registerRequest);
	            return ResponseEntity.ok("User registered successfully: " + user.getUsername());
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
	        }
	    }
	}


