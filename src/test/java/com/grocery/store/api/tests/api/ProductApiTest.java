package com.grocery.store.api.tests.api;

import com.grocery.store.api.base.BaseTest;
import com.grocery.store.api.services.ProductService;
import com.grocery.store.api.utils.AssertionUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

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

    @Test(groups = {"regression"})
    public void getProductByIdShouldReturnProduct() {

        int productId = productService.getFirstProductId();

        var response = productService.getProduct(productId);

        assertStatus(response, 200);

        Assert.assertEquals(
                response.getString("id"),
                String.valueOf(productId),
                "Returned product id should match the requested id"
        );
        AssertionUtil.assertNotEmpty(response.getString("name"), "name");
        AssertionUtil.assertNotEmpty(response.getString("category"), "category");
    }

    @Test(groups = {"regression"})
    public void getUnknownProductShouldReturnNotFound() {

        var response = productService.getProduct(999999999);

        assertStatus(response, 404);

        Assert.assertTrue(
                response.getString("error").contains("No product with id"),
                "Error message should indicate the product was not found"
        );
    }

    @Test(groups = {"regression"})
    public void getProductsByCategoryShouldReturnOnlyThatCategory() {

        var response = productService.getProductsByCategory("fresh-produce");

        assertStatus(response, 200);

        List<Map<String, Object>> products = response.asList("$");

        Assert.assertTrue(!products.isEmpty(), "Filtered category result should not be empty");

        boolean allMatchCategory = products.stream()
                .allMatch(p -> "fresh-produce".equals(p.get("category")));

        Assert.assertTrue(allMatchCategory, "All returned products should belong to the requested category");
    }
}