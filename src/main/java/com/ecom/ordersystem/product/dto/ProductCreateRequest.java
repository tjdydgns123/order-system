package com.ecom.ordersystem.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    @Min(0)
    private int price;

    @Min(0)
    private int initialStock;
}
