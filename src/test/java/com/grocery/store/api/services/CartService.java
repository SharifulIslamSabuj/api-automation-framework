package com.grocery.store.api.services;

import com.grocery.store.api.models.request.CartRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import com.grocery.store.api.client.RequestBuilder;

public class CartService {

    public Response createCart() {
        return given()
                .spec(RequestBuilder.getSpec())
                .when()
                .post("/carts");
    }

    public Response addItemToCart(String cartId, CartRequest request) {
        return given()
                .spec(RequestBuilder.getSpec())
                .body(request)
                .when()
                .post("/carts/" + cartId + "/items");
    }
}