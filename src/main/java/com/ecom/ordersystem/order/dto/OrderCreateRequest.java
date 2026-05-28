package com.ecom.ordersystem.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    @NotBlank(message = "productId는 필수입니다")
    private String productId;

    @Min(value = 1, message = "quantity는 1 이상이어야 합니다")
    private int quantity;
}
