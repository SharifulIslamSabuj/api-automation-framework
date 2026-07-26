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

    @Test(groups = {"regression"})
    public void getCartShouldReturnAddedItems() {

        int productId = productService.getFirstProductId();
        String cartId = cartService.createEmptyCart().getString("cartId");

        cartService.addProductToCart(cartId, productId);

        var cartResponse = cartService.getCart(cartId);

        assertStatus(cartResponse, 200);
        AssertionUtil.assertTrue(
                cartResponse.getString("items[0].productId").equals(String.valueOf(productId)),
                "Cart should contain the product that was added"
        );
    }

    @Test(groups = {"regression"})
    public void addProductToInvalidCartShouldReturnNotFound() {

        int productId = productService.getFirstProductId();

        var response = cartService.addProductToCart("nonexistent-cart-id", productId);

        assertStatus(response, 404);
        AssertionUtil.assertTrue(
                response.getString("error").contains("No cart with id"),
                "Error message should indicate the cart was not found"
        );
    }

    @Test(groups = {"regression"})
    public void addDuplicateProductToCartShouldFail() {

        int productId = productService.getFirstProductId();
        String cartId = cartService.createEmptyCart().getString("cartId");

        cartService.addProductToCart(cartId, productId);
        var duplicateResponse = cartService.addProductToCart(cartId, productId);

        assertStatus(duplicateResponse, 400);
        AssertionUtil.assertTrue(
                duplicateResponse.getString("error").contains("already been added"),
                "Error message should indicate the product is already in the cart"
        );
    }

    @Test(groups = {"regression"})
    public void removeProductFromCartShouldSucceed() {

        int productId = productService.getFirstProductId();
        String cartId = cartService.createEmptyCart().getString("cartId");
        String itemId = cartService.addProductToCart(cartId, productId).getString("itemId");

        var removeResponse = cartService.removeProductFromCart(cartId, itemId);
        assertStatus(removeResponse, 204);

        var cartAfterRemoval = cartService.getCart(cartId);
        AssertionUtil.assertTrue(
                cartAfterRemoval.asList("items").isEmpty(),
                "Cart should have no items after removal"
        );
    }
}