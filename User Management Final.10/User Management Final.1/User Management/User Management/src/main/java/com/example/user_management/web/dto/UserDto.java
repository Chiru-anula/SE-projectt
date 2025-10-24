package com.example.user_management.web.dto;

import com.example.user_management.domain.AppUser;
import com.example.user_management.domain.Role;

public record UserDto(Long id, String name, String email, String phone, Role role) {
    public static UserDto from(AppUser u) {
        return new UserDto(u.getId(), u.getName(), u.getEmail(), u.getPhone(), u.getRole());
    }
}
