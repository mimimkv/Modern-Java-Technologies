package bg.sofia.uni.fmi.mjt.netflix;

import bg.sofia.uni.fmi.mjt.netflix.account.Account;
import bg.sofia.uni.fmi.mjt.netflix.content.Episode;
import bg.sofia.uni.fmi.mjt.netflix.content.Movie;
import bg.sofia.uni.fmi.mjt.netflix.content.Series;
import bg.sofia.uni.fmi.mjt.netflix.content.Streamable;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.Genre;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.ContentNotFoundException;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.ContentUnavailableException;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.netflix.platform.Netflix;
import bg.sofia.uni.fmi.mjt.netflix.platform.StreamingService;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Account account1 = new Account("pesho", LocalDateTime.now().minusYears(20));
        Account account2 = new Account("gosho", LocalDateTime.now().minusYears(10));
        Account account3 = new Account("pencho", LocalDateTime.now().minusYears(13));
        Account account4 = new Account("randomUser", LocalDateTime.now().minusYears(15));
        Account account5 = new Account("ivan", LocalDateTime.of(2012, 9, 30, 12, 45));

        Account[] accounts = new Account[]{account1, account2, account3};

        Streamable movie1 = new Movie("Star Wars", Genre.ACTION, PgRating.G, 120);
        Streamable movie2 = new Movie("Wrong Turn", Genre.HORROR, PgRating.NC17, 90);
        Streamable movie3 = new Movie("Mr Bean", Genre.COMEDY, PgRating.PG13, 100);
        Episode[] episodes = {new Episode("First ep. GOT", 40),
                new Episode("Second ep. GOT", 40)};
        Streamable series1 = new Series("Game of Thrones", Genre.ACTION, PgRating.PG13, episodes);

        Streamable[] streamables = new Streamable[]{movie1, movie2, movie3, series1};
        StreamingService netflix = new Netflix(accounts, streamables);

        if (netflix.mostViewed() != null) {
            System.out.println(netflix.mostViewed().getTitle());
        } else {
            System.out.println("no such streamable");
        }

        if (netflix.findByName("Mr Bean") != null) {
            System.out.println(netflix.findByName("Mr Bean").getTitle());
        }

        if (netflix.findByName("random video") == null) {
            System.out.println("no such video");
        }

        try{
            netflix.watch(account1, "Star Wars");
            netflix.watch(account2, "Star Wars");
            netflix.watch(account3, "Star Wars");
            netflix.watch(account4, "Star Wars");
        } catch (ContentUnavailableException | UserNotFoundException | ContentNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try{
            netflix.watch(account1, "some video");
        } catch (ContentUnavailableException | UserNotFoundException | ContentNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try{
            netflix.watch(account2, "Wrong Turn");
        } catch (ContentUnavailableException | UserNotFoundException | ContentNotFoundException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(netflix.mostViewed().getTitle());
        System.out.println(netflix.totalWatchedTimeByUsers());
    }
}
