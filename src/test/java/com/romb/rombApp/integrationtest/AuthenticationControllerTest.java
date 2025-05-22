package com.romb.rombApp.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romb.rombApp.dto.AuthRequest;
import com.romb.rombApp.dto.RegisterRequest;
import com.romb.rombApp.model.Role;
import com.romb.rombApp.model.Staff;
import com.romb.rombApp.service.AuthenticationService;
import com.romb.rombApp.service.JwtService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService;

    @Test
    void register_ShouldReturnRegisteredStaff() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Alice");
        request.setUsername("alice123");
        request.setPassword("secret");
        request.setRole(Role.KITCHEN);

        Staff mockStaff = Staff.builder()
                .id(1L)
                .fullName("Alice")
                .username("alice123")
                .role(Role.KITCHEN)
                .build();

        when(authenticationService.signup(any(RegisterRequest.class))).thenReturn(mockStaff);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice123"))
                .andExpect(jsonPath("$.role").value("KITCHEN"));
    }

    @Test
    void login_ShouldReturnAuthToken() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("bob");
        request.setPassword("pass");

        Staff mockStaff = Staff.builder()
                .id(2L)
                .fullName("Bob Builder")
                .username("bob")
                .role(Role.MANAGER)
                .build();

        when(authenticationService.authenticate(any(AuthRequest.class))).thenReturn(mockStaff);
        when(jwtService.generateToken(mockStaff)).thenReturn("fake-jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(7200L);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.expiresIn").value(7200))
                .andExpect(jsonPath("$.username").value("bob"))
                .andExpect(jsonPath("$.role").value("MANAGER"));
    }

    @TestConfiguration
    static class MockConfig {

        @Bean
        @Primary
        public AuthenticationService mockAuthService() {
            return mock(AuthenticationService.class);
        }

        @Bean
        @Primary
        public JwtService mockJwtService() {
            return mock(JwtService.class);
        }
    }
}