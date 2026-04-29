package com.grocery.store.api.tests.e2e;

import com.grocery.store.api.base.BaseTest;
import com.grocery.store.api.models.request.CartRequest;
import com.grocery.store.api.models.request.OrderRequest;
import com.grocery.store.api.services.CartService;
import com.grocery.store.api.services.OrderService;
import com.grocery.store.api.services.ProductService;
import com.grocery.store.api.utils.TokenManager;
import com.thedeanda.lorem.LoremIpsum;
import org.testng.annotations.Test;

public class GroceryStoreApiTest extends BaseTest {

    ProductService productService = new ProductService();
    CartService cartService = new CartService();
    OrderService orderService = new OrderService();

    @Test
    public void GetAllProductsShouldSucceed() {
        productService.getAllProducts()
                .then()
                .statusCode(200);
    }

    @Test(priority = 0)
    public void placeOrderShouldSucceed() {

        // 1. Get product
        int productId = productService.getAllProducts()
                .then()
                .extract()
                .jsonPath()
                .getInt("[0].id");

        // 2. Create cart
        String cartId = cartService.createCart()
                .then()
                .extract()
                .jsonPath()
                .getString("cartId");

        // 3. Add item to cart (POJO)
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(productId);

        cartService.addItemToCart(cartId, cartRequest)
                .then()
                .statusCode(201);

        // 4. Create order (POJO)
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCartId(cartId);
        orderRequest.setCustomerName(LoremIpsum.getInstance().getName());

        orderService.createOrder(orderRequest, TokenManager.getToken())
                .then()
                .statusCode(201);
    }
}