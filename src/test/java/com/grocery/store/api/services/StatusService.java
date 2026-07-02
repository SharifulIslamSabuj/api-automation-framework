package com.grocery.store.api.services;

import com.grocery.store.api.client.ApiClient;
import com.grocery.store.api.client.ResponseWrapper;

public class StatusService {

    private static final String BASE_PATH = "/status";

    public ResponseWrapper getStatus() {

        return new ResponseWrapper(
                ApiClient.get(BASE_PATH)
        );
    }
}