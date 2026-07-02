package com.grocery.store.api.services;

import com.grocery.store.api.client.ApiClient;
import com.grocery.store.api.client.ApiRoutes;
import com.grocery.store.api.client.ResponseWrapper;
import com.grocery.store.api.models.request.ClientRequest;
import io.restassured.http.Method;

import java.util.Map;

public class ClientService {

    private static final String BASE_PATH = ApiRoutes.CLIENTS;

    public ResponseWrapper createClient(ClientRequest request) {

        return new ResponseWrapper(
                ApiClient.request(Method.POST, BASE_PATH, request, null)
        );
    }

    public ResponseWrapper createClient(ClientRequest request, String token) {

        return new ResponseWrapper(
                ApiClient.request(
                        Method.POST,
                        BASE_PATH,
                        request,
                        Map.of("Authorization", "Bearer " + token)
                )
        );
    }
}