package com.romb.rombApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.romb.rombApp.model.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUsername(String username);
}