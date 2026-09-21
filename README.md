# smartshop-microservices
E-Commerce Microservices Platform

# 🛒 SmartShop Microservices

A production-ready e-commerce backend built with **Spring Boot 4.x**, **Java 17**, and a full microservices architecture — featuring JWT auth, async event-driven communication via Kafka, distributed tracing with Zipkin, and full Docker orchestration.

---

## 📐 Architecture Overview

```
                        ┌─────────────────────────────────────────────┐
                        │           Client (REST / Browser)           │
                        └─────────────────┬───────────────────────────┘
                                          │
                        ┌─────────────────▼───────────────────────────┐
                        │        API Gateway  :8080                   │
                        │  Spring Cloud Gateway MVC + Resilience4j    │
                        │  Circuit Breaker · GatewayConfig.java DSL   │
                        └──┬──────┬───────┬────────┬──────────────────┘
                           │      │       │        │
              ┌────────────▼─┐ ┌──▼────┐ ┌▼──────┐ └──────────────┐
              │ User Service │ │Product│ │ Order │                 │
              │    :8081     │ │:8082  │ │ :8083 │                 │
              │  JWT Auth    │ │ CRUD  │ │Feign +│                 │
              └──────────────┘ └───────┘ │Kafka  │                 │
                                         └───┬───┘                 │
                                             │ Kafka Topic         │
                                    ┌────────▼────────┐            │
                                    │ Payment Service │            │
                                    │     :8084       │            │
                                    │  Kafka Consumer │            │
                                    └────────┬────────┘            │
                                             │ Kafka Topic         │
                                    ┌────────▼──────────┐          │
                                    │Notification Service│         │
                                    │      :8085         │         │
                                    │  Kafka + Email     │         │
                                    └────────────────────┘         │
                                                                    │
              ┌─────────────────────────────────────────────────────┘
              │
   ┌──────────▼────────┐    ┌────────────────┐    ┌──────────────┐
   │  Eureka Discovery │    │     Zipkin      │    │    MySQL     │
   │      :8761        │    │     :9411       │    │    :3306     │
   │  Service Registry │    │ Dist. Tracing   │    │  (per svc)   │
   └───────────────────┘    └────────────────┘    └──────────────┘
                                    ▲
                        ┌───────────┴───────────┐
                        │         Kafka          │
                        │    + Zookeeper :9092   │
                        └───────────────────────┘
```

---

## 🧩 Services

| Service | Port | Description |
|---|---|---|
| **Discovery Service** | `8761` | Eureka service registry — all services self-register |
| **API Gateway** | `8080` | Single entry point; routes, load balancing, circuit breaker |
| **User Service** | `8081` | Registration, login, JWT token issuance & validation |
| **Product Service** | `8082` | Product CRUD (create, read, update, delete) |
| **Order Service** | `8083` | Place orders via Feign → Product; publishes to Kafka |
| **Payment Service** | `8084` | Consumes order events; processes payment; publishes result |
| **Notification Service** | `8085` | Consumes payment events; sends email notifications |

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| **Language** | Java 17 |
| **Framework** | Spring Boot 4.x |
| **Service Discovery** | Spring Cloud Netflix Eureka |
| **API Gateway** | Spring Cloud Gateway MVC (`spring-cloud-starter-gateway-server-webmvc`) |
| **Circuit Breaker** | Resilience4j (via Gateway) |
| **Inter-service HTTP** | OpenFeign (Order → Product) |
| **Messaging** | Apache Kafka 4.2 + Zookeeper |
| **Database** | MySQL 8 (per-service schema) |
| **Auth** | JWT (JSON Web Tokens) |
| **Distributed Tracing** | Micrometer Tracing + Brave + Zipkin |
| **Containerisation** | Docker + Docker Compose (10 containers) |
| **Config** | `application.properties` (per service) |

---

## 🗂️ Project Structure

```
smartshop-microservices/
├── discovery-service/          # Eureka Server
├── api-gateway/                # Spring Cloud Gateway MVC
│   └── src/.../GatewayConfig.java   # Java DSL routes (not properties)
├── user-service/               # JWT Auth
├── product-service/            # Product CRUD
├── order-service/              # Feign + Kafka Producer
├── payment-service/            # Kafka Consumer → Producer
├── notification-service/       # Kafka Consumer + Email
├── docker-compose.yml          # Full stack orchestration
└── README.md
```

---

## ⚡ Event-Driven Flow

