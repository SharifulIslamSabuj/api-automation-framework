package com.grocery.store.api.config;

import io.qameta.allure.restassured.AllureRestAssured;

public class AllureConfig {

    public static AllureRestAssured getFilter() {
        return new AllureRestAssured()
                .setRequestTemplate("request.ftl")
                .setResponseTemplate("response.ftl");
    }
}