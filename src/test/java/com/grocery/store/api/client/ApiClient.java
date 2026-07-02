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

        int maxRetry = ConfigManager.getRetryCount();
        int retryDelay = ConfigManager.getRetryDelay();

        Response lastResponse = null;

        for (int attempt = 0; attempt <= maxRetry; attempt++) {

            try {

                var req = given()
                        .spec(RequestSpecFactory.getSpec());

                if (headers != null) req.headers(headers);
                if (body != null) req.body(body);

                Response response = req.request(method, endpoint);
                lastResponse = response;

                int status = response.getStatusCode();

                if (status < 500) {
                    if (status >= 400) {
                        ObservabilityManager.logClientError(endpoint, status);
                    }
                    return response;
                }

                ObservabilityManager.logServerError(endpoint, status);

                Thread.sleep(retryDelay);

            } catch (Exception e) {

                ObservabilityManager.logNetworkError(endpoint, e.getMessage());

                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        return lastResponse;
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