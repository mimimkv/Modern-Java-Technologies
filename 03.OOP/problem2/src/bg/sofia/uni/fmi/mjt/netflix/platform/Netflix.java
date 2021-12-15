package bg.sofia.uni.fmi.mjt.netflix.platform;

import bg.sofia.uni.fmi.mjt.netflix.account.Account;
import bg.sofia.uni.fmi.mjt.netflix.content.Streamable;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.ContentNotFoundException;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.ContentUnavailableException;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.UserNotFoundException;

import java.time.LocalDateTime;


public class Netflix implements StreamingService{
    private final Account[] accounts;
    private final Streamable[] streamableContent;
    private final int[] countWatchedContent;

    public Netflix(Account[] accounts, Streamable[] streamableContent) {
        this.accounts = accounts;
        this.streamableContent = streamableContent;
        countWatchedContent = new int[streamableContent.length];
    }

    @Override
    public void watch(Account user, String videoContentName) throws ContentUnavailableException {
        if (!userExists(user)) {
            throw new UserNotFoundException("User " + user.username() + " not found");
        }

        Streamable video = findByName(videoContentName);
        if (video == null) {
            throw new ContentNotFoundException("Streaming content " + videoContentName + " not found");
        }

        if (isAgeRestricted(user, video)) {
            throw new ContentUnavailableException("Video " + videoContentName + " is age restricted");
        }

        int index = findIndexOfVideo(videoContentName);
        ++countWatchedContent[index];

    }

    @Override
    public Streamable findByName(String videoContentName) {
        for (Streamable s : streamableContent) {
            if (s.getTitle().equals(videoContentName)) {
                return s;
            }
        }

        return null;
    }

    @Override
    public Streamable mostViewed() {
        if (isEmpty(countWatchedContent)) {
            return null;
        }

        int indexOfMostViewed = getIndexOfMaxElement(countWatchedContent);
        return streamableContent[indexOfMostViewed];
    }

    @Override
    public int totalWatchedTimeByUsers() {
        int totalTimeInMinutes = 0;
        for (int i = 0; i < streamableContent.length; i++) {
            totalTimeInMinutes += (countWatchedContent[i] *
                    streamableContent[i].getDuration());
        }

        return totalTimeInMinutes;
    }

    private boolean userExists(Account user) {
        for (Account account : accounts) {
            if (account.equals(user)) {
                return true;
            }
        }

        return false;
    }

    private boolean isAgeRestricted(Account user, Streamable content) {
        LocalDateTime minYearsAgo = null;
        switch (content.getRating()) {
            case PG13 -> minYearsAgo = LocalDateTime.now().minusYears(13);
            case NC17 -> minYearsAgo = LocalDateTime.now().minusYears(17);
        }

        return minYearsAgo != null && user.birthdayDate().isAfter(minYearsAgo);
    }

    private int findIndexOfVideo(String name) {
        for (int i = 0; i < streamableContent.length; i++) {
            if (streamableContent[i].getTitle().equals(name)) {
                return i;
            }
        }

        return -1;
    }

    private boolean isEmpty(int[] array) {
        for (int i : array) {
            if (i != 0) {
                return false;
            }
        }

        return true;
    }

    private int getIndexOfMaxElement(int[] array) {
        int max = -1;
        for (int i = 0; i < array.length; i++) {
            if (max == -1 || array[max] < array[i]) {
                max = i;
            }
        }

        return max;
    }
}
