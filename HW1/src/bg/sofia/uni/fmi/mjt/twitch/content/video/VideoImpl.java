package bg.sofia.uni.fmi.mjt.twitch.content.video;

import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;

public class VideoImpl implements Video {

    private Metadata metadata;
    private final Duration duration;
    private int numberOfViews;

    public VideoImpl(Metadata metadata, Duration duration) {
        this.metadata = metadata;
        this.duration = duration;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void startWatching(User user) {
        ++numberOfViews;

    }

    @Override
    public void stopWatching(User user) {

    }

    @Override
    public int getNumberOfViews() {
        return numberOfViews;
    }
}
