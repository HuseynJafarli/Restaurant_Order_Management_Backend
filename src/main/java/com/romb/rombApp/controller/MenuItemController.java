package com.romb.rombApp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.romb.rombApp.model.MenuItem;
import com.romb.rombApp.service.Interfaces.MenuItemService;

@RestController
@RequestMapping("/api/menu-items")
@CrossOrigin(origins = "http://localhost:3000") 
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping
    public List<MenuItem> getAllMenuItems() {
        return menuItemService.getAll();
    }

    @PostMapping
    public MenuItem createMenuItem(@RequestBody MenuItem menuItem) {
        return menuItemService.create(menuItem);
    }
}

