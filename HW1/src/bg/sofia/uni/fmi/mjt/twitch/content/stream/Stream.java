package bg.sofia.uni.fmi.mjt.twitch.content.stream;

import bg.sofia.uni.fmi.mjt.twitch.content.Content;

import java.time.LocalDateTime;

public interface Stream extends Content {

    //LocalDateTime getStartTime();

    void setEndTime(LocalDateTime endTime);
}
