package com.grocery.store.api.execution;

public final class ExecutionController {

    private ExecutionController() {}

    public static String getEnv() {
        return System.getProperty("env", "dev");
    }

    public static String getGroups() {
        return System.getProperty("groups", "all");
    }

    public static boolean isCI() {
        String ciProp = System.getProperty("CI", "false");
        String ciEnv = System.getenv("CI");
        return "true".equalsIgnoreCase(ciProp) || "true".equalsIgnoreCase(ciEnv);
    }

    public static boolean isSmoke() {
        String groups = getGroups().toLowerCase();
        return groups.contains("smoke") || groups.equals("all") || groups.isEmpty();
    }

    public static boolean isRegression() {
        String groups = getGroups().toLowerCase();
        return groups.contains("regression") || groups.equals("all") || groups.isEmpty();
    }

    public static boolean isIntegration() {
        String groups = getGroups().toLowerCase();
        if (isCI() && !groups.contains("integration")) {
            return false;
        }
        return groups.contains("integration") || groups.equals("all") || groups.isEmpty();
    }
}