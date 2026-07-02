package com.grocery.store.api.testdata.generator;

import com.grocery.store.api.models.request.ClientRequest;
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

    public static ClientRequest validClient() {
        ClientRequest request = new ClientRequest();
        long timestamp = System.currentTimeMillis();
        request.setClientName("AutoUser_" + timestamp);
        request.setClientEmail("user" + timestamp + "@test.com");
        return request;
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
        long timestamp = System.currentTimeMillis();
        request.setCartId(cartId);
        request.setCustomerName("Customer_" + timestamp);
        return request;
    }

    // =========================
    // GENERAL
    // =========================

    public static int randomProductId() {
        return 1225; // fallback controlled test data (can improve later)
    }
}