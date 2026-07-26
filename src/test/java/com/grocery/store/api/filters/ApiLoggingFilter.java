package com.grocery.store.api.filters;

import com.grocery.store.api.observability.ObservabilityManager;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

/**
 * Enterprise API Logging Filter (Grocery Store Framework)
 *
 * Responsibility:
 * - Capture HTTP request/response
 * - Measure response time
 * - Send logs to ObservabilityManager
 * - Attach details to Allure report
 */
public class ApiLoggingFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        // =========================
        // REQUEST DETAILS
        // =========================
        String method = requestSpec.getMethod();
        String uri = requestSpec.getURI();

        ObservabilityManager.logRequest(method, uri);

        // =========================
        // EXECUTION TIMER START
        // =========================
        long startTime = System.currentTimeMillis();

        Response response = ctx.next(requestSpec, responseSpec);

        long responseTime = System.currentTimeMillis() - startTime;

        // =========================
        // RESPONSE DETAILS
        // =========================
        int statusCode = response.getStatusCode();

        ObservabilityManager.logResponse(statusCode, responseTime);

        // =========================
        // FAILURE CLASSIFICATION
        // (sole owner of client/server error classification logging;
        //  ApiClient's retry loop uses status codes for retry decisions only)
        // =========================
        if (statusCode >= 400 && statusCode < 500) {
            ObservabilityManager.logClientError(uri, statusCode);
        }

        if (statusCode >= 500) {
            ObservabilityManager.logServerError(uri, statusCode);
        }

        if (response.getBody().asString().isEmpty()) {
            ObservabilityManager.logValidationError("Empty response body for: " + uri);
        }

        return response;
    }
}