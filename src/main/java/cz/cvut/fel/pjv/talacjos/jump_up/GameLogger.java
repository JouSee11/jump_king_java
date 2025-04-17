package cz.cvut.fel.pjv.talacjos.jump_up;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Logger utility for the Jump Up game.
 * This class provides logging functionality with different log levels,
 * can log to console and file, and can be enabled/disabled via command-line arguments.
 * Follows the Singleton pattern to ensure a single instance is used throughout the game.
 */
public class GameLogger {
    private static GameLogger instance;
    private final Logger logger;
    private boolean loggingEnabled = false;
    private Level loggingLevel = Level.INFO;
    private boolean logToFile = false;
    private static final String LOG_FOLDER = "logs";

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private GameLogger() {
        logger = Logger.getLogger("cz.cvut.fel.pjv.talacjos.jump_up");
        // Prevent using parent handlers (to avoid duplicate logging)
        logger.setUseParentHandlers(false);
    }

    /**
     * Returns the singleton instance of the GameLogger.
     *
     * @return The GameLogger instance
     */
    public static synchronized GameLogger getInstance() {
        if (instance == null) {
            instance = new GameLogger();
        }
        return instance;
    }

    /**
     * Initializes the logger based on command-line arguments.
     *
     * @param args Command-line arguments
     */
    public void initialize(String[] args) {
        parseArgs(args);
        configureLogger();

        if (loggingEnabled) {
            info("Logger enabled, level: " + loggingLevel.getName());
        }

    }

    /**
     * Configures the logger with appropriate handlers based on settings.
     */
    private void configureLogger(){
        if (!loggingEnabled) {
            logger.setLevel(Level.OFF);
            return;
        }

        logger.setLevel(loggingLevel);

        // remove existing handlers
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        addConsoleHandler();

        // Add file handler if enabled
        if (logToFile) {
            addFileHandler();
        }

    }

    /**
     * Adds a console handler with custom formatting.
     */
    private void addConsoleHandler() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(loggingLevel);
        consoleHandler.setFormatter(createFormatter());
        logger.addHandler(consoleHandler);
    }

    /**
     * Adds a file handler for logging to files.
     */
    private void addFileHandler() {
        try {
            // Create logs directory if it doesn't exist
            File logDir = new File(LOG_FOLDER);
            if (!logDir.exists() && !logDir.mkdir()) {
                warning("Failed to create log directory: " + LOG_FOLDER);
                return;
            }

            // Create file handler with timestamp in filename
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            FileHandler fileHandler = new FileHandler(LOG_FOLDER + "/game_" + timestamp + ".log");
            fileHandler.setLevel(loggingLevel);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            info("Logging to file: " + LOG_FOLDER + "/game_" + timestamp + ".log");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create log file", e);
        }
    }

    /**
     * Creates a custom formatter for log messages.
     */
    private Formatter createFormatter() {
        return new SimpleFormatter() {
            private static final String FORMAT = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

            @Override
            public synchronized String format(LogRecord record) {
                return String.format(FORMAT,
                        new Date(record.getMillis()),
                        record.getLevel().getLocalizedName(),
                        record.getMessage()
                );
            }
        };
    }

    /**
     * Parses command-line arguments to configure logging.
     * Supported arguments:
     * --log-enable: Enable logging
     * --log-level=LEVEL: Set log level (INFO, WARNING, SEVERE, etc.)
     * --log-file: Enable logging to file
     *
     * @param args Command-line arguments
     */
    private void parseArgs(String[] args) {
        if (args == null) return;

        // go over all arguments
        for (String arg : args) {
            if ("--log-enable".equals(arg)) {
                loggingEnabled = true;
            } else if (arg.startsWith("--log-level=")) {
                String levelStr = arg.substring("--log-level=".length()).toUpperCase();
                try {
                    loggingLevel = Level.parse(levelStr);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid log level: " + levelStr + ". Using default: INFO");
                }
            } else if ("--log-file".equals(arg)) {
                logToFile = true;
            }
        }
    }

    // Convenience methods for different log levels

    /**
     * Logs a severe message.
     * @param message The message to log
     */
    public void severe(String message) {
        logger.severe(message);
    }

    /**
     * Logs a warning message.
     * @param message The message to log
     */
    public void warning(String message) {
        logger.warning(message);
    }

    /**
     * Logs an info message.
     * @param message The message to log
     */
    public void info(String message) {
        logger.info(message);
    }

    /**
     * Logs a configuration message.
     * @param message The message to log
     */
    public void config(String message) {
        logger.config(message);
    }

    /**
     * Logs a fine detail message.
     * @param message The message to log
     */
    public void fine(String message) {
        logger.fine(message);
    }

    /**
     * Logs an exception with its stack trace.
     *
     * @param message Message describing the exception context
     * @param throwable The exception to log
     */
    public void exception(String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }


}
