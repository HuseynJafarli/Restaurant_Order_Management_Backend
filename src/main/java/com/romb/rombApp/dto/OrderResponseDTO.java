package com.romb.rombApp.dto;

import com.romb.rombApp.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private Long tableId;
    private List<OrderItemDetailsDTO> items;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}
