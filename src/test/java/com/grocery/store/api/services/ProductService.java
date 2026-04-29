package com.grocery.store.api.services;

import com.grocery.store.api.client.ApiClient;
import io.restassured.response.Response;

public class ProductService {

    public Response getAllProducts() {
        return ApiClient.get("/products");
    }
}