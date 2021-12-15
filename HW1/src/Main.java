import bg.sofia.uni.fmi.mjt.twitch.StreamingPlatform;
import bg.sofia.uni.fmi.mjt.twitch.Twitch;
import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserImpl;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        User user1 = new UserImpl("pesho");
        User user2 = new UserImpl("gosho");
        User user3 = new UserImpl("pencho");
        User user4 = new UserImpl("nasko");
        User user5 = new UserImpl("john");

        UserService userService = new UserServiceImpl(
                Map.of(user1.getName(), user1, user2.getName(), user2,
                        user3.getName(), user3, user4.getName(), user4,
                        user5.getName(), user5));

        StreamingPlatform twitch = new Twitch(userService);

        // startStream tests:
        System.out.println("startStream tests:");
        Stream stream1 = null;
        Stream stream2 = null;
        try {
            stream1 = twitch.startStream("pencho", "Learn C++", Category.GAMES);
            stream2 = twitch.startStream("gosho", "tennis", Category.ESPORTS);
            twitch.startStream("unknown", "random", Category.GAMES);
        } catch (UserNotFoundException | UserStreamingException e) {
            System.out.println(e.getMessage());
        }

        try {
            twitch.startStream("gosho", "football", Category.ESPORTS);
        } catch (UserNotFoundException | UserStreamingException e) {
            System.out.println(e.getMessage());
        }

        try {
            twitch.startStream("nasko", null, Category.ESPORTS);
        } catch (IllegalArgumentException | UserStreamingException | UserNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // endStream tests:
        System.out.println("\nendStream tests:");
        try {
            twitch.endStream(null, stream1);
        } catch (UserNotFoundException | UserStreamingException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            twitch.endStream("pesho", stream2);
        } catch (UserNotFoundException | UserStreamingException e) {
            System.out.println(e.getMessage());
        }

        try {
            twitch.endStream("unknown", stream1);
        } catch (UserNotFoundException | UserStreamingException e) {
            System.out.println(e.getMessage());
        }

        Video video1 = null;
        Video video2 = null;
        try {
            video1 = twitch.endStream("gosho", stream2);
        } catch (UserNotFoundException | UserStreamingException e) {
            System.out.println(e.getMessage());
        }

        //these should return null:
        System.out.println();
        if (twitch.getMostWatchedContent() == null) {
            System.out.println("Most watched content is not defined!");
        }

        if (twitch.getMostWatchedStreamer() == null) {
            System.out.println("Most watched user is not defined!");
        }

        try {
            List<Category> l1 = twitch.getMostWatchedCategoriesBy("nasko");
            if (l1.isEmpty()) {
                System.out.println("Nasko has not seen anything yet!");
            }
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            twitch.watch("nasko", video1);
            twitch.watch("nasko", video1);
            twitch.watch("nasko", stream1);
            twitch.watch("nasko", video1);
        } catch (UserNotFoundException | UserStreamingException e) {
            System.out.println(e.getMessage());
        }

        List<Category> list1 = null;
        try {
            list1 = twitch.getMostWatchedCategoriesBy("nasko");
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Nasko's most watched categories:");
        for (Category c : list1) {
            System.out.println(c.name());
        }

        System.out.println("Nasko's most watched content:");
        Content content1 = null;
        try {
            content1 = twitch.getMostWatchedContentFrom("nasko");
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(content1.getMetadata().title());

        System.out.println("\nMost watched content:");
        Content content2 = twitch.getMostWatchedContent();
        System.out.println(content2.getMetadata().title());

        System.out.printf("Most watched streamer: %s\n",
                twitch.getMostWatchedStreamer().getName());

        try {
            twitch.watch("pencho", video1);
        } catch (UserNotFoundException | UserStreamingException e) {
            System.out.println(e.getMessage());
        }

        Stream stream3 = null;
        Video video3 = null;
        try {
            video2 = twitch.endStream("pencho", stream1);
            stream3 = twitch.startStream("pencho", "song1", Category.MUSIC);
            video3 = twitch.endStream("pencho", stream3);
            twitch.watch("pencho", video2);
            twitch.watch("pencho", video2);
            twitch.watch("pencho", video2);
            twitch.watch("pencho", video3);
            twitch.watch("pencho", video2);
            twitch.watch("pencho", video1);
            twitch.watch("pencho", video1);
        } catch (UserNotFoundException | UserStreamingException e) {
            System.out.println(e.getMessage());
        }

        System.out.printf("Most watched streamer: %s\n",
                twitch.getMostWatchedStreamer().getName());
        System.out.printf("Most watched content: %s\n",
                twitch.getMostWatchedContent().getMetadata().title());

        try {
            System.out.printf("Most watched content from pencho: %s\n",
                    twitch.getMostWatchedContentFrom("pencho").getMetadata().title());
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }

        List<Category> penchoList = null;
        try {
            penchoList = twitch.getMostWatchedCategoriesBy("pencho");
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\nPencho's most watched categories:");
        for (Category category : penchoList) {
            System.out.println(category.name());
        }

        List<Category> list2 = null;
        try {
            list2 = twitch.getMostWatchedCategoriesBy("john");
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }

        if (list2.equals(Collections.emptyList())) {
            System.out.println("John has not seen anything yet");
        }
    }
}
