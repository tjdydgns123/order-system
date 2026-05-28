package com.ecom.ordersystem.order.messaging;

import com.ecom.ordersystem.order.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    public static final String TOPIC_ORDERS = "orders";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(OrderEvent event) {
        log.info("[Producer] 발행 → {}: {}", TOPIC_ORDERS, event);
        kafkaTemplate.send(TOPIC_ORDERS, event.getOrderId(), event);
    }
}
