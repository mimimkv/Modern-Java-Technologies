package bg.sofia.uni.fmi.mjt.spotify.playable;

public abstract class PlayableBase implements Playable {
    protected final String title;
    protected final String artist;
    protected int totalPlays;
    protected final int year;
    protected final double duration;

    public PlayableBase(String title, String artist, int year, double duration) {
        this.title = title;
        this.artist = artist;
        this.year = year;
        this.duration = duration;
    }

    public abstract String play();

    @Override
    public int getTotalPlays() {
        return totalPlays;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public double getDuration() {
        return duration;
    }
}
