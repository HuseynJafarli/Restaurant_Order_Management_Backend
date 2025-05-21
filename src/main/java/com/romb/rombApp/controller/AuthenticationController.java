package com.romb.rombApp.controller;


import com.romb.rombApp.dto.AuthRequest;
import com.romb.rombApp.dto.AuthResponse;
import com.romb.rombApp.dto.RegisterRequest;
import com.romb.rombApp.model.Staff;
import com.romb.rombApp.service.AuthenticationService;
import com.romb.rombApp.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000") 
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<Staff> register(@RequestBody RegisterRequest registerRequest) {
        Staff registeredStaff = authenticationService.signup(registerRequest);
        return ResponseEntity.ok(registeredStaff);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        Staff staff = authenticationService.authenticate(authRequest);

        String jwtToken = jwtService.generateToken(staff);
        AuthResponse response = new AuthResponse();
        response.setToken(jwtToken);
        response.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(response);
    }
}
