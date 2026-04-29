package com.grocery.store.api.tests.api;

import com.grocery.store.api.services.ProductService;
import org.testng.annotations.Test;

public class ProductApiTest {

    ProductService productService = new ProductService();

    @Test(description = "Verify all products can be retrieved")
    public void getAllProductsShouldSucceed() {

        productService.getAllProducts()
                .then()
                .statusCode(200);
    }
}