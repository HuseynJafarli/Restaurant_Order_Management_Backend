package com.romb.rombApp.controller;

import com.romb.rombApp.dto.OrderRequestDTO;
import com.romb.rombApp.dto.OrderResponseDTO;
import com.romb.rombApp.model.Order;
import com.romb.rombApp.service.Implementations.OrderServiceImpl;
import com.romb.rombApp.service.Interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAll().stream()
                .map(orderServiceImpl::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id) {
        return orderServiceImpl.convertToResponseDTO(orderService.getById(id));
    }

    @PostMapping
    public OrderResponseDTO createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        return orderServiceImpl.convertToResponseDTO(orderServiceImpl.createFromDTO(orderRequestDTO));
    }

    @PutMapping("/{id}")
    public OrderResponseDTO updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO dto) {
        Order existing = orderService.getById(id);
        existing.setStatus(dto.getStatus());
        return orderServiceImpl.convertToResponseDTO(orderService.update(id, existing));
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
    }
}
