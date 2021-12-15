package bg.sofia.uni.fmi.mjt.twitch.content.stream;

import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;
import java.time.LocalDateTime;

public class StreamImpl implements Stream {

    private Metadata metadata;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    //private Duration duration;
    private int numberOfViews;

    public StreamImpl(Metadata metadata, LocalDateTime startTime) {
        this.metadata = metadata;
        this.startTime = startTime;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public Duration getDuration() {
        //return duration;
        if (endTime == null) {
            return Duration.between(startTime, LocalDateTime.now());
        }

        return Duration.between(startTime, endTime);
    }

    @Override
    public void startWatching(User user) {
        ++numberOfViews;
    }

    @Override
    public void stopWatching(User user) {
        --numberOfViews;
    }

    @Override
    public int getNumberOfViews() {
        return numberOfViews;
    }

    /*
    public LocalDateTime getStartTime() {
        return startTime;
    }*/

    @Override
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
