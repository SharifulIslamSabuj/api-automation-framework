package com.grocery.store.api.testdata.generator;

import java.util.UUID;

public class DataGenerator {

    public static String randomEmail() {
        return "user_" + UUID.randomUUID() + "@test.com";
    }

    public static String randomName() {
        return "User_" + System.currentTimeMillis();
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }
}