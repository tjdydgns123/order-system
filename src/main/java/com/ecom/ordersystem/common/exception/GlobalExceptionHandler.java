package com.ecom.ordersystem.common.exception;

import com.ecom.ordersystem.stock.exception.StockNotEnoughException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockNotEnoughException.class)
    public ResponseEntity<Map<String, Object>> handleStockNotEnough(StockNotEnoughException e) {
        log.warn("재고 부족: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorBody("STOCK_NOT_ENOUGH", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .orElse("입력값 오류");
        return ResponseEntity.badRequest().body(errorBody("VALIDATION_FAILED", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAny(Exception e) {
        log.error("서버 오류", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorBody("INTERNAL_ERROR", "서버 오류가 발생했습니다"));
    }

    private Map<String, Object> errorBody(String code, String message) {
        return Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "code", code,
                "message", message
        );
    }
}
