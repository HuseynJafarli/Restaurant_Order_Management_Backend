package com.romb.rombApp.dto;

import com.romb.rombApp.model.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private Long tableId;
    private List<OrderItemDTO> items;
    private OrderStatus status;
}
