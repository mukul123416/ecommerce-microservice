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

## 📊 Advanced System Architecture

```mermaid
graph TD
    %% User Entry
    User((User/Client)) -->|HTTPS/REST| Gateway[API Gateway: 8086]
    
    %% Infrastructure Services
    Eureka[Service Registry: 8761]
    Config[Config Server: 8087]

    %% Infrastructure Connection
    Config -.-> Eureka

    %% Business Logic Layer
    subgraph "Core Business Services"
        Gateway --> UserSvc[User Service: 8081]
        Gateway --> ProdSvc[Product Service: 8082]
        Gateway --> OrderSvc[Order Service: 8083]
        Gateway --> PaySvc[Payment Svc: 8084]
        Gateway --> InvSvc[Inventory Service: 8085]
    end

    %% Registry Discovery
    UserSvc -.-> Eureka
    ProdSvc -.-> Eureka
    OrderSvc -.-> Eureka
    PaySvc -.-> Eureka
    InvSvc -.-> Eureka
    Gateway -.-> Eureka

    %% Messaging Layer (Overlap Fixed)
    subgraph "Message Bus"
        Kafka{Kafka Broker}
        Kafka --> NotifSvc[Notification Svc: 8089]
    end
    
    ProdSvc --> Kafka
    OrderSvc --> Kafka
    InvSvc --> Kafka

    %% Data Layer
    subgraph "Persistence Layer"
        UserSvc --> DB1[(Postgres: User DB)]
        ProdSvc --> DB2[(Postgres: Product DB)]
        OrderSvc --> DB3[(Postgres: Order DB)]
    end

    %% Style Fixes
    style Gateway fill:#FF8C00,stroke:#333,stroke-width:2px,color:#fff
    style Kafka fill:#282C34,stroke:#55f,color:#61DAFB,stroke-width:3px
    style Eureka fill:#2ECC71,stroke:#333,color:#fff
    style Config fill:#2ECC71,stroke:#333,color:#fff
    style DB1 fill:#34495E,stroke:#fff,color:#fff
    style DB2 fill:#34495E,stroke:#fff,color:#fff
    style DB3 fill:#34495E,stroke:#fff,color:#fff
    style UserSvc fill:#f9f9f9,stroke:#333,color:#000
    style ProdSvc fill:#f9f9f9,stroke:#333,color:#000
    style OrderSvc fill:#f9f9f9,stroke:#333,color:#000
    style InvSvc fill:#f9f9f9,stroke:#333,color:#000
    style NotifSvc fill:#f9f9f9,stroke:#333,color:#000
