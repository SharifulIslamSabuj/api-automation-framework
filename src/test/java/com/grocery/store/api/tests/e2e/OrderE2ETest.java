package com.grocery.store.api.tests.e2e;

import com.grocery.store.api.base.BaseTest;
import com.grocery.store.api.models.request.OrderRequest;
import com.grocery.store.api.models.response.OrderResponse;
import com.grocery.store.api.services.CartService;
import com.grocery.store.api.services.OrderService;
import com.grocery.store.api.services.ProductService;
import com.grocery.store.api.testdata.generator.TestDataFactory;
import com.grocery.store.api.utils.AssertionUtil;
import com.grocery.store.api.utils.TokenManager;
import org.testng.annotations.Test;

public class OrderE2ETest extends BaseTest {

    private final ProductService productService = new ProductService();
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();

    @Test(groups = {"smoke", "regression", "e2e"})
    public void createOrderShouldSucceed() {

        int productId = productService.getFirstProductId();

        // Create cart
        var cartResponse = cartService.createEmptyCart();
        assertStatus(cartResponse, 201);

        String cartId = cartResponse.getString("cartId");

        // Add product
        var addResponse = cartService.addProductToCart(cartId, productId);
        assertStatus(addResponse, 201);

        // Create order (FIXED PART)
        OrderRequest request = new OrderRequest();
        request.setCartId(cartId);
        request.setCustomerName(TestDataFactory.customerName());

        var orderResponse = orderService.placeOrder(
                request,
                TokenManager.getToken()
        );

        assertStatus(orderResponse, 201);

        OrderResponse response = orderResponse.as(OrderResponse.class);

        AssertionUtil.assertNotNull(response.getOrderId(), "orderId");
        AssertionUtil.assertNotEmpty(response.getOrderId(), "orderId");
    }
}