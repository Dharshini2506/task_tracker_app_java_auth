package com.tasktracker.backend.security;


import com.tasktracker.backend.model.entity.Users;
import com.tasktracker.backend.repository.UserRepo;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserRepo userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Users user) {
        System.out.println("=== REGISTER DEBUG ===");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Password (raw): " + user.getPassword());
        
        if (userRepository.existsByEmail(user.getEmail())) {
            System.out.println("Email already exists!");
            return ResponseEntity.badRequest().body("Email already in use");
        }
        
        try {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            System.out.println("Encoded password: " + encodedPassword);
            
            Users savedUser = userRepository.save(user);
            System.out.println("Saved user ID: " + savedUser.getId());
            System.out.println("Saved user email: " + savedUser.getEmail());
            
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Registration failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        
        Users user = userRepository.findByEmail(email)
            .orElse(null);
        
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        
        String token = jwtUtil.generateToken(user.getId());
        
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("email", user.getEmail());
        
        return ResponseEntity.ok(response);
    }
}