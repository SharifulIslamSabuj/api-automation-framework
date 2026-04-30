package com.grocery.store.api.services;

import com.grocery.store.api.client.ApiClient;
import com.grocery.store.api.models.request.OrderRequest;
import io.restassured.response.Response;

public class OrderService {

    private static final String BASE_PATH = "/orders";

    // Create order
    public Response placeOrder(OrderRequest request, String token) {
        return ApiClient.postWithAuth(BASE_PATH, request, token);
    }

    // Get all orders
    public Response getOrders(String token) {
        return ApiClient.getWithAuth(BASE_PATH, token);
    }

    // Get order by ID
    public Response getOrderById(String orderId, String token) {
        return ApiClient.getWithAuth(BASE_PATH + "/" + orderId, token);
    }

    // Delete order
    public Response deleteOrder(String orderId, String token) {
        return ApiClient.deleteWithAuth(BASE_PATH + "/" + orderId, token);
    }
}