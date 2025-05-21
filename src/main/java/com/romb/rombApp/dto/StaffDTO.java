package com.romb.rombApp.dto;

import com.romb.rombApp.model.Role;
import lombok.Data;

@Data
public class StaffDTO {
    private Long id;
    private String fullName;
    private String username;
    private Role role;
}
