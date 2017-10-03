package algorithm;

public class Logger {
    public static final boolean LOG_ON = true;

    public static void log(String message) {
        if (LOG_ON) {
            System.out.println(message);
        }
    }
}
