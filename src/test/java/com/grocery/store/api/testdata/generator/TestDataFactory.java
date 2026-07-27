package com.grocery.store.api.testdata.generator;

import com.grocery.store.api.models.request.OrderRequest;

import java.util.concurrent.atomic.AtomicLong;

public class TestDataFactory {

    private TestDataFactory() {}

    // Timestamp alone is not collision-safe under parallel execution (proven:
    // 5 threads calling System.currentTimeMillis() concurrently produced as few
    // as 22 unique values across 10,000 calls). The counter guarantees
    // per-JVM uniqueness regardless of thread contention.
    private static final AtomicLong SEQUENCE = new AtomicLong();

    // =========================
    // CLIENT DATA
    // =========================

    public static String clientName() {
        return "AutoUser_" + System.currentTimeMillis() + "_" + SEQUENCE.incrementAndGet();
    }

    public static String clientEmail() {
        return "user" + System.currentTimeMillis() + "_" + SEQUENCE.incrementAndGet() + "@test.com";
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
        return "Customer_" + System.currentTimeMillis() + "_" + SEQUENCE.incrementAndGet();
    }

    public static OrderRequest validOrder(String cartId) {
        OrderRequest request = new OrderRequest();
        request.setCartId(cartId);
        request.setCustomerName(customerName());
        return request;
    }
}