import bg.sofia.uni.fmi.mjt.spotify.Spotify;
import bg.sofia.uni.fmi.mjt.spotify.StreamingService;
import bg.sofia.uni.fmi.mjt.spotify.account.Account;
import bg.sofia.uni.fmi.mjt.spotify.account.FreeAccount;
import bg.sofia.uni.fmi.mjt.spotify.account.PremiumAccount;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlayableNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.library.Library;
import bg.sofia.uni.fmi.mjt.spotify.library.UserLibrary;
import bg.sofia.uni.fmi.mjt.spotify.playable.Audio;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;
import bg.sofia.uni.fmi.mjt.spotify.playable.Video;


public class Main {
    public static void main(String[] args) {
        Playable video1 = new Video("video1", "artist1", 2000, 3.20);
        Playable video2 = new Video("video2", "artist2", 1999, 10.0);
        Playable audio1 = new Audio("audio1", "artist1", 2009, 3.1);
        Playable audio2 = new Audio("audio2", "artist2", 2002, 1.40);

        Library library1 = new UserLibrary();
        Account account1 = new PremiumAccount("pesho@abv.bg", library1);
        Account account2 = new FreeAccount("ivan@abv.bg", library1);

        Playable[] playables = new Playable[]{video1, audio1};
        Account[] accounts = new Account[]{account1};
        StreamingService spotify = new Spotify(accounts, playables);

        try{
            spotify.findByTitle("audio1");
            spotify.findByTitle("song1");
        } catch (PlayableNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            spotify.play(account1, "video1");
            spotify.play(account1, "video1");
            spotify.play(account2, "audio1");
        } catch (AccountNotFoundException | PlayableNotFoundException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(spotify.getMostPlayed().getTitle());
    }
}
