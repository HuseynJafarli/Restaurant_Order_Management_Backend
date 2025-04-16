package com.romb.rombApp.service.Interfaces;

import com.romb.rombApp.model.Staff;

public interface StaffService {
    
    Staff registerStaff(Staff staff);
    
    Staff loginStaff(String staffname, String password);
}
