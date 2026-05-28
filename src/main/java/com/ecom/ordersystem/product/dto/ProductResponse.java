package com.ecom.ordersystem.product.dto;

import com.ecom.ordersystem.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {

    private String id;
    private String name;
    private String category;
    private int price;
    private long stock;

    public static ProductResponse of(Product product, long stock) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .stock(stock)
                .build();
    }
}
