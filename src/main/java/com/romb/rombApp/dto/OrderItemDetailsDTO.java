package com.romb.rombApp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDetailsDTO {
    private String menuItemName;
    private int quantity;
    private BigDecimal itemPrice;
    private BigDecimal subtotal;
}
