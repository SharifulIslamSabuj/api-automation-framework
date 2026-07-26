package com.grocery.store.api.base;

import com.grocery.store.api.client.ResponseWrapper;
import com.grocery.store.api.utils.AssertionUtil;

public abstract class BaseTest {

    protected void validate(ResponseWrapper response, String schema) {
        response.validateSchema(schema);
    }

    protected void assertStatus(ResponseWrapper response, int expected) {
        AssertionUtil.assertStatusCode(response, expected);
    }
}