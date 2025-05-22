package com.romb.rombApp.messaging.producer;

import com.romb.rombApp.config.RabbitMQConfig;
import com.romb.rombApp.messaging.dto.OrderItemMessageDTO;
import com.romb.rombApp.messaging.dto.OrderMessageDTO;
import com.romb.rombApp.model.Order;
import com.romb.rombApp.model.OrderItem;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public OrderMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderCreatedMessage(Order order) {
        OrderMessageDTO dto = new OrderMessageDTO();
        dto.setOrderId(order.getId());
        dto.setTableId(order.getTable().getId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());

        dto.setItems(order.getItems().stream().map(item -> {
            OrderItemMessageDTO itemDTO = new OrderItemMessageDTO();
            itemDTO.setMenuItemName(item.getMenuItem().getName());
            itemDTO.setQuantity(item.getQuantity());
            return itemDTO;
        }).collect(Collectors.toList()));

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_ROUTING_KEY,
                dto
        );
    }
}
