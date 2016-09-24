package dev.gsitgithub.webapp.config.utils;

public class ApplicationEnvironmentUtils {

    public static final String LOCALHOST = "localhost";
    public static final String DEVELOPMENT = "development";
    public static final String TEST = "test";
    public static final String PRODUCTION = "production";
    private static String APPLICATION_ENVIRONMENT = null;

    public static String getEnvironment() {
        if (APPLICATION_ENVIRONMENT == null) {
            APPLICATION_ENVIRONMENT = System.getProperty("application.environment");
        }
        return APPLICATION_ENVIRONMENT;
    }

    public static boolean isLocalhostEnvironment() {
        return LOCALHOST.equals(getEnvironment());
    }

    public static boolean isDevelopmentEnvironment() {
        return DEVELOPMENT.equals(getEnvironment());
    }

    public static boolean isTestEnvironment() {
        return TEST.equals(getEnvironment());
    }

    public static boolean isProductionEnvironment() {
        return PRODUCTION.equals(getEnvironment());
    }
}
