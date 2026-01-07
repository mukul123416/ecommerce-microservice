🚀 E-Commerce Microservices Ecosystem
A production-ready, cloud-native microservices architecture built with Java 17, Spring Boot 3, and Spring Cloud. This project demonstrates a complete DevOps lifecycle, from code quality (Sonar) to containerization (Docker) and event-driven communication (Kafka).

🏗️ Architecture Overview
This project follows a Decoupled Microservices Architecture with the following components:

Service Registry: Netflix Eureka for dynamic service discovery.

API Gateway: Spring Cloud Gateway as a single entry point with centralized routing.

Config Server: Centralized Git-based configuration management.

Business Services: User, Product, Order, Payment, Inventory, and Notification services.

Event-Bus: Apache Kafka for asynchronous inter-service communication.

Database: PostgreSQL (with host.docker.internal mapping for local/container hybrid connectivity).

🛠️ Tech Stack & Tools
Backend: Java 17, Spring Boot 3.x, Spring Cloud.

Communication: FeignClient (Synchronous), Apache Kafka (Asynchronous).

Database: PostgreSQL, JPA/Hibernate.

Resilience: Resilience4j (Circuit Breaker, Retry, Rate Limiter).

Security: Okta OAuth2 / JWT with Role-Based Access Control (@PreAuthorize).

DevOps: GitHub Actions (CI/CD), SonarCloud (Code Analysis), Docker & Docker Compose.

⚡ Key Features
1. Fault Tolerance & Resilience
Implemented Resilience4j to handle service failures gracefully:

Circuit Breaker: Prevents cascading failures with Open/Half-Open states.

Retry: Automatic retry mechanism for transient failures.

Rate Limiter: Protects services from traffic spikes and DoS attacks.

2. CI/CD Pipeline
Fully automated pipeline in .github/workflows/ci.yml:

Multi-Module Build: Individual service build and test using Maven.

Code Quality: Deep scan via SonarCloud for bugs, vulnerabilities, and code smell.

3. Containerization
Docker Compose orchestrates the entire ecosystem:

Isolated virtual network (microservice-network).

Automated service dependency management (depends_on).

Configurable environment variables for seamless deployment.

🚀 How to Run
Prerequisites
Docker & Docker Compose installed.

Java 17 & Maven (for local builds).

Steps
Clone the Repository:

Bash

git clone https://github.com/mukul123416/ecommerce-microservice.git
cd ecommerce-microservices
Build the Project:

Bash

mvn clean install -DskipTests
Spin up the Infrastructure:

Bash

docker-compose up --build
📈 Monitoring & Health
Eureka Dashboard: http://localhost:8761

Actuator Health: http://localhost:<port>/actuator/health

SonarCloud Dashboard: https://sonarcloud.io/projects

🧑‍💻 Author
Mukul - Backend Engineer / Java Specialist

"Built with a focus on scalability, fault tolerance, and automated delivery."
