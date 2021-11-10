package bg.sofia.uni.fmi.mjt.spotify.library;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.EmptyLibraryException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.LibraryCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.playlist.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.playlist.UserPlaylist;

public class UserLibrary implements Library {
    private static final int LIBRARY_CAPACITY = 21;
    private static final String DEFAULT_LIBRARY = "Liked content";
    private int size;
    private Playlist[] playlists;

    public UserLibrary() {
        playlists = new Playlist[LIBRARY_CAPACITY];
        playlists[size++] = new UserPlaylist(DEFAULT_LIBRARY);
    }

    @Override
    public void add(Playlist playlist) throws LibraryCapacityExceededException {
        if (size == LIBRARY_CAPACITY) {
            throw new LibraryCapacityExceededException(
                    String.format("Library is full! Cannot add %s to the library!", playlist.getName()));
        }

        playlists[size++] = playlist;
    }

    @Override
    public void remove(String name) throws EmptyLibraryException, PlaylistNotFoundException {
        if (name.equals(DEFAULT_LIBRARY)) {
            throw new IllegalArgumentException(
                    String.format("%s library cannot be deleted", DEFAULT_LIBRARY));
        }

        if (size < 2) {
            throw new EmptyLibraryException(
                    String.format("Library is empty!"));
        }

        int indexOfPlaylist = findPlaylist(name);
        if (indexOfPlaylist == -1) {
            throw new PlaylistNotFoundException(
                    String.format("%s does not exist!", name));
        }

        for (int i = indexOfPlaylist; i < playlists.length - 1; ++i) {
            playlists[i] = playlists[i + 1];
        }
        --size;
    }

    @Override
    public Playlist getLiked() {
        return playlists[0];
    }

    private int findPlaylist(String name) {
        for (int i = 0; i < size; i++) {
            if (name.equals(playlists[i].getName())) {
                return i;
            }
        }

        return -1;
    }

}
