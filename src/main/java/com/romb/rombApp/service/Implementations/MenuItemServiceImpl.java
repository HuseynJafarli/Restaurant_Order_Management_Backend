package com.romb.rombApp.service.Implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.romb.rombApp.model.MenuItem;
import com.romb.rombApp.repository.MenuItemRepository;
import com.romb.rombApp.service.Interfaces.MenuItemService;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository repository;

    public MenuItemServiceImpl(MenuItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MenuItem> getAll() {
        return repository.findAll();
    }

    @Override
    public MenuItem getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public MenuItem create(MenuItem item) {
        return repository.save(item);
    }

    @Override
    public MenuItem update(Long id, MenuItem item) {
        item.setId(id);
        return repository.save(item);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

