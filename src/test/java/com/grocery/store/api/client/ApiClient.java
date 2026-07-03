package com.grocery.store.api.client;

import com.grocery.store.api.config.ConfigManager;
import com.grocery.store.api.observability.ObservabilityManager;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public final class ApiClient {

    private ApiClient() {}

    public static Response request(Method method,
                                   String endpoint,
                                   Object body,
                                   Map<String, String> headers) {

        // Only safe (idempotent) GET requests are retried by default.
        boolean retryable = method == Method.GET;
        int maxRetry = retryable ? ConfigManager.getRetryCount() : 0;
        int retryDelay = ConfigManager.getRetryDelay();

        for (int attempt = 0; attempt <= maxRetry; attempt++) {

            try {

                var req = given()
                        .spec(RequestSpecFactory.getSpec());

                if (headers != null) req.headers(headers);
                if (body != null) req.body(body);

                Response response = req.request(method, endpoint);
                int status = response.getStatusCode();

                if (status < 500) {
                    if (status >= 400) {
                        ObservabilityManager.logClientError(endpoint, status);
                    }
                    return response;
                }

                ObservabilityManager.logServerError(endpoint, status);

                boolean transientStatus = status == 502 || status == 503 || status == 504;

                if (transientStatus && attempt < maxRetry) {
                    ObservabilityManager.logRetry(method.name(), endpoint, "HTTP " + status, attempt, maxRetry, retryDelay);
                    sleep(retryDelay);
                    continue;
                }

                return response;

            } catch (Exception e) {

                ObservabilityManager.logNetworkError(endpoint, e.getMessage());

                if (attempt < maxRetry) {
                    ObservabilityManager.logRetry(method.name(), endpoint, e.getClass().getSimpleName(), attempt, maxRetry, retryDelay);
                    sleep(retryDelay);
                    continue;
                }

                throw new RuntimeException(
                        "API request failed after " + (attempt + 1) + " attempt(s): " + method + " " + endpoint, e
                );
            }
        }

        throw new RuntimeException("API request failed: " + method + " " + endpoint);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public static Response get(String endpoint) {
        return request(Method.GET, endpoint, null, null);
    }

    public static Response post(String endpoint, Object body) {
        return request(Method.POST, endpoint, body, null);
    }

    public static Response postWithAuth(String endpoint, Object body, String token) {
        return request(Method.POST, endpoint, body,
                Map.of("Authorization", "Bearer " + token));
    }
}