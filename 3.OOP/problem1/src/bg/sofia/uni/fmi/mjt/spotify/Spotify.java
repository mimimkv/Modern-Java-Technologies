package bg.sofia.uni.fmi.mjt.spotify;

import bg.sofia.uni.fmi.mjt.spotify.account.Account;
import bg.sofia.uni.fmi.mjt.spotify.account.AccountType;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlayableNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.StreamingServiceException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;
import bg.sofia.uni.fmi.mjt.spotify.playlist.Playlist;

public class Spotify implements StreamingService {
    private static final double AD_REVENUE = 0.10;
    private static final double PREMIUM_REVENUE = 25.0;

    private Account[] accounts;
    private Playable[] playableContent;

    public Spotify(Account[] accounts, Playable[] playableContent) {
        this.accounts = accounts;
        this.playableContent = playableContent;
    }

    @Override
    public void play(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException {
        if (account == null || title == null || title.isBlank()) {
            throw new IllegalArgumentException("Arguments cannot be null or blank!");
        }

        if (!accountExists(account)) {
            throw new AccountNotFoundException("The account is not registered!");
        }

        Playable playable = findByTitle(title); // this throws PlayableNotFoundExc
        account.listen(playable);

    }

    @Override
    public void like(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException, StreamingServiceException {
        if (account == null || title == null || title.isBlank()) {
            throw new IllegalArgumentException("Arguments cannot be null or blank!");
        }

        if (!accountExists(account)) {
            throw new AccountNotFoundException("The account is not registered!");
        }

        Playable playable = findByTitle(title); // this throws exception
        Playlist likedContentLibrary = account.getLibrary().getLiked();

        try {
            likedContentLibrary.add(playable);
        } catch (PlaylistCapacityExceededException e) {
            e.printStackTrace();
            throw new StreamingServiceException("Content could not be added");
        }

    }

    @Override
    public Playable findByTitle(String title) throws PlayableNotFoundException {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("The title is empty!");
        }

        for (Playable p : playableContent) {
            if (p != null && title.equals(p.getTitle())) {
                return p;
            }
        }

        throw new PlayableNotFoundException(String.format("%s not found!", title));
    }

    @Override
    public Playable getMostPlayed() {
        int maxPlays = 0;
        Playable mostPlayed = null;

        for (Playable p : playableContent) {
            int currPlays = p.getTotalPlays();
            if (currPlays > maxPlays) {
                maxPlays = currPlays;
                mostPlayed = p;
            }
        }

        return mostPlayed;
    }

    @Override
    public double getTotalListenTime() {
        double totalListenTime = 0.0;

        for (Account a : accounts) {
            totalListenTime += a.getTotalListenTime();
        }

        return totalListenTime;
    }

    @Override
    public double getTotalPlatformRevenue() {
        double revenue = 0;

        for (Account a : accounts) {
            if (a.getType().equals(AccountType.FREE)) {
                revenue += (a.getAdsListenedTo() * AD_REVENUE);
            } else {
                revenue += PREMIUM_REVENUE;
            }
        }

        return revenue;
    }

    private boolean accountExists(Account account) {
        for (Account a : accounts) {
            if (a.getEmail().equals(account.getEmail())) {
                return true;
            }
        }

        return false;
    }
}
