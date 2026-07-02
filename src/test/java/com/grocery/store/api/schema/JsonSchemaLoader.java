package com.grocery.store.api.schema;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Loads JSON schema files from resources/schemas
 */
public class JsonSchemaLoader {

    private JsonSchemaLoader() {}

    public static String loadSchema(String schemaFileName) {

        String path = "schemas/" + schemaFileName;

        try (InputStream input = JsonSchemaLoader.class
                .getClassLoader()
                .getResourceAsStream(path)) {

            if (input == null) {
                throw new RuntimeException("Schema file not found: " + path);
            }

            return new String(input.readAllBytes(), StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load schema: " + schemaFileName, e);
        }
    }
}