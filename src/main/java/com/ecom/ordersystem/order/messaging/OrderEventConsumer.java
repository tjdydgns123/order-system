package com.ecom.ordersystem.order.messaging;

import com.ecom.ordersystem.order.dto.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventConsumer {

    @KafkaListener(topics = OrderEventProducer.TOPIC_ORDERS, groupId = "order-notification-group")
    public void onOrderEvent(OrderEvent event) {
        log.info("[Consumer-notification] 수신: {}", event);
        // 실무에서는 여기서 NotificationService 같은 걸 호출
        // 예: notificationService.sendOrderConfirmation(event);
    }
}
