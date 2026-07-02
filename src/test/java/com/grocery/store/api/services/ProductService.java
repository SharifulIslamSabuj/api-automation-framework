package com.grocery.store.api.services;

import com.grocery.store.api.client.ApiClient;
import com.grocery.store.api.client.ResponseWrapper;
import io.restassured.http.Method;

import java.util.List;
import java.util.Map;

public class ProductService {

    private static final String BASE_PATH = "/products";

    public ResponseWrapper getAllProducts() {

        return new ResponseWrapper(
                ApiClient.request(Method.GET, BASE_PATH, null, null)
        );
    }

    public int getFirstProductId() {

        List<Map<String, Object>> products =
                getAllProducts().asList("$");

        return products.stream()
                .findFirst()
                .map(p -> (Integer) p.get("id"))
                .orElseThrow(() -> new RuntimeException("No products found"));
    }
}