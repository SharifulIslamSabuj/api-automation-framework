package com.grocery.store.api.tests.api;

import com.grocery.store.api.base.BaseTest;
import com.grocery.store.api.models.request.ClientRequest;
import com.grocery.store.api.services.ClientService;
import com.grocery.store.api.testdata.generator.TestDataFactory;
import com.grocery.store.api.utils.AssertionUtil;
import com.grocery.store.api.utils.TokenManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ClientApiTest extends BaseTest {

    private final ClientService clientService = new ClientService();

    @Test(groups = {"regression"})
    public void createApiClientShouldSucceed() {

        ClientRequest request = new ClientRequest();
        request.setClientName(TestDataFactory.clientName());
        request.setClientEmail(TestDataFactory.clientEmail());

        var response = clientService.createClient(request, TokenManager.getToken());

        assertStatus(response, 201);

        String token = response.getString("accessToken");

        AssertionUtil.assertNotEmpty(token, "accessToken");
    }

    @Test(groups = {"regression"})
    public void registerDuplicateClientShouldBeRejected() {

        ClientRequest request = new ClientRequest();
        request.setClientName(TestDataFactory.clientName());
        request.setClientEmail(TestDataFactory.clientEmail());

        clientService.createClient(request);
        var duplicateResponse = clientService.createClient(request);

        assertStatus(duplicateResponse, 409);
        Assert.assertTrue(
                duplicateResponse.getString("error").contains("already registered"),
                "Error message should indicate the client is already registered"
        );
    }
}