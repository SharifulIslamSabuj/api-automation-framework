package com.grocery.store.api.config;


import com.grocery.store.api.observability.ObservabilityManager;

import java.io.InputStream;
import java.util.Properties;

/**
 * Supports:
 * - Profile-based environment switching (env system property)
 * - Cached base URL resolution
 */
public final class ConfigManager {

    private static final Properties properties = new Properties();

    private static String resolvedProfile;
    private static String resolvedBaseUrl;

    private static final String DEV = "dev.base.url";
    private static final String QA = "qa.base.url";
    private static final String PROD = "prod.base.url";

    private ConfigManager() {}

    static {
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found");
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }

        resolveProfile();
    }

    // =========================
    // PROFILE RESOLUTION
    // =========================

    private static void resolveProfile() {

        String profile = currentEnv();

        resolvedProfile = profile;
        resolvedBaseUrl = resolveBaseUrl(profile);

        ObservabilityManager.logProfile(profile, resolvedBaseUrl);
    }

    private static String resolveBaseUrl(String profile) {

        return switch (profile) {
            case "qa" -> properties.getProperty(QA);
            case "prod" -> properties.getProperty(PROD);
            case "dev" -> properties.getProperty(DEV);
            default -> throw new IllegalArgumentException(
                    "Unsupported environment '" + profile + "'. Supported values: dev, qa, prod."
            );
        };
    }

    // =========================
    // PUBLIC API
    // =========================

    public static String getBaseUrl() {

        String override = System.getProperty("baseUrl", "");
        if (!override.isBlank()) {
            return override;
        }

        if (!currentEnv().equals(resolvedProfile)) {
            resolveProfile();
        }

        return resolvedBaseUrl;
    }

    private static String currentEnv() {
        return System.getProperty("env", "dev");
    }

    // =========================
    // TIMEOUTS
    // =========================

    public static int getConnectionTimeout() {
        return Integer.parseInt(properties.getProperty("connection.timeout", "5000"));
    }

    public static int getReadTimeout() {
        return Integer.parseInt(properties.getProperty("read.timeout", "10000"));
    }

    // =========================
    // RETRY
    // =========================

    public static int getRetryCount() {
        return Integer.parseInt(properties.getProperty("retry.count", "0"));
    }

    public static int getRetryDelay() {
        return Integer.parseInt(properties.getProperty("retry.delay", "1000"));
    }
}