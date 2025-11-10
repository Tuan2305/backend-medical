package com.example.medical.controller;

import com.example.medical.dto.request.LoginRequest;
import com.example.medical.dto.response.ApiResponse;
import com.example.medical.dto.response.AuthResponse;
import com.example.medical.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Authentication failed: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(ApiResponse.success("Logout successful", "User logged out"));
    }
}