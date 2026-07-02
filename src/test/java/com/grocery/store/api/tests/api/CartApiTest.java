package com.grocery.store.api.tests.api;

import com.grocery.store.api.base.BaseTest;
import com.grocery.store.api.services.CartService;
import com.grocery.store.api.services.ProductService;
import com.grocery.store.api.utils.AssertionUtil;
import org.testng.annotations.Test;

public class CartApiTest extends BaseTest {

    private final CartService cartService = new CartService();
    private final ProductService productService = new ProductService();

    @Test(groups = {"smoke", "regression"})
    public void createCartShouldSucceed() {

        int productId = productService.getFirstProductId();

        // Create cart
        var cartResponse = cartService.createEmptyCart();

        assertStatus(cartResponse, 201);

        String cartId = cartResponse.getString("cartId");

        AssertionUtil.assertNotNull(cartId, "cartId");
        AssertionUtil.assertNotEmpty(cartId, "cartId");

        // Add product + schema validation
        var addResponse = cartService.addProductToCart(cartId, productId);
        validate(addResponse, "cart-schema.json");
    }
}