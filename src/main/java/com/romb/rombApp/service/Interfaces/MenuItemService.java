package com.romb.rombApp.service.Interfaces;

import java.util.List;

import com.romb.rombApp.model.MenuItem;

public interface MenuItemService {
    List<MenuItem> getAll();
    MenuItem getById(Long id);
    MenuItem create(MenuItem item);
    MenuItem update(Long id, MenuItem item);
    void delete(Long id);
}

