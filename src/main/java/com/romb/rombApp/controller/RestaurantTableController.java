package com.romb.rombApp.controller;

import com.romb.rombApp.dto.RestaurantTableDTO;
import com.romb.rombApp.dto.RestaurantTableGetDTO;
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
    public List<RestaurantTableGetDTO> getAllTables() {
        return tableService.getAll();
    }

    // @GetMapping("/{id}")
    // public RestaurantTableGetDTO getTableById(@PathVariable Long id) {
    //     return tableService.getById(id);
    // }

    @GetMapping("/{tableUrl}")
    public RestaurantTableGetDTO getTableByTableUrl(@PathVariable String tableUrl) {
        return tableService.getByTableUrl(tableUrl);
    }

    @PostMapping
    public RestaurantTableDTO createTable(@RequestBody RestaurantTableDTO tableDTO) {
        return tableService.create(tableDTO);
    }

    @PutMapping("/{id}")
    public RestaurantTableGetDTO updateTable(@PathVariable Long id, @RequestBody RestaurantTableGetDTO tableDTO) {
        return tableService.update(id, tableDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTable(@PathVariable Long id) {
        tableService.delete(id);
    }
}
