package com.grocery.store.api.services;

import com.grocery.store.api.client.ApiClient;
import com.grocery.store.api.models.request.ClientRequest;
import io.restassured.response.Response;

public class ClientService {

    private static final String BASE_PATH = "/api-clients";

    // =========================
    // Create client WITHOUT auth (for token generation)
    // =========================
    public Response createClient(ClientRequest request) {
        return ApiClient.post(BASE_PATH, request);
    }

    // =========================
    // Create client WITH auth (if needed in future scenarios)
    // =========================
    public Response createClient(ClientRequest request, String token) {
        return ApiClient.postWithAuth(BASE_PATH, request, token);
    }
}