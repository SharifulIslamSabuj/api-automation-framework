package com.grocery.store.api.tests.api;

import com.grocery.store.api.models.request.CartRequest;
import com.grocery.store.api.services.CartService;
import com.grocery.store.api.services.ProductService;
import org.testng.annotations.Test;

public class CartApiTest {

    CartService cartService = new CartService();
    ProductService productService = new ProductService();

    @Test(description = "Verify cart can be created")
    public void createCartShouldSucceed() {

        cartService.createCart()
                .then()
                .statusCode(201);
    }

    @Test(description = "Verify item can be added to cart")
    public void addItemToCartShouldSucceed() {

        // Get product
        int productId = productService.getAllProducts()
                .then()
                .extract()
                .jsonPath()
                .getInt("[0].id");

        // Create cart
        String cartId = cartService.createCart()
                .then()
                .extract()
                .jsonPath()
                .getString("cartId");

        // Add item
        CartRequest request = new CartRequest();
        request.setProductId(productId);

        cartService.addItemToCart(cartId, request)
                .then()
                .statusCode(201);
    }
}