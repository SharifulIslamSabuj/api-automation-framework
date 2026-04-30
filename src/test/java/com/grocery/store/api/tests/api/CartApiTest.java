package com.grocery.store.api.tests.api;

import com.grocery.store.api.services.CartService;
import com.grocery.store.api.services.ProductService;
import com.grocery.store.api.client.ResponseSpecFactory;
import com.grocery.store.api.utils.AssertionUtil;
import org.testng.annotations.Test;

public class CartApiTest {

    private final CartService cartService = new CartService();
    private final ProductService productService = new ProductService();

    @Test(
            description = "Verify cart can be created with product",
            groups = {"smoke", "regression"}
    )
    public void createCartShouldSucceed() {

        // Step 1: Get product ID
        int productId = productService.getFirstProductId();

        // Step 2: Create empty cart and extract cartId
        String cartId = cartService.createEmptyCart()
                .then()
                .spec(ResponseSpecFactory.expect201())
                .extract()
                .jsonPath()
                .getString("cartId");

        // Step 3: Validate cartId
        AssertionUtil.assertNotNull(cartId, "Cart ID should not be null");
        AssertionUtil.assertNotEmpty(cartId, "Cart ID should not be empty");

        // Step 4: Add product to cart and validate response
        cartService.addProductToCart(cartId, productId)
                .then()
                .spec(ResponseSpecFactory.expect201());
    }
}