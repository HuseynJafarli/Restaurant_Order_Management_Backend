package com.romb.rombApp.servicetest;

import com.romb.rombApp.dto.OrderItemDTO;
import com.romb.rombApp.dto.OrderRequestDTO;
import com.romb.rombApp.exception.NoContentException;
import com.romb.rombApp.exception.ResourceNotFoundException;
import com.romb.rombApp.messaging.producer.OrderMessageProducer;
import com.romb.rombApp.model.*;
import com.romb.rombApp.repository.MenuItemRepository;
import com.romb.rombApp.repository.OrderRepository;
import com.romb.rombApp.repository.RestaurantTableRepository;
import com.romb.rombApp.service.Implementations.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantTableRepository tableRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderMessageProducer orderMessageProducer;

    private Order mockOrder;
    private RestaurantTable mockTable;
    private MenuItem mockMenuItem;

    @BeforeEach
    void setup() {
        mockTable = RestaurantTable.builder().id(1L).build();
        mockMenuItem = MenuItem.builder().id(1L).name("Pizza").price(BigDecimal.valueOf(10)).build();

        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .menuItem(mockMenuItem)
                .quantity(2)
                .build();

        mockOrder = Order.builder()
                .id(1L)
                .table(mockTable)
                .status(OrderStatus.IN_PREPARATION)
                .totalAmount(BigDecimal.valueOf(20))
                .createdAt(LocalDateTime.now())
                .items(List.of(orderItem))
                .build();

        orderItem.setOrder(mockOrder);
    }

    @Test
    void getAll_ReturnsOrderList() {
        when(orderRepository.findAll()).thenReturn(List.of(mockOrder));
        List<Order> orders = orderService.getAll();
        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
    }

    @Test
    void getAll_ThrowsNoContentException() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(NoContentException.class, () -> orderService.getAll());
    }

    @Test
    void getById_ExistingId_ReturnsOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));
        Order found = orderService.getById(1L);
        assertEquals(mockOrder.getId(), found.getId());
    }

    @Test
    void getById_NonExistingId_ThrowsException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.getById(999L));
    }

    @Test
    void create_SavesOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        Order created = orderService.create(mockOrder);
        assertNotNull(created);
        assertEquals(mockOrder.getId(), created.getId());
    }

    @Test
    void update_ExistingId_UpdatesStatus() {
        Order updated = Order.builder().status(OrderStatus.DELIVERED).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order result = orderService.update(1L, updated);
        assertEquals(OrderStatus.DELIVERED, result.getStatus());
    }

    @Test
    void update_NonExistingId_ThrowsException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.update(999L, mockOrder));
    }

    @Test
    void delete_ValidId_DeletesOrder() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);
        assertDoesNotThrow(() -> orderService.delete(1L));
    }

    @Test
    void delete_InvalidId_ThrowsException() {
        when(orderRepository.existsById(999L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> orderService.delete(999L));
    }

    @Test
    void createFromDTO_ValidDTO_CreatesOrder() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setTableId(1L);
        dto.setStatus(OrderStatus.IN_PREPARATION);

        OrderItemDTO itemDto = new OrderItemDTO();
        itemDto.setMenuItemId(1L);
        itemDto.setQuantity(2);
        dto.setItems(List.of(itemDto));

        when(tableRepository.findById(1L)).thenReturn(Optional.of(mockTable));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(mockMenuItem));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order result = orderService.createFromDTO(dto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void createFromDTO_InvalidTable_ThrowsException() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setTableId(999L);
        dto.setItems(List.of());

        when(tableRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createFromDTO(dto));
    }

    @Test
    void createFromDTO_InvalidMenuItem_ThrowsException() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setTableId(1L);
        dto.setStatus(OrderStatus.IN_PREPARATION);

        OrderItemDTO itemDto = new OrderItemDTO();
        itemDto.setMenuItemId(999L);
        itemDto.setQuantity(2);
        dto.setItems(List.of(itemDto));

        when(tableRepository.findById(1L)).thenReturn(Optional.of(mockTable));
        when(menuItemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createFromDTO(dto));
    }
}