package com.grocery.store.api.tests.e2e;

import com.grocery.store.api.base.BaseTest;
import com.grocery.store.api.models.request.OrderRequest;
import com.grocery.store.api.models.response.OrderResponse;
import com.grocery.store.api.services.CartService;
import com.grocery.store.api.services.OrderService;
import com.grocery.store.api.services.ProductService;
import com.grocery.store.api.client.ResponseSpecFactory;
import com.grocery.store.api.testdata.factory.OrderRequestFactory;
import com.grocery.store.api.utils.AssertionUtil;
import com.grocery.store.api.utils.TokenManager;
import org.testng.annotations.Test;

public class OrderE2ETest extends BaseTest {

    private final ProductService productService = new ProductService();
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();

    @Test(
            description = "Verify order can be created end-to-end",
            groups = {"smoke", "regression", "e2e"}
    )
    public void createOrderShouldSucceed() {

        // Step 1: Get product ID
        int productId = productService.getFirstProductId();

        // Step 2: Create empty cart
        String cartId = cartService.createEmptyCart()
                .then()
                .spec(ResponseSpecFactory.expect201())
                .extract()
                .jsonPath()
                .getString("cartId");

        // Step 3: Add product to cart
        cartService.addProductToCart(cartId, productId)
                .then()
                .spec(ResponseSpecFactory.expect201());

        // Step 4: Build order request
        OrderRequest orderRequest = OrderRequestFactory.defaultOrderRequest(cartId);

        // Step 5: Place order
        OrderResponse response = orderService.placeOrder(orderRequest, TokenManager.getToken())
                .then()
                .spec(ResponseSpecFactory.expect201())
                .extract()
                .as(OrderResponse.class);

        // Step 6: Assertions
        AssertionUtil.assertNotNull(response.getOrderId(), "Order ID should not be null");
        AssertionUtil.assertNotEmpty(response.getOrderId(), "Order ID should not be empty");
    }
}