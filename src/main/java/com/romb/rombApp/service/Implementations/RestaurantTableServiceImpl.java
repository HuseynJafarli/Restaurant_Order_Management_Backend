package com.romb.rombApp.service.Implementations;

import com.romb.rombApp.dto.RestaurantTableDTO;
import com.romb.rombApp.dto.RestaurantTableGetDTO;
import com.romb.rombApp.exception.ResourceNotFoundException;
import com.romb.rombApp.model.RestaurantTable;
import com.romb.rombApp.repository.RestaurantTableRepository;
import com.romb.rombApp.service.Interfaces.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    @Autowired
    private RestaurantTableRepository repository;

    private RestaurantTableDTO toDTO(RestaurantTable table) {
        RestaurantTableDTO dto = new RestaurantTableDTO();
        dto.setTableUrl(table.getTableUrl());
        return dto;
    }

    private RestaurantTable fromDTO(RestaurantTableDTO dto) {
        RestaurantTable table = new RestaurantTable();
        table.setTableUrl(dto.getTableUrl());
        return table;
    }

    private RestaurantTableGetDTO toResponseDTO(RestaurantTable table) {
        RestaurantTableGetDTO dto = new RestaurantTableGetDTO();
        dto.setTableUrl(table.getTableUrl());
        dto.setId(table.getId());
        return dto;
    }


    

    @Override
    public List<RestaurantTableGetDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantTableGetDTO getById(Long id) {
        RestaurantTable table = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with ID: " + id));
        return toResponseDTO(table);
    }

    @Override
    public RestaurantTableGetDTO getByTableUrl(String tableUrl) {
        RestaurantTable table = repository.findByTableUrl(tableUrl)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with url: " + tableUrl));
        return toResponseDTO(table);
    }



    @Override
    public RestaurantTableDTO create(RestaurantTableDTO dto) {
        RestaurantTable table = fromDTO(dto);
        RestaurantTable saved = repository.save(table);
        return toDTO(saved);
    }

    @Override
    public RestaurantTableGetDTO update(Long id, RestaurantTableGetDTO dto) {
        RestaurantTable existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with ID: " + id));
        // No additional fields to update, just save existing entity for now
        RestaurantTable updated = repository.save(existing);
        return toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Table not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
