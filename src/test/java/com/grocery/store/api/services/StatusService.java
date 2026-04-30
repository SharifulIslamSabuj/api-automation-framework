package com.grocery.store.api.services;

import com.grocery.store.api.client.ApiClient;
import io.restassured.response.Response;

public class StatusService {

    private static final String BASE_PATH = "/status";

    public Response getStatus() {
        return ApiClient.get(BASE_PATH);
    }
}