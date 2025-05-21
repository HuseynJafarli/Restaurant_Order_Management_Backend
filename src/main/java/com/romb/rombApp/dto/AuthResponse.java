package com.romb.rombApp.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long expiresIn;

}
