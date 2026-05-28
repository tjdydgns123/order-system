package com.ecom.ordersystem.product.controller;

import com.ecom.ordersystem.product.dto.ProductCreateRequest;
import com.ecom.ordersystem.product.dto.ProductResponse;
import com.ecom.ordersystem.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponse create(@Valid @RequestBody ProductCreateRequest request) {
        return productService.create(request);
    }

    @GetMapping("/search")
    public List<ProductResponse> search(@RequestParam String q) {
        return productService.searchByName(q);
    }

    @GetMapping("/category/{category}")
    public List<ProductResponse> byCategory(@PathVariable String category) {
        return productService.findByCategory(category);
    }

    @GetMapping("/price")
    public List<ProductResponse> byPriceRange(@RequestParam int min, @RequestParam int max) {
        return productService.findByPriceRange(min, max);
    }
}
