package com.romb.rombApp.service.Interfaces;

import java.util.List;
import com.romb.rombApp.model.Order;

public interface OrderService {
    List<Order> getAll();
    Order getById(Long id);
    Order create(Order order);
    Order update(Long id, Order order);
    void delete(Long id);
}
