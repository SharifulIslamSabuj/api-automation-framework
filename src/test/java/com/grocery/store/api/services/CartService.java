package com.grocery.store.api.services;

import com.grocery.store.api.client.ApiClient;
import com.grocery.store.api.models.request.CartRequest;
import io.restassured.response.Response;

public class CartService {

    private static final String BASE_PATH = "/carts";

    // Create empty cart
    public Response createEmptyCart() {
        return ApiClient.post(BASE_PATH, null);
    }

    // Add product to cart
    public Response addProductToCart(String cartId, int productId) {

        CartRequest request = new CartRequest();
        request.setProductId(productId);

        return ApiClient.post(BASE_PATH + "/" + cartId + "/items", request);
    }
}