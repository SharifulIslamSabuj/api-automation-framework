package com.grocery.store.api.client;

import com.grocery.store.api.base.BaseTest;
import io.restassured.specification.RequestSpecification;

public class RequestBuilder {

    private RequestBuilder() {}

    public static RequestSpecification getSpec() {
        return new BaseTest().getRequestSpec();
    }
}