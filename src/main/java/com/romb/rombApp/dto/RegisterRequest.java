package com.romb.rombApp.dto;

import com.romb.rombApp.model.Role;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;

    private String password;

    private String fullName;
    private Role role;
}
