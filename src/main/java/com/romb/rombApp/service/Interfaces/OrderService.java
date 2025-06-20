package com.romb.rombApp.service.Interfaces;

import java.util.List;

import com.romb.rombApp.dto.OrderRequestDTO;
import com.romb.rombApp.dto.OrderResponseDTO;
import com.romb.rombApp.model.Order;

public interface OrderService {
    List<Order> getAll();
    Order getById(Long id);
    Order create(Order order);
    Order update(Long id, Order order);
    void delete(Long id);
    OrderResponseDTO convertToResponseDTO(Order order);
    Order createFromDTO(OrderRequestDTO dto);
}
