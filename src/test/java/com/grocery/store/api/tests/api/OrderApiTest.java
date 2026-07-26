package com.grocery.store.api.tests.api;

import com.grocery.store.api.base.BaseTest;
import com.grocery.store.api.models.request.OrderRequest;
import com.grocery.store.api.services.CartService;
import com.grocery.store.api.services.OrderService;
import com.grocery.store.api.services.ProductService;
import com.grocery.store.api.testdata.generator.TestDataFactory;
import com.grocery.store.api.utils.AssertionUtil;
import com.grocery.store.api.utils.TokenManager;
import org.testng.annotations.Test;

public class OrderApiTest extends BaseTest {

    private final ProductService productService = new ProductService();
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();

    @Test(groups = {"regression"})
    public void placeOrderWithoutTokenShouldBeRejected() {

        OrderRequest request = TestDataFactory.validOrder("nonexistent-cart-id");

        var response = orderService.placeOrder(request);

        assertStatus(response, 401);
        AssertionUtil.assertTrue(
                response.getString("error").contains("Missing Authorization header"),
                "Error message should indicate the Authorization header is missing"
        );
    }

    @Test(groups = {"regression"})
    public void placeOrderWithInvalidTokenShouldBeRejected() {

        OrderRequest request = TestDataFactory.validOrder("nonexistent-cart-id");

        var response = orderService.placeOrder(request, "invalid-token");

        assertStatus(response, 401);
        AssertionUtil.assertTrue(
                response.getString("error").contains("Invalid bearer token"),
                "Error message should indicate the bearer token is invalid"
        );
    }

    @Test(groups = {"regression"})
    public void placeOrderWithInvalidCartShouldFail() {

        OrderRequest request = TestDataFactory.validOrder("nonexistent-cart-id");

        var response = orderService.placeOrder(request, TokenManager.getToken());

        assertStatus(response, 400);
        AssertionUtil.assertTrue(
                response.getString("error").contains("Invalid or missing cartId"),
                "Error message should indicate the cartId is invalid"
        );
    }

    @Test(groups = {"regression"})
    public void orderLifecycleShouldSupportRetrievalAndDeletion() {

        int productId = productService.getFirstProductId();
        String cartId = cartService.createEmptyCart().getString("cartId");
        cartService.addProductToCart(cartId, productId);

        String token = TokenManager.getToken();
        OrderRequest request = TestDataFactory.validOrder(cartId);
        String orderId = orderService.placeOrder(request, token).getString("orderId");

        var getResponse = orderService.getOrder(orderId, token);
        assertStatus(getResponse, 200);
        AssertionUtil.assertTrue(
                getResponse.getString("id").equals(orderId),
                "Retrieved order should match the created order"
        );

        var deleteResponse = orderService.deleteOrder(orderId, token);
        assertStatus(deleteResponse, 204);

        var getAfterDelete = orderService.getOrder(orderId, token);
        assertStatus(getAfterDelete, 404);
    }
}