```
POST /api/orders
      │
      ▼
 Order Service
      ├──[Feign]──► Product Service  (validate stock)
      └──[Kafka]──► topic: order-created
                          │
                          ▼
                   Payment Service   (consume & process)
                          └──[Kafka]──► topic: payment-processed
                                              │
                                              ▼
                                    Notification Service
                                          └──► Email sent to user
```

---

## 🚀 Running the Project

### Prerequisites

- Docker Desktop installed and running
- Ports `8080`, `8081–8085`, `8761`, `9092`, `9411`, `3306` available

### Start Everything

```bash
git clone https://github.com/YOUR_USERNAME/smartshop-microservices.git
cd smartshop-microservices

docker-compose up --build
```

All 10 containers will start:
`discovery-service`, `api-gateway`, `user-service`, `product-service`, `order-service`, `payment-service`, `notification-service`, `mysql`, `kafka`, `zookeeper`, `zipkin`

### Verify Services Are Up

```bash
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
```

---

## 🧪 API Quick Reference

> All requests go through the **API Gateway at `http://localhost:8080`**

### Auth

```bash
# Register
POST /api/users/register
Content-Type: application/json
{
  "username": "alice",
  "email": "alice@example.com",
  "password": "password123"
}

# Login → returns JWT token
POST /api/users/login
{
  "username": "alice",
  "password": "password123"
}
```

### Products

```bash
# Create product
POST /api/products
Authorization: Bearer <token>
{
  "name": "Laptop",
  "description": "Gaming Laptop",
  "price": 999.99,
  "stock": 10
}

# List all products
GET /api/products
Authorization: Bearer <token>
```

### Orders (triggers full async chain)

```bash
POST /api/orders
Authorization: Bearer <token>
{
  "userId": 1,
  "productId": 1,
  "quantity": 2,
  "totalAmount": 1999.98
}
```

---

## 🔍 Monitoring & Observability

| Tool | URL | What to check |
|---|---|---|
| **Eureka Dashboard** | http://localhost:8761 | All services registered & UP |
| **Zipkin UI** | http://localhost:9411 | Distributed traces across services |
| **Actuator Health** | http://localhost:8080/actuator/health | Circuit breaker state, health |
| **Gateway Actuator** | http://localhost:8080/actuator/gateway/routes | Active routes |

### Zipkin Trace Example

After placing an order you should see a trace waterfall:
```
api-gateway  ──────────────────────────────────────  200ms
  order-service  ────────────────────────────────    180ms
    product-service (feign)  ──────────────          80ms
    kafka-producer  ─────                            10ms
```

---

## ⚙️ Key Configuration Notes

### Gateway — Java DSL (not properties)

Routes are defined in `GatewayConfig.java` using the Java DSL due to a known bug in `spring-cloud-starter-gateway-server-webmvc` with property-based routing:

```java
@Configuration
public class GatewayConfig {
    @Bean
    public RouterFunction<ServerResponse> routes() {
        return GatewayRouterFunctions.route("user-service")
            .route(RequestPredicates.path("/api/users/**"),
                HandlerFunctions.http("http://user-service:8081"))
            .build();
        // ... other routes
    }
}
```

### Tracing — every service has:

```properties
management.tracing.sampling.probability=1.0
management.zipkin.tracing.endpoint=http://zipkin:9411/api/v2/spans
logging.pattern.level=%5p [${spring.application.name},%X{traceId},%X{spanId}]
```

---

## 🐳 Docker Compose Overview

```yaml
services:
  discovery-service:   # :8761
  api-gateway:         # :8080  depends_on: discovery-service
  user-service:        # :8081  depends_on: discovery-service, mysql
  product-service:     # :8082  depends_on: discovery-service, mysql
  order-service:       # :8083  depends_on: discovery-service, mysql, kafka
  payment-service:     # :8084  depends_on: discovery-service, kafka
  notification-service:# :8085  depends_on: discovery-service, kafka
  mysql:               # :3306
  zookeeper:           # :2181
  kafka:               # :9092
  zipkin:              # :9411
```

---

## 📌 Notable Design Decisions

- **MVC Gateway over Reactive** — `spring-cloud-starter-gateway-server-webmvc` chosen to stay on the blocking/MVC stack consistent with all other services
- **Java DSL for routes** — property-based route config has a known bug in the MVC gateway variant; Java `GatewayConfig.java` is the workaround
- **Per-service databases** — each service owns its own MySQL schema (true microservice data isolation)
- **Kafka for async decoupling** — Order → Payment → Notification chain is fully event-driven; services are never directly coupled
- **`application.properties` throughout** — consistent with team convention; no YAML

---

## 📄 License

MIT © 2026 — Shubham Dekhane