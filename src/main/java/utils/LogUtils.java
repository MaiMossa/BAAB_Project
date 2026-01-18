package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {

    // Path where log files are stored
    public static String LOGS_PATH = "test-outputs/Logs";

    // Private constructor to prevent instantiation
    private LogUtils() {
        super();
    }

    /**
     * Retrieves a logger for the calling class.
     *
     * This method uses the stack trace to dynamically determine
     * the class that called the logging method, so logs are
     * correctly categorized.
     *
     * @return a Logger instance from Log4j
     */
    private static Logger logger() {
        return LogManager.getLogger(Thread.currentThread().getStackTrace()[3].getClassName());
    }

    /**
     * Logs a message at TRACE level.
     *
     * TRACE is usually used for very detailed debugging information.
     *
     * @param message the message parts to log
     */
    public static void trace(String... message) {
        logger().trace(String.join(" ", message));
    }

    /**
     * Logs a message at DEBUG level.
     *
     * DEBUG is useful for debugging information that is less detailed than TRACE.
     *
     * @param message the message parts to log
     */
    public static void debug(String... message) {
        logger().debug(String.join(" ", message));
    }

    /**
     * Logs a message at INFO level.
     *
     * INFO level is used for general runtime events or steps in test execution.
     *
     * @param message the message parts to log
     */
    public static void info(String... message) {
        logger().info(String.join(" ", message));
    }

    /**
     * Logs a message at WARN level.
     *
     * WARN indicates a potential problem or unusual situation that does not stop execution.
     *
     * @param message the message parts to log
     */
    public static void warn(String... message) {
        logger().warn(String.join(" ", message));
    }

    /**
     * Logs a message at ERROR level.
     *
     * ERROR indicates a failure in a test step or a critical issue in execution.
     *
     * @param message the message parts to log
     */
    public static void error(String... message) {
        logger().error(String.join(" ", message));
    }

    /**
     * Logs a message at FATAL level.
     *
     * FATAL indicates a very severe error that might cause the program to terminate.
     *
     * @param message the message parts to log
     */
    public static void fatal(String... message) {
        logger().fatal(String.join(" ", message));
    }
}
