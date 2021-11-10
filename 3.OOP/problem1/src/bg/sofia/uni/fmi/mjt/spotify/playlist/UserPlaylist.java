package bg.sofia.uni.fmi.mjt.spotify.playlist;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class UserPlaylist implements Playlist {
    private static final int PLAYLIST_CAPACITY = 20;

    private String name;
    private Playable[] playables;
    private int size;

    public UserPlaylist(String name) {
        this.name = name;
        playables = new Playable[PLAYLIST_CAPACITY];
    }

    @Override
    public void add(Playable playable) throws PlaylistCapacityExceededException {
        if (playables.length == PLAYLIST_CAPACITY) {
            throw new PlaylistCapacityExceededException(
                    String.format("The playlist is full! Cannot add %s to the playlist!", playable.getTitle()));
        }

        playables[size++] = playable;
    }

    @Override
    public String getName() {
        return name;
    }


}
