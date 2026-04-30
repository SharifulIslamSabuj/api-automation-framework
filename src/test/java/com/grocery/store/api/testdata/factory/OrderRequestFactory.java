package com.grocery.store.api.testdata.factory;

import com.grocery.store.api.models.request.OrderRequest;
import com.grocery.store.api.testdata.generator.DataGenerator;

public class OrderRequestFactory {

    public static OrderRequest defaultOrderRequest(String cartId) {

        OrderRequest request = new OrderRequest();
        request.setCartId(cartId);
        request.setCustomerName(DataGenerator.randomName());

        return request;
    }

    public static OrderRequest customOrderRequest(String cartId, String customerName) {

        OrderRequest request = new OrderRequest();
        request.setCartId(cartId);
        request.setCustomerName(customerName);

        return request;
    }
}