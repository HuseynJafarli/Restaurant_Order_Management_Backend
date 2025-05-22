package com.romb.rombApp.service.Implementations;

import com.romb.rombApp.dto.*;
import com.romb.rombApp.exception.NoContentException;
import com.romb.rombApp.exception.ResourceNotFoundException;
import com.romb.rombApp.model.*;
import com.romb.rombApp.messaging.producer.OrderMessageProducer;
import com.romb.rombApp.repository.MenuItemRepository;
import com.romb.rombApp.repository.OrderRepository;
import com.romb.rombApp.repository.RestaurantTableRepository;
import com.romb.rombApp.service.Interfaces.OrderService;

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

    @Autowired
    private OrderMessageProducer orderMessageProducer;

    @Override
    public List<Order> getAll() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new NoContentException("No orders found.");
        }
        return orders;
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }

    @Override
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order update(Long id, Order updatedOrder) {
        Order existing = getById(id);
        existing.setStatus(updatedOrder.getStatus());
        existing.setTable(updatedOrder.getTable());
        existing.setItems(updatedOrder.getItems());
        existing.setTotalAmount(updatedOrder.getTotalAmount());
        return orderRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public Order createFromDTO(OrderRequestDTO dto) {
        RestaurantTable table = tableRepository.findById(dto.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        Order order = new Order();
        order.setTable(table);
        order.setStatus(dto.getStatus());
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = dto.getItems().stream().map(itemDto -> {
            MenuItem menuItem = menuItemRepository.findById(itemDto.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found"));

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

        Order savedOrder = orderRepository.save(order);

        orderMessageProducer.sendOrderCreatedMessage(savedOrder);

        return savedOrder;
    }

    @Override
    public OrderResponseDTO convertToResponseDTO(Order order) {
        Long tableId = order.getTable() != null ? order.getTable().getId() : null;

        List<OrderItemDetailsDTO> itemDetails = order.getItems() != null
                ? order.getItems().stream().map(item -> {
                    MenuItem menuItem = item.getMenuItem();
                    return OrderItemDetailsDTO.builder()
                            .menuItemName(menuItem.getName())
                            .quantity(item.getQuantity())
                            .build();
                }).collect(Collectors.toList())
                : List.of();

        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .tableId(tableId)
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(itemDetails)
                .build();
    }
}
