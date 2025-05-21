package com.romb.rombApp.service.Implementations;

import com.romb.rombApp.dto.*;
import com.romb.rombApp.model.*;
import com.romb.rombApp.repository.MenuItemRepository;
import com.romb.rombApp.repository.OrderRepository;
import com.romb.rombApp.repository.RestaurantTableRepository;
import com.romb.rombApp.service.Interfaces.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantTableRepository tableRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
    }

    @Override
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order update(Long id, Order updatedOrder) {
        Order existing = getById(id);
        existing.setStatus(updatedOrder.getStatus());
        return orderRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
    }

    public Order createFromDTO(OrderRequestDTO dto) {
        RestaurantTable table = tableRepository.findById(dto.getTableId())
                .orElseThrow(() -> new EntityNotFoundException("Table not found"));

        Order order = new Order();
        order.setTable(table);
        order.setStatus(dto.getStatus());
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = dto.getItems().stream().map(itemDto -> {
            MenuItem menuItem = menuItemRepository.findById(itemDto.getMenuItemId())
                    .orElseThrow(() -> new EntityNotFoundException("MenuItem not found"));

            return OrderItem.builder()
                    .order(order)
                    .menuItem(menuItem)
                    .quantity(itemDto.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        order.setItems(items);

        BigDecimal total = items.stream()
                .map(i -> i.getMenuItem().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    public OrderResponseDTO convertToResponseDTO(Order order) {
        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .tableId(order.getTable().getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream().map(item -> {
                    MenuItem menuItem = item.getMenuItem();
                    BigDecimal subtotal = menuItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    return OrderItemDetailsDTO.builder()
                            .menuItemName(menuItem.getName())
                            .quantity(item.getQuantity())
                            .itemPrice(menuItem.getPrice())
                            .subtotal(subtotal)
                            .build();
                }).collect(Collectors.toList()))
                .build();
    }
}
