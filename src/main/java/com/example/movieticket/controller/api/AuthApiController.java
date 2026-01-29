package com.example.movieticket.controller.api;

import com.example.movieticket.dto.request.LoginRequest;
import com.example.movieticket.dto.request.RegisterRequest;
import com.example.movieticket.dto.response.ApiResponse;
import com.example.movieticket.dto.response.AuthResponse;
import com.example.movieticket.entity.User;
import com.example.movieticket.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication APIs")
public class AuthApiController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate a user and return user details")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        List<User> users = userRepository.findByUsername(loginRequest.getUsername());

        if (users.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid username or password"));
        }

        User user = users.get(0);
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid username or password"));
        }

        AuthResponse authResponse = AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .userType(user.getUserType())
                .email(user.getEmail())
                .build();

        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user account")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        // Check if username already exists
        if (!userRepository.findByUsername(registerRequest.getUsername()).isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Username already exists"));
        }

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setUserType("REGULAR");

        User savedUser = userRepository.save(user);

        AuthResponse authResponse = AuthResponse.builder()
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .userType(savedUser.getUserType())
                .email(savedUser.getEmail())
                .build();

        return ResponseEntity.ok(ApiResponse.success("Registration successful", authResponse));
    }

    @GetMapping("/verify-username")
    @Operation(summary = "Verify username availability", description = "Check if a username is available for registration")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> verifyUsername(@RequestParam String username) {
        if (username == null || username.length() < 5) {
            return ResponseEntity.ok(ApiResponse.success(Map.of("available", false)));
        }

        boolean isAvailable = userRepository.findByUsername(username).isEmpty();
        return ResponseEntity.ok(ApiResponse.success(Map.of("available", isAvailable)));
    }
}
