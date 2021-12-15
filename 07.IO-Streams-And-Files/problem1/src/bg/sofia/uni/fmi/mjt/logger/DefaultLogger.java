package bg.sofia.uni.fmi.mjt.logger;


import java.nio.file.Path;
import java.time.LocalDateTime;

public class DefaultLogger implements Logger {

    private static final String FILE_NAME_TEMPLATE = "logs-%d.txt";
    private LoggerOptions options;
    private int counter = 0;

    public DefaultLogger(LoggerOptions options) {
        this.options = options;

    }

    @Override
    public void log(Level level, LocalDateTime timestamp, String message) {
        if (level == null || timestamp == null || message == null || message.isBlank()) {
            throw new IllegalArgumentException("Arguments cannot be null or empty");
        }

        if (options.getMinLogLevel().getLevel() > level.getLevel() && options.shouldThrowErrors()) {
            throw new LogException("Operation cannot be completed");
        }

    }

    @Override
    public LoggerOptions getOptions() {
        return options;
    }

    @Override
    public Path getCurrentFilePath() {
        return null;
    }
}
