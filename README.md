# Customer Microservice - Banking System

A production-ready microservice for managing customer information in a distributed banking system. Built with Spring Boot and following contract-first API design principles using OpenAPI 3.0 specifications.

## 🎯 Overview

This microservice handles all customer-related operations including registration, profile management, and validation services for other microservices in the banking ecosystem. It implements RESTful APIs with comprehensive validation, error handling, and inter-service communication patterns.

## 🏗️ Architecture & Design

### Key Features
- **Contract-First API Design**: OpenAPI 3.0 specifications drive development, ensuring consistency between documentation and implementation
- **API Architecture**: Separate public and internal APIs for client-facing operations and microservice communication
- **Domain-Driven Design**: Clear separation between DTOs, entities, and service layers
- **Functional Programming**: Applied to validation logic for immutable, predictable data processing
- **Type-Safe Interfaces**: Auto-generated interfaces from OpenAPI specs ensure compile-time contract adherence

### Technology Stack
- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Database**: MySQL with JPA/Hibernate
- **API Documentation**: OpenAPI 3.0 (Swagger UI)
- **Testing**: JUnit 5, Mockito
- **Code Quality**: JaCoCo (70% coverage minimum), Checkstyle
- **Build Tool**: Maven

## 📋 API Endpoints

### Client-Facing API (`/api/v1/customers`)
- `GET /` - Retrieve all active customers
- `GET /{customerId}` - Retrieve customer by ID
- `POST /` - Create new customer
- `PATCH /update/{customerId}` - Update customer information
- `PATCH /activate/{customerId}` - Activate customer account
- `PATCH /deactivate/{customerId}` - Deactivate customer account

### Internal API (`/api/v1/internal/customers`)
- `GET /validate-customer/{customerId}` - Validate customer for inter-service operations

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- MySQL 8.0+

### Local Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/abengl/BankingSystem-CustomerMs.git
   cd customer-ms
   ```

2. **Configure database**
   ```properties
   # application.properties
   spring.datasource.url=jdbc:mysql://localhost:3306/customer_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The service will start on `http://localhost:8085`

### API Documentation

Access interactive API documentation at:
- **Swagger UI**: [http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html)
- **OpenAPI Spec**: [http://localhost:8085/v3/api-docs](http://localhost:8085/v3/api-docs)

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Generate Coverage Report
```bash
mvn clean test jacoco:report
```
View report at `target/site/jacoco/index.html`

### Code Quality Check
```bash
mvn checkstyle:check
```

## 📂 Project Structure

```
customer-ms/
├── src/main/java/com/alessandragodoy/customerms/
│   ├── api/                    # Generated API interfaces
│   │   ├── CustomerApi.java
│   │   └── internal/
│   │       └── InternalCustomerApi.java
│   ├── controller/             # API implementations
│   │   ├── CustomerController.java
│   │   ├── InternalCustomerController.java
│   ├── dto/                   # Data Transfer Objects
│   ├── service/               # Business logic
│   │   ├── ICustomerService.java
│   │   └── impl/
│   ├── model/                 # JPA entities
│   ├── repository/            # Data access layer
│   ├── adapter/              # External service clients
│   ├── exception/            # Custom exceptions
│   └── utility/              # Helper classes
├── src/main/resources/
│   ├── openapi/              # API contracts
│   │   ├── customer-api.yml
│   │   └── internal-customer-api.yml
│   └── application.properties
└── pom.xml
```

## 🔗 Integration

### Microservice Communication

This service integrates with:
- **Account Microservice**: Validates if customers have active accounts before deactivation
- Communicates via REST API using `RestTemplate`

## 📊 Code Quality Metrics

- **Test Coverage**: Minimum 70% line and instruction coverage (enforced by JaCoCo)
- **Code Style**: Google Java Style Guide (enforced by Checkstyle)
- **Excluded from Coverage**: Configuration, DTOs, generated code, exceptions

## 🎓 Technical Highlights

- **Contract-First Development**: API specifications drive implementation
- **Clean Architecture**: Clear separation of concerns across layers
- **Type Safety**: Compile-time validation of API contracts
- **Functional Programming**: Immutable DTOs and functional validation
- **Comprehensive Testing**: Unit tests with Mockito, 70% coverage minimum
- **Code Quality**: Automated checks with Checkstyle and JaCoCo
- **RESTful Best Practices**: Proper HTTP methods, status codes, and resource naming

## 📫 Contact

**Alessandra Godoy**
- Email: api@alessandragodoy.com
