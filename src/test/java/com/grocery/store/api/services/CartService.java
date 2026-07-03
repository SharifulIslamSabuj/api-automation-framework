package com.grocery.store.api.services;

import com.grocery.store.api.client.ApiClient;
import com.grocery.store.api.client.ApiRoutes;
import com.grocery.store.api.client.ResponseWrapper;
import com.grocery.store.api.testdata.generator.TestDataFactory;

import java.util.Map;

public class CartService {

    private static final String BASE_PATH = ApiRoutes.CARTS;

    public ResponseWrapper createEmptyCart() {

        return new ResponseWrapper(
                ApiClient.post(BASE_PATH, null)
        );
    }

    public ResponseWrapper addProductToCart(String cartId, int productId) {

        String endpoint = BASE_PATH + "/" + cartId + "/items";

        Map<String, Object> body = Map.of(
                "productId", productId,
                "quantity", TestDataFactory.defaultQuantity()
        );

        return new ResponseWrapper(
                ApiClient.post(endpoint, body)
        );
    }
}