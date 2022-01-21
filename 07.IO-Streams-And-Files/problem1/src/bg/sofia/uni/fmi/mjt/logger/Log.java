package bg.sofia.uni.fmi.mjt.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Log(Level level, LocalDateTime timestamp, String packageName, String message) {
    @Override
    public String toString() {
        return "[" + level + "]|" +
                timestamp.format(DateTimeFormatter.ISO_DATE_TIME) + "|" +
                packageName + "|" + message + "\n";
    }
}
