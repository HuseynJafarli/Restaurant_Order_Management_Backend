package com.romb.rombApp.messaging.dto;

import lombok.Data;

@Data
public class OrderItemMessageDTO {
    private String menuItemName;
    private int quantity;
}