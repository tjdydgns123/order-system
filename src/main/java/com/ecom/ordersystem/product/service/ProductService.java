package com.ecom.ordersystem.product.service;

import com.ecom.ordersystem.product.domain.Product;
import com.ecom.ordersystem.product.dto.ProductCreateRequest;
import com.ecom.ordersystem.product.dto.ProductResponse;
import com.ecom.ordersystem.product.repository.ProductRepository;
import com.ecom.ordersystem.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StockService stockService;

    /** 상품 등록 → ES 색인 + Redis 재고 초기화 */
    public ProductResponse create(ProductCreateRequest request) {
        Product product = Product.builder()
                .id(request.getId())
                .name(request.getName())
                .category(request.getCategory())
                .price(request.getPrice())
                .createdAt(System.currentTimeMillis())
                .build();

        Product saved = productRepository.save(product);
        stockService.initStock(saved.getId(), request.getInitialStock());
        log.info("[Product] 등록 완료 {} (재고 {})", saved.getId(), request.getInitialStock());

        return ProductResponse.of(saved, request.getInitialStock());
    }

    /** 상품명 검색 — ES 풀텍스트 */
    public List<ProductResponse> searchByName(String keyword) {
        return productRepository.findByNameContaining(keyword).stream()
                .map(p -> ProductResponse.of(p, stockService.getStock(p.getId())))
                .toList();
    }

    /** 카테고리 필터 */
    public List<ProductResponse> findByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(p -> ProductResponse.of(p, stockService.getStock(p.getId())))
                .toList();
    }

    /** 가격 범위 */
    public List<ProductResponse> findByPriceRange(int min, int max) {
        return productRepository.findByPriceBetween(min, max).stream()
                .map(p -> ProductResponse.of(p, stockService.getStock(p.getId())))
                .toList();
    }
}
