package com.romb.rombApp.controller;


import com.romb.rombApp.model.MenuCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<?> getMenuItemsByCategory(@PathVariable String categoryName) {
        MenuCategory category;

        // Convert the string to enum and handle invalid input
        try {
            category = MenuCategory.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "Invalid category: " + categoryName +
                            ". Allowed values: APPETIZER, MAIN_COURSE, DESSERT, DRINK"
            );
        }

        List<MenuItem> items = menuItemService.findByCategory(category);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public MenuItem getMenuItemById(@PathVariable Long id) {
        return menuItemService.getById(id);
    }

    @PutMapping("/{id}")
    public MenuItem updateMenuItem(@PathVariable Long id, @RequestBody MenuItem updatedItem) {
        return menuItemService.update(id, updatedItem);
    }

    @DeleteMapping("/{id}")
    public void deleteMenuItem(@PathVariable Long id) {
        menuItemService.delete(id);
    }

}

