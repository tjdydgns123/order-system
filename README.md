# order-system

Kafka, Redis, Elasticsearch를 활용한 주문 시스템 토이 프로젝트.

## 기술 스택

- **Java 17**, **Spring Boot 3.5.0**
- **Apache Kafka** — 주문 이벤트 비동기 처리 (Producer / Consumer)
- **Redis** — 재고 캐시 및 동시성 제어
- **Elasticsearch** — 상품 검색
- **Maven**, **Lombok**

## 도메인 구성

```
com.ecom.ordersystem
├── order      주문 생성, 이벤트 발행/소비
├── product    상품 등록 및 검색 (Elasticsearch)
├── stock      재고 관리 (Redis)
└── common     공통 예외 처리
```

## 주요 흐름

1. 주문 요청 → `OrderController` 수신
2. `StockService`가 Redis에서 재고 차감 (부족 시 `StockNotEnoughException`)
3. `OrderEventProducer`가 Kafka로 주문 이벤트 발행
4. `OrderEventConsumer`가 이벤트를 구독하여 후속 처리
5. 상품 조회는 Elasticsearch 인덱스를 통해 제공

## 실행 전 준비

로컬에 다음 서비스가 실행 중이어야 합니다.

| 서비스 | 포트 |
|--------|------|
| Redis | 6379 |
| Elasticsearch | 9200 |
| Kafka | 9092 |

## 실행

```bash
./mvnw spring-boot:run
```

서버는 `http://localhost:8080` 에서 기동됩니다.
