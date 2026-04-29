package com.grocery.store.api.tests.api;

import com.grocery.store.api.client.ApiClient;
import com.grocery.store.api.models.request.CartRequest;
import com.grocery.store.api.models.request.OrderRequest;
import com.grocery.store.api.services.CartService;
import com.grocery.store.api.services.ProductService;
import com.grocery.store.api.utils.TokenManager;
import com.thedeanda.lorem.LoremIpsum;
import org.testng.annotations.Test;

public class OrderApiTest {

    ProductService productService = new ProductService();
    CartService cartService = new CartService();

    // ✅ Helper method (VERY IMPORTANT – avoids test dependency)
    private String createOrder() {

        int productId = productService.getAllProducts()
                .then()
                .extract()
                .jsonPath()
                .getInt("[0].id");

        String cartId = cartService.createCart()
                .then()
                .extract()
                .jsonPath()
                .getString("cartId");

        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(productId);

        cartService.addItemToCart(cartId, cartRequest)
                .then()
                .statusCode(201);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCartId(cartId);
        orderRequest.setCustomerName(LoremIpsum.getInstance().getName());

        return ApiClient.postWithAuth("/orders", orderRequest, TokenManager.getToken())
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getString("orderId");
    }

    @Test(description = "Verify order can be created")
    public void createOrderShouldSucceed() {

        createOrder(); // already asserts internally
    }

    @Test(description = "Verify orders list can be retrieved")
    public void getOrdersShouldSucceed() {

        ApiClient.getWithAuth("/orders", TokenManager.getToken())
                .then()
                .statusCode(200);
    }

    @Test(description = "Verify order details can be retrieved")
    public void getOrderByIdShouldSucceed() {

        String orderId = createOrder();

        ApiClient.getWithAuth("/orders/" + orderId, TokenManager.getToken())
                .then()
                .statusCode(200);
    }

    @Test(description = "Verify order can be deleted")
    public void deleteOrderShouldSucceed() {

        String orderId = createOrder();

        ApiClient.deleteWithAuth("/orders/" + orderId, TokenManager.getToken())
                .then()
                .statusCode(204);
    }
}