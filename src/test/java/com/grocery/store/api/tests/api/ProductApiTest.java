package com.grocery.store.api.tests.api;

import com.grocery.store.api.services.ProductService;
import org.testng.annotations.Test;

public class ProductApiTest {

    private final ProductService productService = new ProductService();

    @Test(groups = {"smoke", "regression"})
    public void getAllProductsShouldSucceed() {

        productService.getAllProducts()
                .then()
                .statusCode(200);
    }
}