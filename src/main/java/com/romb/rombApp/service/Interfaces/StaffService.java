package com.romb.rombApp.service.Interfaces;

import com.romb.rombApp.dto.StaffDTO;
import com.romb.rombApp.dto.StaffLoginDTO;
import com.romb.rombApp.dto.StaffRegisterDTO;

import java.util.List;

public interface StaffService {

    StaffDTO registerStaff(StaffRegisterDTO registerDTO);

    StaffDTO loginStaff(StaffLoginDTO loginDTO);

    List<StaffDTO> getAllStaff();

    StaffDTO getById(Long id);

    StaffDTO update(Long id, StaffRegisterDTO dto);

    void delete(Long id);
}
