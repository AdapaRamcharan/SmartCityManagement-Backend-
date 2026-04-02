package com.smartcity.backend.controller;

import com.smartcity.backend.dto.JwtResponse;
import com.smartcity.backend.dto.LoginRequest;
import com.smartcity.backend.dto.LoginRequestEmail;
import com.smartcity.backend.entity.User;
import com.smartcity.backend.repository.UserRepository;
import com.smartcity.backend.security.JwtUtils;
import com.smartcity.backend.service.SmartCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"}, allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmartCityService smartCityService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("{\"error\": \"Username is already taken!\"}");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("{\"error\": \"Email is already in use!\"}");
        }
        User savedUser = smartCityService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestEmail loginRequest) {
        try {
            // Find user by email
            User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequest.getEmail()));

            // Authenticate using username and password
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword())
            );

            // If authentication is successful, generate JWT token
            String jwt = jwtUtils.generateJwtToken(user.getUsername());
            
            // Log the login
            smartCityService.logLogin(user, "127.0.0.1");

            // Return JWT token and user details
            JwtResponse response = new JwtResponse(jwt, user.getUsername(), user.getRole());
            response.setEmail(user.getEmail());
            response.setId(user.getId());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login failed: " + e.getMessage());
        }
    }
}