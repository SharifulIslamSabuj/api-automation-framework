package com.grocery.store.api.filters;

import com.grocery.store.api.observability.ObservabilityManager;
import io.qameta.allure.Allure;
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
        String requestBody = requestSpec.getBody() != null
                ? requestSpec.getBody().toString()
                : "EMPTY";

        ObservabilityManager.logRequest(method, uri);

        Allure.addAttachment(
                "API REQUEST",
                "METHOD: " + method +
                        "\nURI: " + uri +
                        "\nBODY: " + requestBody
        );

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
        String responseBody = response.getBody().asPrettyString();

        ObservabilityManager.logResponse(statusCode, responseTime);

        // =========================
        // FAILURE CLASSIFICATION
        // =========================
        if (statusCode >= 400 && statusCode < 500) {
            ObservabilityManager.logClientError(uri, statusCode);
        }

        if (statusCode >= 500) {
            ObservabilityManager.logServerError(uri, statusCode);
        }

        if (responseBody == null || responseBody.isEmpty()) {
            ObservabilityManager.logValidationError("Empty response body for: " + uri);
        }

        // =========================
        // ALLURE ATTACHMENTS
        // =========================
        Allure.addAttachment(
                "API RESPONSE",
                "STATUS: " + statusCode +
                        "\nTIME: " + responseTime + " ms" +
                        "\nBODY: " + responseBody
        );

        return response;
    }
}