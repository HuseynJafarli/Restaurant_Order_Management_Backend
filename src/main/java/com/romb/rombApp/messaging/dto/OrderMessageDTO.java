package com.romb.rombApp.messaging.dto;

import com.romb.rombApp.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderMessageDTO {
    private Long orderId;
    private Long tableId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<OrderItemMessageDTO> items;
}
