package com.grocery.store.api.testdata.builder;

import com.grocery.store.api.models.request.ClientRequest;

public class ClientRequestBuilder {

    private String clientName;
    private String clientEmail;

    public ClientRequestBuilder setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public ClientRequestBuilder setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
        return this;
    }

    public ClientRequest build() {
        ClientRequest request = new ClientRequest();
        request.setClientName(clientName);
        request.setClientEmail(clientEmail);
        return request;
    }
}