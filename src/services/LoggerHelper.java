package services;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerHelper {
    private static final String LOG_FILE = "application.log";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";

    public static void logInfo(String message) {
        log("INFO", message, ANSI_CYAN);
    }

    public static void logWarning(String message) {
        log("WARNING", message, ANSI_YELLOW);
    }

    public static void logError(String message, Throwable throwable) {
        log("ERROR", message, ANSI_RED);
        logStackTrace(throwable);
    }

    private static void log(String level, String message, String color) {
        String formattedMessage = String.format("[%s] %s%s%s: %s", getCurrentTime(), color, level, ANSI_RESET, message);
        System.out.println(formattedMessage);

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(formattedMessage);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    private static void logStackTrace(Throwable throwable) {
        System.err.println("Stack trace:");
        throwable.printStackTrace(System.err);
    }

    private static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
