package com.romb.rombApp.service.Implementations;

import com.romb.rombApp.dto.RestaurantTableDTO;
import com.romb.rombApp.model.RestaurantTable;
import com.romb.rombApp.repository.RestaurantTableRepository;
import com.romb.rombApp.service.Interfaces.RestaurantTableService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    @Autowired
    private RestaurantTableRepository repository;

    // Helper mapper methods
    private RestaurantTableDTO toDTO(RestaurantTable table) {
        RestaurantTableDTO dto = new RestaurantTableDTO();
        dto.setId(table.getId());
        return dto;
    }

    private RestaurantTable fromDTO(RestaurantTableDTO dto) {
        RestaurantTable table = new RestaurantTable();
        table.setId(dto.getId());
        return table;
    }

    @Override
    public List<RestaurantTableDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantTableDTO getById(Long id) {
        RestaurantTable table = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Table not found with ID: " + id));
        return toDTO(table);
    }

    @Override
    public RestaurantTableDTO create(RestaurantTableDTO dto) {
        RestaurantTable table = fromDTO(dto);
        RestaurantTable saved = repository.save(table);
        return toDTO(saved);
    }

    @Override
    public RestaurantTableDTO update(Long id, RestaurantTableDTO dto) {
        RestaurantTable existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Table not found with ID: " + id));
        // No additional fields to update, just save existing entity for now
        RestaurantTable updated = repository.save(existing);
        return toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Table not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
