package com.grocery.store.api.tests.api;

import com.grocery.store.api.base.BaseTest;
import com.grocery.store.api.services.StatusService;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class StatusApiTest extends BaseTest {

    private final StatusService statusService = new StatusService();

    @Test(
            description = "Verify API health status is UP",
            groups = {"smoke"}
    )
    public void getStatusShouldReturnUp() {

        statusService.getStatus()
                .then()
                .statusCode(200)
                .body("status", Matchers.equalTo("UP"));
    }
}