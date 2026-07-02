package com.grocery.store.api.observability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Enterprise Observability Manager (Grocery Store API Framework)
 * Centralized logging + failure classification + execution tracking
 *
 * Production-grade features:
 * - Structured logs
 * - Failure classification
 * - Retry observability support
 * - CI-friendly debugging format
 */
public final class ObservabilityManager {

    private static final Logger logger =
            Logger.getLogger(ObservabilityManager.class.getName());

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private static final String PREFIX = "[OBS]";

    private ObservabilityManager() {}

    // =========================================================
    // TEST LIFECYCLE
    // =========================================================

    public static void logTestStart(String className, String methodName) {
        log("[TEST_START] " + className + "." + methodName);
    }

    public static void logTestEnd(String className, String methodName, String status) {
        log("[TEST_END] " + className + "." + methodName + " | STATUS=" + status);
    }

    // =========================================================
    // EXECUTION FLOW
    // =========================================================

    public static void logRunning(String testName) {
        log("[RUNNING] " + testName);
    }

    public static void logSkipped(String testName, String reason) {
        log("[SKIPPED] " + testName + " | REASON=" + reason);
    }

    public static void logExecutionStrategy(boolean ci,
                                            String groups,
                                            String env,
                                            int total,
                                            int run) {

        log("=====================================================");
        log("EXECUTION STRATEGY");
        log("CI=" + ci);
        log("GROUPS=" + groups);
        log("ENV=" + env);
        log("TOTAL=" + total + " | RUNNING=" + run);
        log("=====================================================");
    }

    // =========================================================
    // API EVENTS
    // =========================================================

    public static void logRequest(String method, String endpoint) {
        log("[REQUEST] " + method + " " + endpoint);
    }

    public static void logResponse(int statusCode, long responseTimeMs) {
        log("[RESPONSE] STATUS=" + statusCode + " | TIME=" + responseTimeMs + "ms");
    }

    // =========================================================
    // FAILURE CLASSIFICATION (ENTERPRISE STANDARD)
    // =========================================================

    public static void logClientError(String endpoint, int statusCode) {
        logger.warning(format("[CLIENT_ERROR]", endpoint, "STATUS=" + statusCode));
    }

    public static void logServerError(String endpoint, int statusCode) {
        logger.severe(format("[SERVER_ERROR]", endpoint, "STATUS=" + statusCode));
    }

    public static void logNetworkError(String endpoint, String error) {
        logger.severe(format("[NETWORK_ERROR]", endpoint, "ERROR=" + error));
    }

    public static void logValidationError(String message) {
        logger.warning("[VALIDATION_ERROR] " + message);
    }

    // =========================================================
    // RETRY OBSERVABILITY (IMPORTANT FOR API CLIENT)
    // =========================================================

    public static void logRetry(String method,
                                String endpoint,
                                String reason,
                                int attempt,
                                int maxRetry,
                                long delayMs) {

        log("[RETRY] "
                + method + " "
                + endpoint
                + " | REASON=" + reason
                + " | ATTEMPT=" + (attempt + 1)
                + "/" + (maxRetry + 1)
                + " | DELAY=" + delayMs + "ms");
    }

    // =========================================================
    // PROFILE / ENV
    // =========================================================

    public static void logProfile(String env, String baseUrl) {
        log("[PROFILE] ENV=" + env + " | BASE_URL=" + baseUrl);
    }

    // =========================================================
    // CORE LOGGER
    // =========================================================

    private static void log(String message) {
        String time = LocalDateTime.now().format(FORMAT);
        logger.info(PREFIX + " " + time + " " + message);
    }

    private static String format(String type, String endpoint, String message) {
        return type + " " + endpoint + " | " + message;
    }
}