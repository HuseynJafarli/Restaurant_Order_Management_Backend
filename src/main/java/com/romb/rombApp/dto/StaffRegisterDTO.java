package com.romb.rombApp.dto;

import com.romb.rombApp.model.Role;
import lombok.Data;

@Data
public class StaffRegisterDTO {
    private String fullName;
    private String username;
    private String password;
    private Role role;
}
