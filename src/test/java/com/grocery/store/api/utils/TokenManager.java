package com.grocery.store.api.utils;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

import com.grocery.store.api.client.RequestBuilder;
import com.thedeanda.lorem.LoremIpsum;

public class TokenManager {

    private static String token;

    private TokenManager() {
        // prevent instantiation
    }

    public static synchronized String getToken() {
        if (token == null) {
            token = generateToken();
        }
        return token;
    }

    private static String generateToken() {

        Map<String, String> client = new HashMap<>();
        client.put("clientName", LoremIpsum.getInstance().getName());
        client.put("clientEmail", LoremIpsum.getInstance().getEmail());

        Response response = given()
                .spec(RequestBuilder.getSpec())
                .body(client)
                .when()
                .post("/api-clients");

        return response.jsonPath().getString("accessToken");
    }
}