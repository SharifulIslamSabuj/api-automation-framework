package com.grocery.store.api.tests.api;

import com.grocery.store.api.client.ApiClient;
import com.thedeanda.lorem.LoremIpsum;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class ClientApiTest {

    @Test(description = "Verify API client can be created")
    public void createApiClientShouldSucceed() {

        Map<String, String> client = new HashMap<>();
        client.put("clientName", LoremIpsum.getInstance().getName());
        client.put("clientEmail", LoremIpsum.getInstance().getEmail());

        ApiClient.post("/api-clients", client)
                .then()
                .statusCode(201);
    }
}