package com.grocery.store.api.services;

import com.grocery.store.api.client.ApiClient;
import com.grocery.store.api.client.ApiRoutes;
import com.grocery.store.api.client.ResponseWrapper;
import io.restassured.http.Method;

import java.util.List;
import java.util.Map;

public class ProductService {

    private static final String BASE_PATH = ApiRoutes.PRODUCTS;

    public ResponseWrapper getAllProducts() {

        return new ResponseWrapper(
                ApiClient.request(Method.GET, BASE_PATH, null, null)
        );
    }

    public ResponseWrapper getProduct(int productId) {

        return new ResponseWrapper(
                ApiClient.get(BASE_PATH + "/" + productId)
        );
    }

    public ResponseWrapper getProductsByCategory(String category) {

        return new ResponseWrapper(
                ApiClient.get(BASE_PATH + "?category=" + category)
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