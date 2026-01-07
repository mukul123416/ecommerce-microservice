# 🚀 E-Commerce Microservices Ecosystem

[![Java Version](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Microservices](https://img.shields.io/badge/Architecture-Microservices-blue.svg)](https://microservices.io/)

A production-ready, cloud-native microservices architecture built with **Java 17**, **Spring Boot 3**, and **Spring Cloud**. This project demonstrates a complete DevOps lifecycle—from code quality analysis to containerized deployment.

---

## 🏗️ Architecture Overview

This project follows a **Decoupled Microservices Architecture** to ensure independent scalability and fault isolation.



* **Service Registry:** Netflix Eureka for dynamic service discovery.
* **API Gateway:** Spring Cloud Gateway as a single entry point with centralized routing.
* **Config Server:** Centralized Git-based configuration management.
* **Business Services:** User, Product, Order, Payment, Inventory, and Notification services.
* **Event-Bus:** Apache Kafka for asynchronous inter-service communication.
* **Database:** PostgreSQL (using `host.docker.internal` for hybrid container-host connectivity).

---

## 📊 System Design

```mermaid
graph TD
    User((User/Client)) -->|Request| Gateway[API Gateway: 8086]
    
    subgraph "Service Discovery & Config"
        Eureka[Service Registry: 8761]
        Config[Config Server: 8087]
    end

    subgraph "Internal Microservices"
        Gateway --> UserSvc[User Service: 8081]
        Gateway --> ProdSvc[Product Service: 8082]
        Gateway --> OrderSvc[Order Service: 8083]
        Gateway --> PaySvc[Payment Svc: 8084]
        Gateway --> InvSvc[Inventory Service: 8085]
    end

    subgraph "Event-Driven Message Bus"
        ProdSvc --> Kafka((Kafka Broker))
        OrderSvc --> Kafka
        InvSvc --> Kafka
        Kafka --> NotifSvc[Notification Svc: 8089]
    end

    subgraph "Infrastructure"
        UserSvc --> DB[(Postgres DB)]
        ProdSvc --> DB
        Config --> Git[(Git Config Repo)]
    end

    style Gateway fill:#f96,stroke:#333,stroke-width:2px
    style Kafka fill:#55f,color:#fff
    style Eureka fill:#4b5,color:#fff
    style DB fill:#eee,stroke:#333
