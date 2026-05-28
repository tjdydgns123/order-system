package com.ecom.ordersystem.product.repository;

import com.ecom.ordersystem.product.domain.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    /** 상품명 부분일치 검색 (ES Text 필드 → 형태소 분석 후 매칭) */
    List<Product> findByNameContaining(String keyword);

    /** 카테고리 정확일치 */
    List<Product> findByCategory(String category);

    /** 가격 범위 */
    List<Product> findByPriceBetween(int min, int max);
}
