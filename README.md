# Grocery Store API Automation Framework

A Java-based REST API test automation framework built with **REST Assured**, **TestNG**, and **Gradle**, validating the public [Simple Grocery Store API](https://simple-grocery-store-api.click) sandbox. The framework demonstrates a layered architecture, configurable retry handling, JSON Schema validation, structured observability logging, and Allure reporting.

---

## Overview

This project exercises the Products, Carts, Orders, API Clients, and Status endpoints of a public grocery-store demo API, covering both isolated API-level tests and a full order-placement end-to-end flow (browse products → create cart → add item → place order).

---

## Features

- Layered architecture (Test → Service → Client) that keeps test logic decoupled from HTTP and endpoint details
- Centralized HTTP client (`ApiClient`) with method- and status-aware retry: only idempotent `GET` requests are retried, and only on transient failures
- JSON Schema validation for cart, product list, and order responses
- Structured, timestamped observability logging for every request, response, error, and retry
- Allure reporting with request/response evidence attached automatically per call
- TestNG group-based execution (`smoke`, `regression`, `e2e`) via dedicated suite files
- Centralized, dynamic test data generation (`TestDataFactory`)
- Token-based authentication handling for secured endpoints (`TokenManager`)
- Single-file Allure HTML report generated automatically after every test run

---

## Tech Stack

| Category | Technology | Version |
|---|---|---|
| Language | Java | 17 |
| Build Tool | Gradle Wrapper | 9.0.0 |
| API Testing | REST Assured | 6.0.0 |
| JSON Handling | REST Assured JSON Path | 6.0.0 |
| Schema Validation | REST Assured JSON Schema Validator | 6.0.0 |
| Test Runner | TestNG | 7.12.0 |
| Reporting | Allure TestNG | 2.32.0 |
| Reporting | Allure REST Assured | 2.32.0 |
| Report Generation | Allure Commandline (external tool) | 2.x |

---

## Framework Architecture

```mermaid
flowchart LR
    subgraph Test Layer
        T[TestNG Test Classes]
    end

    subgraph Service Layer
        S[Service Classes<br/>CartService, ClientService,<br/>OrderService, ProductService, StatusService]
    end

    subgraph Client Layer
        C[ApiClient<br/>retry + error classification]
        R[RequestSpecFactory]
        W[ResponseWrapper]
    end

    subgraph Observability
        L[ApiLoggingFilter]
        O[ObservabilityManager<br/>structured console logs]
        AR[AllureRestAssured<br/>report evidence]
    end

    T --> S --> C
    C --> R
    R --> L
    R --> AR
    C -->|HTTP request/response| API[(Simple Grocery Store API)]
    C --> W --> S
    L --> O
```

**Layer responsibilities**

| Layer | Responsibility |
|---|---|
| Test | TestNG test classes; assertions and schema validation via `BaseTest` |
| Service | One class per resource (`CartService`, `ClientService`, `OrderService`, `ProductService`, `StatusService`); builds requests and calls `ApiClient` |
| Client | `ApiClient` (execution + retry), `RequestSpecFactory` (shared request spec + filters), `ResponseWrapper` (typed response accessors) |
| Model | POJOs for request/response (de)serialization |
| Observability | `ApiLoggingFilter` and `ObservabilityManager` for structured logs; `AllureRestAssured` for report evidence |

---

## Project Structure

```
src/test/java/com/grocery/store/api
├── base
│   └── BaseTest.java                  # Shared assertion/validation helpers
├── client
│   ├── ApiClient.java                 # HTTP execution, retry, error classification
│   ├── ApiRoutes.java                 # Centralized endpoint path constants
│   ├── RequestSpecFactory.java        # Shared RequestSpecification + filter chain
│   └── ResponseWrapper.java           # Typed response accessors
├── config
│   └── ConfigManager.java             # Loads config.properties, resolves environment
├── filters
│   └── ApiLoggingFilter.java          # REST Assured filter for structured request/response logging
├── models
│   ├── request
│   │   ├── ClientRequest.java
│   │   └── OrderRequest.java
│   └── response
│       └── OrderResponse.java
├── observability
│   └── ObservabilityManager.java      # Centralized structured logging
├── schema
│   └── JsonSchemaValidator.java       # JSON Schema validation
├── services
│   ├── CartService.java
│   ├── ClientService.java
│   ├── OrderService.java
│   ├── ProductService.java
│   └── StatusService.java
├── testdata
│   └── generator
│       └── TestDataFactory.java       # Centralized dynamic test data
├── tests
│   ├── api
│   │   ├── CartApiTest.java
│   │   ├── ClientApiTest.java
│   │   ├── ProductApiTest.java
│   │   └── StatusApiTest.java
│   └── e2e
│       └── OrderE2ETest.java
└── utils
    ├── AssertionUtil.java             # Lightweight assertion helpers
    └── TokenManager.java              # Lazy, cached bearer-token provider

src/test/resources
├── allure.properties
├── config.properties
├── schemas
│   ├── cart-schema.json
│   ├── order-schema.json
│   └── products-schema.json
├── testng-e2e.xml
├── testng-regression.xml
└── testng-smoke.xml
```

---

## Execution Flow

```mermaid
flowchart TD
    Start[ApiClient.request] --> Send[Send HTTP request via REST Assured]
    Send --> Status{Status code}
    Status -->|Less than 400| Success[Return response]
    Status -->|400-499| ClientErr[Log client error, return response]
    Status -->|502, 503 or 504| RetryCheck{"GET request and\nretries remaining?"}
    Status -->|Other 5xx| ServerErr[Log server error, return response]
    RetryCheck -->|Yes| WaitRetry[Log retry, wait retry.delay, try again]
    RetryCheck -->|No| ServerErr
    WaitRetry --> Send
    Send -->|Network exception| ExCheck{"GET request and\nretries remaining?"}
    ExCheck -->|Yes| WaitRetry
    ExCheck -->|No| Throw[Throw RuntimeException]
```

---

## Installation

### Prerequisites

Running the tests requires only:

- Java 17

Generating the Allure HTML report additionally requires:

- Allure Commandline, installed and available on `PATH`

Install Allure Commandline with any of the following:

```bash
npm install -g allure-commandline
scoop install allure        # Windows
brew install allure          # macOS
```

> [!NOTE]
> Test execution does not depend on Allure Commandline. TestNG results (`build/test-results`) and Gradle's own HTML report (`build/reports/tests/test/index.html`) are produced regardless.
>
> `generateSingleAllureHtml` is wired as `finalizedBy` on the `test` task, so if Allure Commandline is missing, `./gradlew test` will still execute all tests correctly but the overall command will exit with `BUILD FAILED` when that final reporting step fails. Install Allure Commandline to get a clean `BUILD SUCCESSFUL` and the generated report.

### Clone and verify setup

```bash
git clone https://github.com/<your-username>/api-automation-framework.git
cd api-automation-framework
./gradlew clean test
```

---

## Configuration

Configuration lives in [`src/test/resources/config.properties`](src/test/resources/config.properties):

| Key | Description | Default |
|---|---|---|
| `dev.base.url` | Base URL for the `dev` environment | `https://simple-grocery-store-api.click` |
| `qa.base.url` | Base URL for the `qa` environment | `https://simple-grocery-store-api.click` |
| `prod.base.url` | Base URL for the `prod` environment | `https://simple-grocery-store-api.click` |
| `connection.timeout` | HTTP connection timeout (ms) | `5000` |
| `read.timeout` | HTTP socket read timeout (ms) | `10000` |
| `retry.count` | Additional retry attempts for retryable `GET` failures | `0` |
| `retry.delay` | Delay between retry attempts (ms) | `1000` |

The active environment defaults to `dev` (`ConfigManager`, backed by the `env` system property). The `test` task in `build.gradle` forwards `-Denv` to the test JVM (`systemProperty "env", System.getProperty("env", "dev")`), so `./gradlew test -Denv=qa` does reach `ConfigManager`. Only `dev`, `qa`, and `prod` are supported — any other value (including typos) now fails fast with a clear `IllegalArgumentException` before any request is sent, rather than silently falling back to another profile. All three environment URLs currently point to the same public sandbox API, so environment selection has no visible effect on requests today regardless.

To point at a different base URL entirely without editing `config.properties`, pass `-DbaseUrl=<url>` (also forwarded by `build.gradle`); it takes precedence over the resolved environment profile. This is intended for ad hoc overrides (e.g. a personal fork of the sandbox, or a future CI-provided endpoint) rather than day-to-day use.

No static credentials are required to run this suite. `TokenManager` registers a fresh, uniquely-named API client (`POST /api-clients`) on first use and caches the returned bearer token for the rest of the JVM — there is nothing to configure or provide up front.

---

## Running Tests

```bash
# Run every test in the project
./gradlew clean test

# Run a single test class
./gradlew test --tests "com.grocery.store.api.tests.api.ProductApiTest"

# Run every test in a package
./gradlew test --tests "com.grocery.store.api.tests.api.*"

# Run a specific TestNG suite
./gradlew test -Psuite=smoke
./gradlew test -Psuite=regression
./gradlew test -Psuite=e2e

# Full build (compile, test, generate Allure report)
./gradlew clean build

# Regenerate the Allure HTML report from existing results
./gradlew generateSingleAllureHtml
```

### TestNG Groups & Suites

| Test Class | Groups |
|---|---|
| `CartApiTest` | `smoke`, `regression` |
| `ClientApiTest` | `regression` |
| `ProductApiTest` | `smoke`, `regression` |
| `StatusApiTest` | `smoke` |
| `OrderE2ETest` | `smoke`, `regression`, `e2e` |

| Suite File | Group Filter | Classes Executed |
|---|---|---|
| `testng-smoke.xml` | `smoke` | CartApiTest, ProductApiTest, StatusApiTest, OrderE2ETest |
| `testng-regression.xml` | `regression` | CartApiTest, ClientApiTest, ProductApiTest, OrderE2ETest |
| `testng-e2e.xml` | `e2e` | OrderE2ETest |

---

## Retry Strategy

Retry logic lives in `ApiClient` and is intentionally conservative:

| Condition | Behavior |
|---|---|
| `GET` request returns `502`, `503`, or `504` | Retried up to `retry.count` additional times, waiting `retry.delay` ms between attempts |
| `GET` request throws a network exception (timeout, connection failure) | Retried the same way |
| Any other `5xx` response (e.g. `500`, `501`) | Logged and returned immediately — not retried |
| Any `4xx` response | Logged and returned immediately — never retried |
| `POST` / `PUT` / `PATCH` / `DELETE` requests | Never retried by default, to avoid duplicating non-idempotent operations |
| All retry attempts exhausted due to exceptions | A clear `RuntimeException` is thrown — a failed call never silently returns `null` |

Every retry attempt is logged through `ObservabilityManager.logRetry(...)`, including the attempt number, reason, and delay.

---

## Logging

Two components share logging responsibility, each with a distinct purpose:

- **`ObservabilityManager`** — structured, timestamped console logs (`[REQUEST]`, `[RESPONSE]`, `[CLIENT_ERROR]`, `[SERVER_ERROR]`, `[NETWORK_ERROR]`, `[RETRY]`, `[PROFILE]`) for every request lifecycle event.
- **`ApiLoggingFilter`** — a REST Assured `Filter` that captures method, URI, status code, and response time for every call and routes them through `ObservabilityManager`.

Full request/response payload evidence (headers, body, curl-equivalent command) is handled separately by `AllureRestAssured` and lives in the Allure report rather than the console, avoiding duplicate logging.

---

## Reporting

- Allure results are written to `build/allure-results` (configured via `allure.properties` and reinforced by the Gradle `test` task).
- After every test run, the `generateSingleAllureHtml` task (wired via `finalizedBy`) produces a single-file HTML report at:

  ```
  build/allure-report/GroceryStore-ApiAutomationReport.html
  ```

- Gradle's own default test report is also generated at `build/reports/tests/test/index.html`, independent of Allure.

---

## JSON Schema Validation

| Schema File | Validates | Used In |
|---|---|---|
| `cart-schema.json` | Add-to-cart response | `CartApiTest` |
| `products-schema.json` | Product list response | `ProductApiTest` |
| `order-schema.json` | Order creation response | `OrderE2ETest` |

Validation is performed via `BaseTest.validate(response, schemaFileName)`, which delegates to `JsonSchemaValidator`, backed by REST Assured's `json-schema-validator` module.

---

## Authentication

Some endpoints (placing an order, creating a client with an existing token) require a bearer token. `TokenManager` lazily creates one API client via `POST /api-clients` on first use and caches the returned `accessToken` for the remainder of the test run, using a synchronized accessor so concurrent test threads share a single token instead of creating one each.

---

## Test Data Strategy

`TestDataFactory` centralizes generation of dynamic test data (client names/emails, customer names, cart quantities) using timestamp-based uniqueness, avoiding hardcoded values that could collide across parallel or repeated runs.

---

## Future Enhancements

- CI/CD pipeline via GitHub Actions
- Negative-path / error-response test coverage (4xx scenarios)
- TestNG `IRetryAnalyzer` for automatic re-run of failed tests at the CI level

---

## Author

**Md. Shariful Islam**
Senior QA Automation Engineer
GitHub: [@SharifulIslamSabuj](https://github.com/SharifulIslamSabuj)

---

## License

This project does not yet include a license file. Consider adding an [MIT License](https://choosealicense.com/licenses/mit/) (or another OSI-approved license) before sharing this repository publicly.
