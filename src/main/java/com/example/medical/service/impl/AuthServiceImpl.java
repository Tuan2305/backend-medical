package com.example.medical.service.impl;

import com.example.medical.dto.response.AuthResponse;
import com.example.medical.dto.response.UserInfoDTO;
import com.example.medical.entity.User;
import com.example.medical.mapper.UserMapper;
import com.example.medical.repository.UserRepository;
import com.example.medical.service.AuthService;
import com.example.medical.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    public AuthResponse authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        UserInfoDTO userInfo = userMapper.toUserInfoDTO(user);

        return new AuthResponse(true, token, "Authentication successful", userInfo);
    }
}