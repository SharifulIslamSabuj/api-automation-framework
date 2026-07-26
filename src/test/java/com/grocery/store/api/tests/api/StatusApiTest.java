package com.grocery.store.api.tests.api;

import com.grocery.store.api.base.BaseTest;
import com.grocery.store.api.services.StatusService;
import com.grocery.store.api.utils.AssertionUtil;
import org.testng.annotations.Test;

public class StatusApiTest extends BaseTest {

    private final StatusService statusService = new StatusService();

    @Test(groups = {"smoke"})
    public void getStatusShouldReturnUp() {

        var response = statusService.getStatus();

        assertStatus(response, 200);

        String status = response.getString("status");

        AssertionUtil.assertNotNull(status, "status");

        AssertionUtil.assertTrue(
                "UP".equals(status),
                "Service status should be UP"
        );
    }
}