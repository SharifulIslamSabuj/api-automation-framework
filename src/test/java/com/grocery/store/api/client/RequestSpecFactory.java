package com.grocery.store.api.client;

import com.grocery.store.api.config.ConfigManager;
import com.grocery.store.api.filters.ApiLoggingFilter;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;

public class RequestSpecFactory {

    private RequestSpecFactory() {}

    public static RequestSpecification getSpec() {

        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getBaseUrl())
                .setContentType("application/json")
                .setConfig(
                        RestAssuredConfig.config()
                                .httpClient(
                                        HttpClientConfig.httpClientConfig()
                                                .setParam("http.connection.timeout", ConfigManager.getConnectionTimeout())
                                                .setParam("http.socket.timeout", ConfigManager.getReadTimeout())
                                )
                )
                .addFilter(new AllureRestAssured())
                .addFilter(new ApiLoggingFilter())
                .build();
    }
}