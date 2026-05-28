package com.ecom.ordersystem.stock.controller;

import com.ecom.ordersystem.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/{productId}")
    public Map<String, Object> init(@PathVariable String productId,
                                    @RequestParam int quantity) {
        stockService.initStock(productId, quantity);
        return Map.of("productId", productId, "quantity", quantity);
    }

    @GetMapping("/{productId}")
    public Map<String, Object> get(@PathVariable String productId) {
        return Map.of("productId", productId, "stock", stockService.getStock(productId));
    }

    @GetMapping("/popular")
    public Map<String, Double> popular(@RequestParam(defaultValue = "10") int n) {
        Set<ZSetOperations.TypedTuple<String>> top = stockService.topN(n);
        Map<String, Double> result = new LinkedHashMap<>();
        if (top != null) {
            top.forEach(t -> result.put(t.getValue(), t.getScore()));
        }
        return result;
    }
}
