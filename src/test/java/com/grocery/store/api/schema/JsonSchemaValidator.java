package com.grocery.store.api.schema;

import java.io.InputStream;

public class JsonSchemaValidator {

    public static void validate(String responseBody, String schemaFile) {

        try {
            InputStream schemaStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("schemas/" + schemaFile);

            if (schemaStream == null) {
                throw new RuntimeException("Schema file not found: " + schemaFile);
            }

            io.restassured.module.jsv.JsonSchemaValidator
                    .matchesJsonSchema(schemaStream)
                    .matches(responseBody);

        } catch (Exception e) {
            throw new RuntimeException("Schema validation failed: " + e.getMessage(), e);
        }
    }
}