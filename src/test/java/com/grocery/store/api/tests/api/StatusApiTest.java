package com.grocery.store.api.tests.api;

import com.grocery.store.api.client.ApiClient;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class StatusApiTest {

    @Test(description = "Verify API status is UP")
    public void getStatusShouldReturnUp() {

        ApiClient.get("/status")
                .then()
                .statusCode(200)
                .body("status", equalTo("UP"));
    }
}