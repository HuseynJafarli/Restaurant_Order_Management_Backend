package com.romb.rombApp.controller;

import com.romb.rombApp.dto.RestaurantTableDTO;
import com.romb.rombApp.service.Interfaces.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin(origins = "http://localhost:3000")
public class RestaurantTableController {

    @Autowired
    private RestaurantTableService tableService;

    @GetMapping
    public List<RestaurantTableDTO> getAllTables() {
        return tableService.getAll();
    }

    @GetMapping("/{id}")
    public RestaurantTableDTO getTableById(@PathVariable Long id) {
        return tableService.getById(id);
    }

    @PostMapping
    public RestaurantTableDTO createTable(@RequestBody RestaurantTableDTO tableDTO) {
        return tableService.create(tableDTO);
    }

    @PutMapping("/{id}")
    public RestaurantTableDTO updateTable(@PathVariable Long id, @RequestBody RestaurantTableDTO tableDTO) {
        return tableService.update(id, tableDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTable(@PathVariable Long id) {
        tableService.delete(id);
    }
}
