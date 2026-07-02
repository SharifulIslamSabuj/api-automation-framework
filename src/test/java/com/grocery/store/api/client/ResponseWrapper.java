package com.grocery.store.api.client;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import com.grocery.store.api.schema.JsonSchemaValidator;

import java.util.List;

public class ResponseWrapper {

    private final Response response;

    public ResponseWrapper(Response response) {
        this.response = response;
    }

    // =========================
    // CORE ACCESSORS
    // =========================

    public int getStatusCode() {
        return response.getStatusCode();
    }

    public String getBodyAsString() {
        return response.getBody().asString();
    }

    public String getString(String path) {
        return response.jsonPath().getString(path);
    }

    public JsonPath jsonPath() {
        return response.jsonPath();
    }

    public <T> T as(Class<T> clazz) {
        return response.as(clazz);
    }

    // ⭐ FIX: REQUIRED FOR PRODUCT TEST
    @SuppressWarnings("unchecked")
    public <T> List<T> asList(String path) {
        return response.jsonPath().getList(path);
    }

    // =========================
    // VALIDATION
    // =========================

    public ResponseWrapper validateSchema(String schemaFile) {
        JsonSchemaValidator.validate(response.getBody().asString(), schemaFile);
        return this;
    }

    // =========================
    // RAW ACCESS
    // =========================

    public Response getRawResponse() {
        return response;
    }
}