package com.romb.rombApp.service.Interfaces;

import com.romb.rombApp.dto.RestaurantTableDTO;
import com.romb.rombApp.model.RestaurantTable;

import java.util.List;

public interface RestaurantTableService {
    List<RestaurantTableDTO> getAll();
    RestaurantTableDTO getById(Long id);
    RestaurantTableDTO create(RestaurantTableDTO dto);
    RestaurantTableDTO update(Long id, RestaurantTableDTO dto);
    void delete(Long id);
}
