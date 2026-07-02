package com.grocery.store.api.tests.api;

import com.grocery.store.api.base.BaseTest;
import com.grocery.store.api.services.ProductService;
import com.grocery.store.api.utils.AssertionUtil;
import org.testng.annotations.Test;

public class ProductApiTest extends BaseTest {

    private final ProductService productService = new ProductService();

    @Test(groups = {"smoke", "regression"})
    public void getAllProductsShouldSucceed() {

        var response = productService.getAllProducts();

        assertStatus(response, 200);

        AssertionUtil.assertNotEmpty(
                response.getBodyAsString(),
                "products response"
        );

        validate(response, "products-schema.json");
    }
}