package com.grocery.store.api.services;

import com.grocery.store.api.models.request.OrderRequest;
import com.grocery.store.api.client.ApiClient;
import io.restassured.response.Response;

public class OrderService {

    public Response createOrder(OrderRequest request, String token) {
        return ApiClient.postWithAuth("/orders", request, token);
    }
}