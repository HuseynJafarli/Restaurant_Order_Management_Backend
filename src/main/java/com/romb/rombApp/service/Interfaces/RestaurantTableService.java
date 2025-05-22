package com.romb.rombApp.service.Interfaces;

import com.romb.rombApp.dto.RestaurantTableDTO;
import com.romb.rombApp.dto.RestaurantTableGetDTO;

import java.util.List;

public interface RestaurantTableService {
    List<RestaurantTableGetDTO> getAll();
    RestaurantTableGetDTO getById(Long id);
    RestaurantTableGetDTO getByTableUrl(String tableUrl);
    RestaurantTableDTO create(RestaurantTableDTO dto);
    RestaurantTableGetDTO update(Long id, RestaurantTableGetDTO dto);
    void delete(Long id);
}
