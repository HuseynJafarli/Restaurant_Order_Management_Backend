package com.romb.rombApp.dto;

import com.romb.rombApp.model.Role;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long expiresIn;
    private String username;
    private String fullName;
    private Role role;
}
