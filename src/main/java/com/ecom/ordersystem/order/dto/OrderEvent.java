package com.ecom.ordersystem.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {

    private String eventType;     // "ORDER_CREATED", "ORDER_CANCELED" 등
    private String orderId;
    private String productId;
    private int quantity;
    private long timestamp;
}
