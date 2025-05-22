package com.romb.rombApp.messaging;

import com.romb.rombApp.messaging.dto.OrderMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderConsumer {

    @RabbitListener(queues = "order.queue")
    public void receiveOrder(OrderMessageDTO message) {
        log.info("New order received for table {}:", message.getTableId());
        message.getItems().forEach(item ->
                log.info(" - {} x {}", item.getMenuItemName(), item.getQuantity())
        );
        log.info("Total Amount: ${}", message.getTotalAmount());
    }
}
