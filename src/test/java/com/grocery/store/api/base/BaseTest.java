package com.grocery.store.api.base;

import com.grocery.store.api.client.ResponseWrapper;
import com.grocery.store.api.config.ConfigManager;
import com.grocery.store.api.utils.AssertionUtil;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {

        // Set Base URI globally for all API tests
        RestAssured.baseURI = ConfigManager.getBaseUrl();

        System.out.println("BaseTest Setup Completed");
        System.out.println("Base URI: " + RestAssured.baseURI);
    }

    protected void validate(ResponseWrapper response, String schema) {
        response.validateSchema(schema);
    }

    protected void assertStatus(ResponseWrapper response, int expected) {
        AssertionUtil.assertStatusCode(response, expected);
    }
}