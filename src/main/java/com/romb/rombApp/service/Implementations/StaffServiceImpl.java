package com.romb.rombApp.service.Implementations;

import com.romb.rombApp.dto.StaffDTO;
import com.romb.rombApp.dto.StaffLoginDTO;
import com.romb.rombApp.dto.StaffRegisterDTO;
import com.romb.rombApp.model.Staff;
import com.romb.rombApp.repository.StaffRepository;
import com.romb.rombApp.service.Interfaces.StaffService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    // Password encoder for hashing passwords
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private StaffDTO toDTO(Staff staff) {
        StaffDTO dto = new StaffDTO();
        dto.setId(staff.getId());
        dto.setFullName(staff.getFullName());
        dto.setUsername(staff.getUsername());
        dto.setRole(staff.getRole());
        return dto;
    }

    private Staff fromRegisterDTO(StaffRegisterDTO dto) {
        return Staff.builder()
                .fullName(dto.getFullName())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();
    }

    @Override
    public StaffDTO registerStaff(StaffRegisterDTO registerDTO) {
        // You might want to check if username exists here before saving
        Staff staff = fromRegisterDTO(registerDTO);
        Staff saved = staffRepository.save(staff);
        return toDTO(saved);
    }

    @Override
    public StaffDTO loginStaff(StaffLoginDTO loginDTO) {
        Staff staff = staffRepository.findAll().stream()
                .filter(s -> s.getUsername().equals(loginDTO.getUsername()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Username not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), staff.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return toDTO(staff);
    }

    @Override
    public List<StaffDTO> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StaffDTO getById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with ID: " + id));
        return toDTO(staff);
    }

    @Override
    public StaffDTO update(Long id, StaffRegisterDTO dto) {
        Staff existing = staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with ID: " + id));

        existing.setFullName(dto.getFullName());
        existing.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        existing.setRole(dto.getRole());

        Staff updated = staffRepository.save(existing);
        return toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!staffRepository.existsById(id)) {
            throw new EntityNotFoundException("Staff not found with ID: " + id);
        }
        staffRepository.deleteById(id);
    }
}
