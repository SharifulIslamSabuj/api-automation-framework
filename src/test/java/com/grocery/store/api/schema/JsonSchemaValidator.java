package com.grocery.store.api.schema;

import java.io.InputStream;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsonSchemaValidator {

    public static void validate(String responseBody, String schemaFile) {

        InputStream schemaStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("schemas/" + schemaFile);

        if (schemaStream == null) {
            throw new RuntimeException("Schema file not found: " + schemaFile);
        }

        assertThat(responseBody, matchesJsonSchema(schemaStream));
    }
}