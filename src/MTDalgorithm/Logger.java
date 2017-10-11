package MTDalgorithm;

/**
 * class for logging
 */
public class Logger {
    public static final boolean LOG_ON = true;

    /**
     * logs a message if the logging is on
     * @param message the message to print
     */
    public static void log(String message) {
        if (LOG_ON) {
            System.out.println(message);
        }
    }
}
