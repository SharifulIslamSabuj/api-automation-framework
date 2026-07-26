package com.grocery.store.api.utils;

import com.grocery.store.api.client.ResponseWrapper;

public class AssertionUtil {

    private AssertionUtil() {}

    // =========================
    // STATUS CODE ASSERTION
    // =========================

    public static void assertStatusCode(ResponseWrapper response, int expected) {

        int actual = response.getStatusCode();

        if (actual != expected) {
            throw new AssertionError(
                    "Status mismatch. Expected: " + expected + " but got: " + actual
            );
        }
    }

    // =========================
    // EMPTY ASSERTION
    // =========================

    public static void assertNotEmpty(String value, String fieldName) {

        if (value == null || value.trim().isEmpty()) {
            throw new AssertionError(fieldName + " is EMPTY");
        }
    }
}