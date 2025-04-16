package com.romb.rombApp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.romb.rombApp.model.RestaurantTable;

import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    Optional<RestaurantTable> findByTableId(Long tableId);
}
