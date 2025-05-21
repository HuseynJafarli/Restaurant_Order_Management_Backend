package com.romb.rombApp.controller;

import com.romb.rombApp.dto.StaffDTO;
import com.romb.rombApp.dto.StaffLoginDTO;
import com.romb.rombApp.dto.StaffRegisterDTO;
import com.romb.rombApp.service.Interfaces.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "http://localhost:3000")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @PostMapping("/register")
    public StaffDTO register(@RequestBody StaffRegisterDTO dto) {
        return staffService.registerStaff(dto);
    }

    @PostMapping("/login")
    public StaffDTO login(@RequestBody StaffLoginDTO dto) {
        return staffService.loginStaff(dto);
    }

    @GetMapping
    public List<StaffDTO> getAllStaff() {
        return staffService.getAllStaff();
    }

    @GetMapping("/{id}")
    public StaffDTO getStaffById(@PathVariable Long id) {
        return staffService.getById(id);
    }

    @PutMapping("/{id}")
    public StaffDTO updateStaff(@PathVariable Long id, @RequestBody StaffRegisterDTO dto) {
        return staffService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteStaff(@PathVariable Long id) {
        staffService.delete(id);
    }
}
