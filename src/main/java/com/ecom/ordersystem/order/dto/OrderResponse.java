package com.ecom.ordersystem.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponse {

    private String orderId;
    private String productId;
    private int quantity;
    private String status;
    private LocalDateTime createdAt;
}
