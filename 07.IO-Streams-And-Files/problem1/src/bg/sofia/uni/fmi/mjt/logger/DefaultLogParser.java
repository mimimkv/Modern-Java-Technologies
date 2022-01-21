package bg.sofia.uni.fmi.mjt.logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultLogParser implements LogParser {

    private static final int LEVEL_INDEX = 0;
    private static final int TIMESTAMP_INDEX = 1;
    private static final int PACKAGE_NAME_INDEX = 2;
    private static final int MESSAGE_INDEX = 3;

    private final Path logsFilePath;
    private List<Log> logs;

    public DefaultLogParser(Path logsFilePath) {
        this.logsFilePath = logsFilePath;
        logs = new ArrayList<>();
        readLogs();
    }

    private void readLogs() {
        logs = new ArrayList<>();
        try (BufferedReader fileReader = Files.newBufferedReader(logsFilePath)) {
            String line;
            String[] tokens;

            while ((line = fileReader.readLine()) != null) {
                tokens = line.split("\\|");
                tokens[LEVEL_INDEX] = tokens[LEVEL_INDEX].replaceAll("\\[", "").replaceAll("]", "");
                Level level = Level.valueOf(tokens[LEVEL_INDEX]);
                LocalDateTime timestamp = LocalDateTime.parse(tokens[TIMESTAMP_INDEX], DateTimeFormatter.ISO_DATE_TIME);
                Log log = new Log(level, timestamp, tokens[PACKAGE_NAME_INDEX], tokens[MESSAGE_INDEX]);
                logs.add(log);
            }

        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }
    }

    @Override
    public List<Log> getLogs(Level level) {
        if (level == null) {
            throw new IllegalArgumentException("Level cannot be null");
        }

        readLogs();
        List<Log> levelLogs = new ArrayList<>();
        for (Log log : logs) {
            if (log.level().getLevel() == level.getLevel()) {
                levelLogs.add(log);
            }
        }

        return levelLogs;
    }

    @Override
    public List<Log> getLogs(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("local date time cannot be null");
        }

        readLogs();
        List<Log> logsFromTo = new ArrayList<>();
        for (Log log : logs) {
            if (log.timestamp().isAfter(from) && log.timestamp().isBefore(to)) {
                logsFromTo.add(log);
            }
        }

        return logsFromTo;
    }

    @Override
    public List<Log> getLogsTail(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n cannot be a negative number");
        }
        if (n == 0) {
            return Collections.emptyList();
        }
        readLogs();
        int logsSize = logs.size();
        if (n > logsSize) {
            n = logsSize;
        }

        return logs.subList(logsSize - n, logsSize);
    }

}
