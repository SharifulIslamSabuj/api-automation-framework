package com.grocery.store.api.observability;

import java.util.logging.Logger;

/**
 * Centralized structured console logging for request/response lifecycle,
 * failure classification, and retry events (Grocery Store API Framework).
 */
public final class ObservabilityManager {

    private static final Logger logger =
            Logger.getLogger(ObservabilityManager.class.getName());

    private static final String PREFIX = "[OBS]";

    private ObservabilityManager() {}

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
        logger.warning(withThread("[VALIDATION_ERROR] " + message));
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
        logger.info(withThread(PREFIX + " " + message));
    }

    private static String format(String type, String endpoint, String message) {
        return withThread(type + " " + endpoint + " | " + message);
    }

    // Gradle's TestNG XML report aggregates all system-err/system-out output for a whole
    // test class into one shared block, so under parallel="classes" execution log lines
    // from concurrently-running classes/methods can appear interleaved in that report.
    // Appending the thread name lets an engineer manually separate one call's request/
    // response/retry lines from another's when reading a jumbled block.
    private static String withThread(String message) {
        return message + " | THREAD=" + Thread.currentThread().getName();
    }
}