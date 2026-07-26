package com.grocery.store.api.services;

import com.grocery.store.api.client.ApiClient;
import com.grocery.store.api.client.ApiRoutes;
import com.grocery.store.api.client.ResponseWrapper;
import com.grocery.store.api.models.request.OrderRequest;

public class OrderService {

    private static final String BASE_PATH = ApiRoutes.ORDERS;

    public ResponseWrapper placeOrder(OrderRequest request, String token) {

        return new ResponseWrapper(
                ApiClient.postWithAuth(
                        BASE_PATH,
                        request,
                        token
                )
        );
    }

    public ResponseWrapper placeOrder(OrderRequest request) {

        return new ResponseWrapper(
                ApiClient.post(BASE_PATH, request)
        );
    }

    public ResponseWrapper getOrder(String orderId, String token) {

        return new ResponseWrapper(
                ApiClient.getWithAuth(BASE_PATH + "/" + orderId, token)
        );
    }

    public ResponseWrapper deleteOrder(String orderId, String token) {

        return new ResponseWrapper(
                ApiClient.deleteWithAuth(BASE_PATH + "/" + orderId, token)
        );
    }
}