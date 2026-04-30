package com.grocery.store.api.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private ApiClient() {}

    public static Response get(String endpoint) {

        return given()
                .spec(RequestSpecFactory.getSpec())
                .filter(new AllureRestAssured())
                .when()
                .get(endpoint);
    }

    public static Response post(String endpoint, Object body) {

        if (body == null) {
            return given()
                    .spec(RequestSpecFactory.getSpec())
                    .filter(new AllureRestAssured())
                    .when()
                    .post(endpoint);
        }

        return given()
                .spec(RequestSpecFactory.getSpec())
                .filter(new AllureRestAssured())
                .body(body)
                .when()
                .post(endpoint);
    }

    public static Response postWithAuth(String endpoint, Object body, String token) {

        if (body == null) {
            return given()
                    .spec(RequestSpecFactory.getSpec())
                    .filter(new AllureRestAssured())
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .post(endpoint);
        }

        return given()
                .spec(RequestSpecFactory.getSpec())
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post(endpoint);
    }

    public static Response getWithAuth(String endpoint, String token) {

        return given()
                .spec(RequestSpecFactory.getSpec())
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + token)
                .when()
                .get(endpoint);
    }

    public static Response deleteWithAuth(String endpoint, String token) {

        return given()
                .spec(RequestSpecFactory.getSpec())
                .filter(new AllureRestAssured())
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(endpoint);
    }
}