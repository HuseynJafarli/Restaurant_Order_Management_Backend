package com.romb.rombApp.service.Implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.romb.rombApp.model.Order;
import com.romb.rombApp.repository.OrderRepository;
import com.romb.rombApp.service.Interfaces.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Order> getAll() {
        return repository.findAll();
    }

    @Override
    public Order getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Order create(Order order) {
        return repository.save(order);
    }

    @Override
    public Order update(Long id, Order order) {
        order.setId(id);
        return repository.save(order);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
