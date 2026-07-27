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

                // Client/server error classification logging is owned solely by
                // ApiLoggingFilter, which observes every attempt uniformly.
                // Status is inspected here only to drive retry decisions.
                if (status < 500) {
                    return response;
                }

                boolean transientStatus = status == 502 || status == 503 || status == 504;

                if (transientStatus && attempt < maxRetry) {
                    ObservabilityManager.logRetry(method.name(), endpoint, "HTTP " + status, attempt, maxRetry, retryDelay);
                    if (!sleep(retryDelay)) {
                        // Interrupted during the retry delay: abort the retry loop and
                        // hand back the last response rather than retrying silently.
                        return response;
                    }
                    continue;
                }

                return response;

            } catch (Exception e) {

                ObservabilityManager.logNetworkError(endpoint, e.getMessage());

                if (attempt < maxRetry) {
                    ObservabilityManager.logRetry(method.name(), endpoint, e.getClass().getSimpleName(), attempt, maxRetry, retryDelay);
                    if (!sleep(retryDelay)) {
                        throw new RuntimeException(
                                "API request interrupted during retry delay: " + method + " " + endpoint, e
                        );
                    }
                    continue;
                }

                throw new RuntimeException(
                        "API request failed after " + (attempt + 1) + " attempt(s): " + method + " " + endpoint, e
                );
            }
        }

        throw new RuntimeException("API request failed: " + method + " " + endpoint);
    }

    /**
     * @return true if the delay completed normally; false if interrupted,
     *         in which case the interrupt flag is restored and the caller
     *         must abort rather than continue retrying.
     */
    private static boolean sleep(long millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
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

    public static Response delete(String endpoint) {
        return request(Method.DELETE, endpoint, null, null);
    }

    public static Response getWithAuth(String endpoint, String token) {
        return request(Method.GET, endpoint, null,
                Map.of("Authorization", "Bearer " + token));
    }

    public static Response deleteWithAuth(String endpoint, String token) {
        return request(Method.DELETE, endpoint, null,
                Map.of("Authorization", "Bearer " + token));
    }
}