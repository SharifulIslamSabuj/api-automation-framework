package com.grocery.store.api.services;

import com.grocery.store.api.client.ApiClient;
import io.restassured.response.Response;

public class ProductService {

    private static final String BASE_PATH = "/products";

    public Response getAllProducts() {
        return ApiClient.get(BASE_PATH);
    }

    // BUSINESS ACTION (enterprise addition)
    public int getFirstProductId() {
        return getAllProducts()
                .then()
                .extract()
                .jsonPath()
                .getInt("[0].id");
    }
}