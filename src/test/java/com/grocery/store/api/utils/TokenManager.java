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

        String accessToken = clientService.createClient(request)
                .getString("accessToken");

        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("Access token is null/empty");
        }

        return accessToken;
    }
}