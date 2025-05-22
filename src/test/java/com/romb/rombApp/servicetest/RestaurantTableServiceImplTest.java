package com.romb.rombApp.servicetest;
import com.romb.rombApp.dto.RestaurantTableDTO;
import com.romb.rombApp.dto.RestaurantTableGetDTO;
import com.romb.rombApp.exception.ResourceNotFoundException;
import com.romb.rombApp.model.RestaurantTable;
import com.romb.rombApp.repository.RestaurantTableRepository;
import com.romb.rombApp.service.Implementations.RestaurantTableServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantTableServiceImplTest {

    @Mock
    private RestaurantTableRepository repository;

    @InjectMocks
    private RestaurantTableServiceImpl service;

    private RestaurantTable table;
    private RestaurantTableDTO tableDTO;
    private RestaurantTableGetDTO tableGetDTO;

    @BeforeEach
    void setup() {
        table = RestaurantTable.builder()
                .id(1L)
                .tableUrl("table-101")
                .build();

        tableDTO = new RestaurantTableDTO();
        tableGetDTO = new RestaurantTableGetDTO();
        tableDTO.setTableUrl("table-101");
    }

    @Test
    void getAll_ShouldReturnAllTables() {
        when(repository.findAll()).thenReturn(List.of(table));

        List<RestaurantTableGetDTO> result = service.getAll();

        assertEquals(1, result.size());
        assertEquals(table.getId(), result.get(0).getId());
        assertEquals(table.getTableUrl(), result.get(0).getTableUrl());
    }

    @Test
    void getById_ValidId_ShouldReturnTable() {
        when(repository.findById(1L)).thenReturn(Optional.of(table));

        RestaurantTableGetDTO result = service.getById(1L);

        assertEquals(table.getId(), result.getId());
        assertEquals(table.getTableUrl(), result.getTableUrl());
    }

    @Test
    void getById_InvalidId_ShouldThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void getByTableUrl_ValidUrl_ShouldReturnTable() {
        when(repository.findByTableUrl("table-101")).thenReturn(Optional.of(table));

        RestaurantTableGetDTO result = service.getByTableUrl("table-101");

        assertEquals(table.getId(), result.getId());
        assertEquals(table.getTableUrl(), result.getTableUrl());
    }

    @Test
    void getByTableUrl_InvalidUrl_ShouldThrowException() {
        when(repository.findByTableUrl("invalid-url")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getByTableUrl("invalid-url"));
    }

    @Test
    void create_ShouldSaveAndReturnDTO() {
        when(repository.save(any(RestaurantTable.class))).thenReturn(table);

        RestaurantTableDTO result = service.create(tableDTO);

        assertEquals(table.getTableUrl(), result.getTableUrl());
    }

    @Test
    void update_ValidId_ShouldUpdateAndReturnDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(table));
        when(repository.save(any(RestaurantTable.class))).thenReturn(table);

        RestaurantTableGetDTO result = service.update(1L, tableGetDTO);

        assertEquals(table.getTableUrl(), result.getTableUrl());
    }

    @Test
    void update_InvalidId_ShouldThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(99L, tableGetDTO));
    }

    @Test
    void delete_ValidId_ShouldCallRepository() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void delete_InvalidId_ShouldThrowException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
    }
}