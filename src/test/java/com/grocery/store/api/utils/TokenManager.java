package com.grocery.store.api.utils;

import com.grocery.store.api.models.request.ClientRequest;
import com.grocery.store.api.services.ClientService;
import com.grocery.store.api.testdata.generator.TestDataFactory;

public class TokenManager {

    private static String token;
    private static final ClientService clientService = new ClientService();

    private TokenManager() {}

    public static synchronized String getToken() {

        if (token == null) {
            token = generateToken();
        }

        return token;
    }

    private static String generateToken() {

        ClientRequest request = new ClientRequest();
        request.setClientName(TestDataFactory.clientName());
        request.setClientEmail(TestDataFactory.clientEmail());

        var response = clientService.createClient(request);
        String accessToken = response.getString("accessToken");

        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException(
                    "Token bootstrap failed: no accessToken in response (status " + response.getStatusCode() + ")"
            );
        }

        return accessToken;
    }
}