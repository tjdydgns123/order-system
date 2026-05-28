package com.ecom.ordersystem.stock.service;

import com.ecom.ordersystem.stock.exception.StockNotEnoughException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private static final String STOCK_KEY_PREFIX = "stock:";
    private static final String POPULAR_KEY = "popular:products";

    private final StringRedisTemplate redisTemplate;

    /** 재고 초기화 (관리자/상품등록 시 호출) */
    public void initStock(String productId, int quantity) {
        redisTemplate.opsForValue().set(stockKey(productId), String.valueOf(quantity));
        log.info("[Stock] 초기화 {} = {}", productId, quantity);
    }

    /** 현재 재고 조회 */
    public long getStock(String productId) {
        String value = redisTemplate.opsForValue().get(stockKey(productId));
        return value == null ? 0L : Long.parseLong(value);
    }

    /**
     * 재고 차감 — Redis DECRBY는 원자 연산이라 동시성 안전.
     * 차감 결과가 음수면 부족이므로 즉시 복구(보상)하고 예외.
     */
    public long decreaseStock(String productId, int quantity) {
        Long remaining = redisTemplate.opsForValue().decrement(stockKey(productId), quantity);
        if (remaining == null) {
            throw new StockNotEnoughException("재고 정보가 없습니다: " + productId);
        }
        if (remaining < 0) {
            // 보상: 음수만큼 다시 더해서 원상복구
            redisTemplate.opsForValue().increment(stockKey(productId), quantity);
            throw new StockNotEnoughException(
                "재고 부족: productId=" + productId + ", 요청=" + quantity);
        }
        log.info("[Stock] 차감 {} -{} (남은 재고={})", productId, quantity, remaining);
        return remaining;
    }

    /** 차감 실패/주문 취소 시 복구 */
    public void increaseStock(String productId, int quantity) {
        Long after = redisTemplate.opsForValue().increment(stockKey(productId), quantity);
        log.info("[Stock] 복구 {} +{} (남은 재고={})", productId, quantity, after);
    }

    /** 인기상품 랭킹 점수 +1 (Sorted Set) */
    public void incrementPopularity(String productId, int delta) {
        redisTemplate.opsForZSet().incrementScore(POPULAR_KEY, productId, delta);
    }

    /** Top N 인기상품 */
    public Set<ZSetOperations.TypedTuple<String>> topN(int n) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(POPULAR_KEY, 0, n - 1);
    }

    private String stockKey(String productId) {
        return STOCK_KEY_PREFIX + productId;
    }
}
