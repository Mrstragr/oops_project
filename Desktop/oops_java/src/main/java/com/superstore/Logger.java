package com.superstore;

import java.io.IOException;

/**
 * Custom Logger class for logging exceptions and warnings to a file.
 */
public class Logger {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("SuperstoreLogger");

    static {
        try {
            java.util.logging.FileHandler fh = new java.util.logging.FileHandler("resources/superstore.log", true);
            fh.setFormatter(new java.util.logging.SimpleFormatter());
            logger.addHandler(fh);
            logger.setLevel(java.util.logging.Level.ALL);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    public static void logException(Exception e) {
        logger.log(java.util.logging.Level.SEVERE, "Exception occurred", e);
    }

    public static void logWarning(String message) {
        logger.warning(message);
    }

    public static void logInfo(String message) {
        logger.info(message);
    }
}
