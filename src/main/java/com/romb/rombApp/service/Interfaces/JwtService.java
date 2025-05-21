package com.romb.rombApp.service.Interfaces;

public interface JwtService {

    String generateToken(String username);
    String extractUsername(String token);
    boolean isTokenValid(String token, String username);
    boolean isTokenExpired(String token);
    
}
