# 🚀 Grocery Store API Automation Framework

A scalable **API Automation Framework** built using **Java, RestAssured, TestNG, and Gradle**, designed for validating Grocery Store REST APIs with both functional and end-to-end test coverage.

---

# 📌 Tech Stack

- Java 17+
- RestAssured
- TestNG
- Gradle
- Allure Reports
- POJO Serialization

---

# 📁 Project Structure

```
src/test/java/com/grocery/store/api
│
├── base
├── client
├── config
├── models
│   ├── request
│   └── response
├── services
├── tests
│   ├── api
│   └── e2e
└── utils
```

---

# 🧪 Test Types

## 🔹 API Tests
- Product API
- Cart API
- Order API
- Client API
- Status API

## 🔹 E2E Tests
- Full order flow:
  - Get products
  - Create cart
  - Add item
  - Place order

---

# ⚙️ Setup Instructions

## 1. Clone repository

```bash
git clone https://github.com/your-username/api-automation-framework.git
cd api-automation-framework
```

---

## 2. Build project

```bash
./gradlew clean build
```

---

# ▶️ Run Tests

## 🔹 Run all tests

```bash
./gradlew clean test
```

---

## 🔹 Run specific test class

```bash
./gradlew test --tests "com.grocery.store.api.tests.api.OrderApiTest"
```

---

## 🔹 Run package level tests

```bash
./gradlew test --tests "com.grocery.store.api.tests.api.*"
```

---

## 🔹 Run TestNG XML suite

```bash
./gradlew test -Psuite=package
```

Available suites:
- testng-package.xml
- testng-classes.xml

---

# 📊 Allure Report

## Generate report

```bash
./gradlew test
```

## Manual report generation

```bash
./gradlew generateSingleAllureHtml
```

📍 Output:

```
build/allure-report/ErpTestApiAutomationReport.html
```

---

# 🔐 Authentication

- Token generated dynamically via API
- Managed using `TokenManager`
- Used for secured endpoints (Orders API)

---

# 🧠 Architecture

```
Test Layer → Service Layer → Client Layer → API
```

### ✔ Test Layer
TestNG test classes

### ✔ Service Layer
Business logic abstraction (CartService, OrderService)

### ✔ Client Layer
API execution layer (ApiClient)

### ✔ Model Layer
POJOs for request/response mapping

---

# 📦 Key Design Patterns

- Layered Architecture
- Builder Pattern (RequestBuilder)
- Singleton Pattern (TokenManager)
- POJO-based Serialization

---

# 🚀 Features

- Modular architecture
- Reusable service layer
- POJO-based request models
- Centralized API client
- Token-based authentication
- Parallel execution support
- Allure reporting integration
- XML + Gradle execution support

---

# 📈 Future Enhancements

- Retry Analyzer for flaky tests
- Data-driven framework
- CI/CD integration (GitHub Actions)
- Schema validation layer
- Logging framework integration
- Environment-based execution (dev/stage/prod)

---

# 👨‍💻 How to Run (Quick Reference)

```bash
./gradlew clean test
```

```bash
./gradlew test --tests "com.grocery.store.api.tests.api.OrderApiTest"
```

```bash
./gradlew test -Psuite=package
```

---

# 📌 Author

QA Automation Engineer  
Focus: API Automation | Java | RestAssured | Framework Design

---

# ⭐ Notes

This framework is designed for **real-world QA automation practice and portfolio demonstration**, following scalable industry architecture principles.
