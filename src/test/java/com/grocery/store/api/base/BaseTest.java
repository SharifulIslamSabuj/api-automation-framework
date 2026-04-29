package com.grocery.store.api.base;

import com.grocery.store.api.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseTest {

    public RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.get("base.url"))
                .setContentType("application/json")
                .addFilter(new AllureRestAssured())
                .build();
    }
}