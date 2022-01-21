package bg.sofia.uni.fmi.mjt.logger;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class DefaultLogger implements Logger {

    private static final String FILE_NAME_TEMPLATE = "logs-%d.txt";
    private final LoggerOptions options;
    private int counter = 0;


    public DefaultLogger(LoggerOptions options) {
        this.options = options;

        Path path = Path.of(options.getDirectory(), String.format(FILE_NAME_TEMPLATE, counter))
                .toAbsolutePath();
        try (Writer writer = Files.newBufferedWriter(path)) {

        } catch (IOException e) {
            if (options.shouldThrowErrors()) {
                throw new LogException("Cannot open file");
            }
        }
    }


    @Override
    public void log(Level level, LocalDateTime timestamp, String message) {
        if (level == null || timestamp == null || message == null || message.isBlank()) {
            throw new IllegalArgumentException("Arguments cannot be null or empty");
        }

        if (options.getMinLogLevel().getLevel() > level.getLevel()) {
            return;
        }

        Log log = new Log(level, timestamp, options.getClazz().getPackageName(), message);
        try {
            if (Files.size(getCurrentFilePath()) >= options.getMaxFileSizeBytes()) {
                ++counter;
            }
        } catch (IOException e) {
            if (options.shouldThrowErrors()) {
                throw new LogException(getCurrentFilePath().toString() + " is invalid path");
            }
        }

        try (Writer writer = Files.newBufferedWriter(getCurrentFilePath(), StandardOpenOption.APPEND)) {
            writer.write(log.toString());
            writer.flush();
        } catch (IOException e) {
            if (options.shouldThrowErrors()) {
                String fileName = String.format(FILE_NAME_TEMPLATE, counter);
                throw new LogException("Cannot open file: " + fileName);
            }
        }

    }


    @Override
    public LoggerOptions getOptions() {
        return options;
    }

    @Override
    public Path getCurrentFilePath() {
        String file = String.format(FILE_NAME_TEMPLATE, counter);
        return Path.of(options.getDirectory(), file).toAbsolutePath();
    }

}
