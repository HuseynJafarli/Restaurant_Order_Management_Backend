package com.romb.rombApp.service.Implementations;

import com.romb.rombApp.model.MenuCategory;
import com.romb.rombApp.model.MenuItem;
import com.romb.rombApp.repository.MenuItemRepository;
import com.romb.rombApp.service.Interfaces.MenuItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public List<MenuItem> getAll() {
        return menuItemRepository.findAll();
    }

    @Override
    public MenuItem getById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found with ID: " + id));
    }

    @Override
    public List<MenuItem> findByCategory(MenuCategory category) {
        return menuItemRepository.findByCategory(category);
    }

    @Override
    public MenuItem create(MenuItem item) {
        return menuItemRepository.save(item);
    }

    @Override
    public MenuItem update(Long id, MenuItem item) {
        MenuItem existing = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MenuItem not found with ID: " + id));

        existing.setName(item.getName());
        existing.setDescription(item.getDescription());
        existing.setPrice(item.getPrice());
        existing.setAvailable(item.isAvailable());
        existing.setWeight(item.getWeight());
        existing.setCalories(item.getCalories());
        existing.setImageUrl(item.getImageUrl());
        existing.setCategory(item.getCategory());

        return menuItemRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new EntityNotFoundException("MenuItem not found with ID: " + id);
        }
        menuItemRepository.deleteById(id);
    }
}
