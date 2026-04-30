package com.grocery.store.api.utils;

import com.grocery.store.api.models.request.ClientRequest;
import com.grocery.store.api.services.ClientService;

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

        long timestamp = System.currentTimeMillis();

        ClientRequest request = new ClientRequest();
        request.setClientName("AutoUser");
        request.setClientEmail("autouser" + timestamp + "@test.com");

        String accessToken = clientService.createClient(request)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getString("accessToken");

        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("Access token is null/empty");
        }

        return accessToken;
    }
}