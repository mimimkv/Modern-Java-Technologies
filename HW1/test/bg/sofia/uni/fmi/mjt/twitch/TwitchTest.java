package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TwitchTest {

    @Mock
    private UserService userService;

    @Mock
    private User user;

    @Mock
    private Stream stream;

    @InjectMocks
    private Twitch twitch;

    @Test
    public void testStartStreamNullUsernameFail() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.startStream(null, "stream1", Category.MUSIC),
                "IllegalArgumentException expected when starting a stream with null username");
    }

    @Test
    public void testStartStreamNullTitleFail() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.startStream("pencho", null, Category.MUSIC),
                "IllegalArgumentException expected when starting a stream with null title");
    }

    @Test
    public void testStartStreamNullCategoryFail() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.startStream("pencho", "stream1", null),
                "IllegalArgumentException expected when starting a stream with null category");
    }

    @Test
    public void testStartStreamEmptyUsernameFail() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.startStream("", "str1", Category.MUSIC),
                "IllegalArgumentException expected when starting a stream with an empty username");
    }

    @Test
    public void testStartStreamBlankUsernameFail() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.startStream("  ", "str1", Category.MUSIC),
                "IllegalArgumentException expected when starting a stream with a blank username");
    }

    @Test
    public void testStartStreamEmptyTitleFail() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.startStream("pencho", "", Category.MUSIC),
                "IllegalArgumentException expected when starting a stream with an empty title");
    }

    @Test
    public void testStartStreamBlankTitleFail() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.startStream("pencho", "  ", Category.MUSIC),
                "IllegalArgumentException expected when starting a stream with a blank title");
    }

    @Test
    public void testStartStreamUserNotFoundFail() {
        when(userService.getUsers()).thenReturn(Map.of("pencho", user));

        assertThrows(UserNotFoundException.class,
                () -> twitch.startStream("unknown", "str1", Category.MUSIC),
                "UserNotFoundException expected when starting a stream with a user that is not in the service");
    }

    @Test
    public void testStartStreamUserStreamingException() {
        when(userService.getUsers()).thenReturn(Map.of("pencho", user));
        when(user.getStatus()).thenReturn(UserStatus.STREAMING);

        assertThrows(UserStreamingException.class,
                () -> twitch.startStream("pencho", "str1", Category.MUSIC),
                "UserStreaming exception expected when a user that already streams " +
                        "tries to start another stream");
    }


    @Test
    public void testEndStreamNullUsernameFail() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.endStream(null, stream),
                "IllegalArgumentException expected when ending a stream with null username");
    }

    @Test
    public void testEndStreamNullTitleFail() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.endStream("pencho", null),
                "IllegalArgumentException expected when ending a stream with null title");
    }

    @Test
    public void testEndStreamEmptyUsernameFail() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.endStream("", stream),
                "IllegalArgumentException expected when ending a stream with an empty username");
    }

    @Test
    public void testEndStreamBlankUsernameFail() {
        assertThrows(IllegalArgumentException.class,
                () -> twitch.endStream("  ", stream),
                "IllegalArgumentException expected when ending a stream with a blank username");
    }

    @Test
    public void testEndStreamUserNotFoundFail() {
        when(userService.getUsers()).thenReturn(Map.of("pencho", user));

        assertThrows(UserNotFoundException.class,
                () -> twitch.endStream("unknown", stream),
                "UserNotFoundException expected when ending a stream with a user that is not in the service");
    }

    @Test
    public void testEndStreamUserStreamingException() {
        when(userService.getUsers()).thenReturn(Map.of("pencho", user));
        when(user.getStatus()).thenReturn(UserStatus.OFFLINE);

        assertThrows(UserStreamingException.class,
                () -> twitch.endStream("pencho", stream),
                "UserStreamingException expected when a user that is not streaming" +
                        "tries to end a stream");
    }

    @Test
    public void testGetMostWatchedStreamerNoSuchStreamer() {
        assertNull(twitch.getMostWatchedStreamer(), "Most watched streamer is expected to be null");
    }

    @Test
    public void testGetMostWatchedStreamerOneMostWatchedSuccess() {
        when(userService.getUsers()).thenReturn(Map.of("pencho", user));
        when(user.getStatus()).thenReturn(UserStatus.OFFLINE);
        when(user.getName()).thenReturn("pencho");
        try {
            stream = twitch.startStream("pencho", "str1", Category.MUSIC);
            twitch.watch("pencho", stream);

        } catch (UserNotFoundException | UserStreamingException e) {
            fail("startStream and watch should not throw an exception when all the data is valid");
        }

        assertEquals(user, twitch.getMostWatchedStreamer());
    }

    @Test
    public void testGetMostWatchedContentNoSuchContent() {
        assertNull(twitch.getMostWatchedContent(), "Most watched content is expected to be null");
    }

    @Test
    public void testGetMostWatchedContentOneContentInTwitch() {
        when(userService.getUsers()).thenReturn(Map.of("pencho", user));
        when(user.getStatus()).thenReturn(UserStatus.OFFLINE);
        when(user.getName()).thenReturn("pencho");
        try {
            stream = twitch.startStream("pencho", "str1", Category.MUSIC);
            twitch.watch("pencho", stream);

        } catch (UserNotFoundException | UserStreamingException e) {
            fail("startStream and watch should not throw an exception when all the data is valid");
        }

        assertEquals(stream, twitch.getMostWatchedContent());
    }


    @Test
    public void testGetMostWatchedContentFromUsernameNullFail() {
        assertThrows(IllegalArgumentException.class, () -> twitch.getMostWatchedContentFrom(null),
                "IllegalArgumentException expected when calling getMostWatchedContentFrom with null argument");
    }

    @Test
    public void testGetMostWatchedContentFromUsernameEmptyFail() {
        assertThrows(IllegalArgumentException.class, () -> twitch.getMostWatchedContentFrom(""),
                "IllegalArgumentException expected when calling getMostWatchedContentFrom with empty username");
    }

    @Test
    public void testGetMostWatchedContentFromUsernameBlankFail() {
        assertThrows(IllegalArgumentException.class, () -> twitch.getMostWatchedContentFrom("       "),
                "IllegalArgumentException expected when calling getMostWatchedContentFrom with blank username");
    }

    @Test
    public void testGetMostWatchedContentFromUserNotFoundFail() {
        when(userService.getUsers()).thenReturn(Map.of("pencho", user));
        assertThrows(UserNotFoundException.class, () -> twitch.getMostWatchedContentFrom("unknown"),
                "UserNotFoundException expected when trying to get most watched content from a user that is not found");
    }

    @Test
    public void testGetMostWatchedCategoriesFromUsernameNullFail() {
        assertThrows(IllegalArgumentException.class, () -> twitch.getMostWatchedCategoriesBy(null),
                "IllegalArgumentException expected when calling getMostWatchedCategoriesBy with null argument");
    }

    @Test
    public void testGetMostWatchedCategoriesFromUsernameEmptyFail() {
        assertThrows(IllegalArgumentException.class, () -> twitch.getMostWatchedCategoriesBy(""),
                "IllegalArgumentException expected when calling getMostWatchedCategoriesBy with an empty argument");
    }

    @Test
    public void testGetMostWatchedCategoriesFromUsernameBlankFail() {
        assertThrows(IllegalArgumentException.class, () -> twitch.getMostWatchedCategoriesBy("   "),
                "IllegalArgumentException expected when calling getMostWatchedCategoriesBy with blank argument");
    }

    @Test
    public void testGetMostWatchedCategoriesFromUserNotFoundFail() {
        when(userService.getUsers()).thenReturn(Map.of("pencho", user));
        assertThrows(UserNotFoundException.class, () -> twitch.getMostWatchedCategoriesBy("unknown"),
                "UserNotFoundException expected when calling getMostWatchedCategoriesBy with unknown user");
    }

    @Test
    public void testGetMostWatchedCategoriesByNothingHasBeenWatched() {
        when(userService.getUsers()).thenReturn(Map.of("pencho", user));
        when(user.getName()).thenReturn("pencho");

        try {
            assertEquals(List.copyOf(Collections.emptyList()), twitch.getMostWatchedCategoriesBy("pencho"),
                    "Empty list of categories expected when nothing has been watched by the given user");
        } catch (UserNotFoundException e) {
            fail("getMostWatchedCategoriesBy should not throw an exception when called with valid data");
        }
    }

    @Test
    public void testGetMostWatchedCategoriesByOneContentWatched() {

    }

}