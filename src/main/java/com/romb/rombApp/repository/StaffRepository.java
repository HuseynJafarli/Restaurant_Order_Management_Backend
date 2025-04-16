package com.romb.rombApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.romb.rombApp.model.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    
}