package com.ecom.ordersystem.order.service;

import com.ecom.ordersystem.order.dto.OrderCreateRequest;
import com.ecom.ordersystem.order.dto.OrderEvent;
import com.ecom.ordersystem.order.dto.OrderResponse;
import com.ecom.ordersystem.order.messaging.OrderEventProducer;
import com.ecom.ordersystem.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final StockService stockService;
    private final OrderEventProducer orderEventProducer;

    public OrderResponse createOrder(OrderCreateRequest request) {
        String orderId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        // 1. 재고 차감 (Redis 원자 연산) — 실패 시 StockNotEnoughException
        stockService.decreaseStock(request.getProductId(), request.getQuantity());

        // 2. 인기상품 점수 증가 (랭킹용)
        stockService.incrementPopularity(request.getProductId(), request.getQuantity());

        // 3. Kafka 이벤트 발행 — 실패하면 차감했던 재고 복구
        try {
            OrderEvent event = OrderEvent.builder()
                    .eventType("ORDER_CREATED")
                    .orderId(orderId)
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .timestamp(System.currentTimeMillis())
                    .build();
            orderEventProducer.publish(event);
        } catch (Exception e) {
            log.error("Kafka 발행 실패 → 재고 복구", e);
            stockService.increaseStock(request.getProductId(), request.getQuantity());
            throw e;
        }

        return OrderResponse.builder()
                .orderId(orderId)
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .status("CREATED")
                .createdAt(now)
                .build();
    }
}
