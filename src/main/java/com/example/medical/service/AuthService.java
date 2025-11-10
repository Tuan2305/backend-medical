package com.example.medical.service;

import com.example.medical.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(String username, String password);
}