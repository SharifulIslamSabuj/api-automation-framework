package com.grocery.store.api.tests.api;

import com.grocery.store.api.base.BaseTest;
import com.grocery.store.api.models.request.ClientRequest;
import com.grocery.store.api.services.ClientService;
import com.grocery.store.api.testdata.builder.ClientRequestBuilder;
import com.grocery.store.api.testdata.generator.DataGenerator;
import com.grocery.store.api.utils.TokenManager;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

@Test(groups = {"regression"})
public class ClientApiTest extends BaseTest {

    private final ClientService clientService = new ClientService();

    @Test(description = "Verify API client can be created")
    public void createApiClientShouldSucceed() {

        ClientRequest request = new ClientRequestBuilder()
                .setClientName(DataGenerator.randomName())
                .setClientEmail(DataGenerator.randomEmail())
                .build();

        clientService.createClient(request, TokenManager.getToken())
                .then()
                .statusCode(201)
                .body("accessToken", Matchers.notNullValue());
    }
}