package com.romb.rombApp.servicetest;

import com.romb.rombApp.model.MenuCategory;
import com.romb.rombApp.model.MenuItem;
import com.romb.rombApp.repository.MenuItemRepository;
import com.romb.rombApp.service.Implementations.MenuItemServiceImpl;
import com.romb.rombApp.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuItemServiceImpl menuItemService;

    private MenuItem testItem;

    @BeforeEach
    void setUp() {
        testItem = MenuItem.builder()
                .id(1L)
                .name("Pizza")
                .description("Delicious cheese pizza")
                .price(BigDecimal.valueOf(12.99))
                .isAvailable(true)
                .weight(500)
                .calories(800)
                .imageUrl("http://example.com/pizza.jpg")
                .category(MenuCategory.MAIN_COURSE)
                .build();
    }

    @Test
    void createMenuItem_ShouldReturnSavedItem() {
        when(menuItemRepository.save(any())).thenReturn(testItem);

        MenuItem savedItem = menuItemService.create(testItem);

        assertNotNull(savedItem);
        assertEquals(testItem.getName(), savedItem.getName());
        verify(menuItemRepository).save(any());
    }

    @Test
    void getMenuItemById_ExistingId_ReturnsItem() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        MenuItem found = menuItemService.getById(1L);

        assertNotNull(found);
        assertEquals("Pizza", found.getName());
        verify(menuItemRepository).findById(1L);
    }

    @Test
    void getAllMenuItems_ShouldReturnList() {
        when(menuItemRepository.findAll()).thenReturn(List.of(testItem));

        List<MenuItem> items = menuItemService.getAll();

        assertEquals(1, items.size());
        assertEquals("Pizza", items.get(0).getName());
    }

    @Test
    void updateMenuItem_ValidId_ReturnsUpdatedItem() {
        MenuItem updated = MenuItem.builder()
                .id(1L)
                .name("Updated Pizza")
                .description("Spicy")
                .price(BigDecimal.valueOf(14.99))
                .isAvailable(false)
                .weight(550)
                .calories(900)
                .imageUrl("http://example.com/pizza.jpg")
                .category(MenuCategory.MAIN_COURSE)
                .build();

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(menuItemRepository.save(any())).thenReturn(updated);

        MenuItem result = menuItemService.update(1L, updated);

        assertEquals("Updated Pizza", result.getName());
        assertFalse(result.isAvailable());
    }

    @Test
    void deleteMenuItem_ValidId_ShouldCallDelete() {
        when(menuItemRepository.existsById(1L)).thenReturn(true);

        menuItemService.delete(1L);

        verify(menuItemRepository).deleteById(1L);
    }

    @Test
    void updateMenuItem_NonExistingId_ThrowsException() {
        Long nonExistingId = 999L;

        when(menuItemRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.update(nonExistingId, testItem));
    }

    @Test
    void getMenuItemById_NonExistingId_ThrowsException() {
        Long nonExistingId = 999L;

        when(menuItemRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.getById(nonExistingId));
    }

}