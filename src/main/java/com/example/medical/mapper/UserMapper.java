package com.example.medical.mapper;

import com.example.medical.dto.response.UserInfoDTO;
import com.example.medical.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserInfoDTO toUserInfoDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserInfoDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().toString()
        );
    }

    // Có thể thêm các method khác như toUserDTO, etc.
}