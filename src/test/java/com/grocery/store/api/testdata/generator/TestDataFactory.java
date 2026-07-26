package com.grocery.store.api.testdata.generator;

import com.grocery.store.api.models.request.OrderRequest;

public class TestDataFactory {

    private TestDataFactory() {}

    // =========================
    // CLIENT DATA
    // =========================

    public static String clientName() {
        return "AutoUser_" + System.currentTimeMillis();
    }

    public static String clientEmail() {
        return "user" + System.currentTimeMillis() + "@test.com";
    }

    // =========================
    // CART DATA
    // =========================

    public static int defaultQuantity() {
        return 1;
    }

    // =========================
    // ORDER DATA
    // =========================

    public static String customerName() {
        return "Customer_" + System.currentTimeMillis();
    }

    public static OrderRequest validOrder(String cartId) {
        OrderRequest request = new OrderRequest();
        request.setCartId(cartId);
        request.setCustomerName(customerName());
        return request;
    }
}