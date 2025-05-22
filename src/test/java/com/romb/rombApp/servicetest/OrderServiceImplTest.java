package com.romb.rombApp.servicetest;

import com.romb.rombApp.dto.OrderItemDTO;
import com.romb.rombApp.dto.OrderRequestDTO;
import com.romb.rombApp.exception.ResourceNotFoundException;
import com.romb.rombApp.messaging.producer.OrderMessageProducer;
import com.romb.rombApp.model.*;
import com.romb.rombApp.repository.MenuItemRepository;
import com.romb.rombApp.repository.OrderRepository;
import com.romb.rombApp.repository.RestaurantTableRepository;
import com.romb.rombApp.service.Implementations.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantTableRepository tableRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private OrderMessageProducer orderMessageProducer;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testGetById_WhenOrderExists_ReturnsOrder() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.NEW);

        when(redisTemplate.opsForValue().get("order_status:" + orderId)).thenReturn(null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository).findById(orderId);
    }

    @Test
    public void testGetById_WhenOrderNotFound_ThrowsException() {
        Long orderId = 999L;

        when(redisTemplate.opsForValue().get("order_status:" + orderId)).thenReturn(null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getById(orderId));
    }

    @Test
    public void testCreate_SavesAndReturnsOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.NEW);

        when(orderRepository.save(order)).thenReturn(order);

        Order savedOrder = orderService.create(order);

        assertNotNull(savedOrder);
        assertEquals(order.getId(), savedOrder.getId());
        verify(orderRepository).save(order);
    }

    @Test
    public void testUpdate_WhenOrderExists_UpdatesStatus() {
        Long orderId = 1L;
        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setStatus(OrderStatus.NEW);

        Order updatedOrder = new Order();
        updatedOrder.setStatus(OrderStatus.READY); // updated to use valid enum

        when(redisTemplate.opsForValue().get("order_status:" + orderId)).thenReturn(null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        Order result = orderService.update(orderId, updatedOrder);

        assertEquals(OrderStatus.READY, result.getStatus());
        verify(orderRepository).save(existingOrder);
    }

    @Test
    public void testUpdate_WhenOrderNotFound_ThrowsException() {
        Long orderId = 999L;
        Order updatedOrder = new Order();
        updatedOrder.setStatus(OrderStatus.DELIVERED);

        when(redisTemplate.opsForValue().get("order_status:" + orderId)).thenReturn(null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.update(orderId, updatedOrder));
    }

    @Test
    public void testDelete_WhenOrderExists_DeletesOrder() {
        Long orderId = 1L;

        when(orderRepository.existsById(orderId)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(orderId);
        when(redisTemplate.delete("order_status:" + orderId)).thenReturn(true); // Fix here

        assertDoesNotThrow(() -> orderService.delete(orderId));

        verify(orderRepository).deleteById(orderId);
        verify(redisTemplate).delete("order_status:" + orderId);
    }

    @Test
    public void testCreateFromDTO_ValidDTO_CreatesOrderSuccessfully() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setTableId(1L);
        dto.setStatus(OrderStatus.NEW);

        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setMenuItemId(1L);
        itemDTO.setQuantity(2);
        dto.setItems(List.of(itemDTO));

        RestaurantTable table = new RestaurantTable();
        table.setId(1L);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setPrice(BigDecimal.valueOf(10));

        when(tableRepository.findById(1L)).thenReturn(Optional.of(table));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        // Fix: manually assign menuItem to OrderItem in mock response
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.getItems().forEach(i -> i.setMenuItem(menuItem));
            return o;
        });

        Order result = orderService.createFromDTO(dto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(20), result.getTotalAmount());
        assertEquals(1, result.getItems().size());
        verify(orderRepository).save(any(Order.class));
    }
}
