package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamImpl;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.content.video.VideoImpl;
import bg.sofia.uni.fmi.mjt.twitch.user.MeteredUser;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Twitch implements StreamingPlatform {

    private static final String ARGS_CANNOT_BE_NULL = "Arguments cannot be null or empty";

    private final UserService userService;
    private Set<Content> content;
    private Set<MeteredUser> meteredUsers;

    public Twitch(UserService userService) {
        this.userService = userService;
        content = new HashSet<>();
        meteredUsers = new HashSet<>();
    }

    @Override
    public Stream startStream(String username, String title, Category category)
            throws UserNotFoundException, UserStreamingException {
        if (username == null || username.isBlank() || title == null ||
                title.isBlank() || category == null) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_NULL);
        }

        if (!userService.getUsers().containsKey(username)) {
            throw new UserNotFoundException(
                    String.format("User with name %s is not found", username));
        }

        User user = userService.getUsers().get(username);
        if (user.getStatus().equals(UserStatus.STREAMING)) {
            throw new UserStreamingException(user.getStatus().getMessage());
        }

        user.setStatus(UserStatus.STREAMING);
        if (!containsUser(user)) {
            addMeteredUser(user);
        }

        Stream stream = new StreamImpl(new Metadata(title, category, user), LocalDateTime.now());
        content.add(stream);

        return stream;
    }

    @Override
    public Video endStream(String username, Stream stream) throws UserNotFoundException, UserStreamingException {
        if (username == null || username.isBlank() || stream == null) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_NULL);
        }

        if (!userService.getUsers().containsKey(username)) {
            throw new UserNotFoundException(
                    String.format("User with name %s is not found", username));
        }

        User user = userService.getUsers().get(username);
        if (user.getStatus().equals(UserStatus.OFFLINE)) {
            throw new UserStreamingException(user.getStatus().getMessage());
        }

        //Duration duration = Duration.between(stream.getStartTime(), LocalDateTime.now());
        stream.setEndTime(LocalDateTime.now());
        user.setStatus(UserStatus.OFFLINE);
        Video video = new VideoImpl(stream.getMetadata(), stream.getDuration());
        content.add(video);
        content.remove(stream);

        return video;
    }

    @Override
    public void watch(String username, Content content) throws UserNotFoundException, UserStreamingException {
        if (username == null || username.isBlank() || content == null) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_NULL);
        }

        if (!userService.getUsers().containsKey(username)) {
            throw new UserNotFoundException(
                    String.format("User with name %s is not found", username));
        }

        User user = userService.getUsers().get(username);
        if (user.getStatus().equals(UserStatus.STREAMING)) {
            throw new UserStreamingException(user.getStatus().getMessage());
        }

        if (!containsUser(user)) {
            addMeteredUser(user);
        }
        MeteredUser watcher = getMeteredUserByName(username);
        watcher.watchContent(content);

        if (!containsUser(user)) {
            addMeteredUser(content.getMetadata().user());
        }
        MeteredUser author = getMeteredUserByName(content.getMetadata().user().getName());
        author.incrementViewsByOthers();
    }

    @Override
    public User getMostWatchedStreamer() {
        User user = null;
        int maxViews = 0;

        for (MeteredUser meteredUser : meteredUsers) {
            if (meteredUser.getTotalViewsByOthers() > maxViews) {
                maxViews = meteredUser.getTotalViewsByOthers();
                user = meteredUser.getUser();
            }
        }

        return user;
    }

    @Override
    public Content getMostWatchedContent() {
        Content mostWatched = null;
        int maxViews = 0;

        for (Content c : content) {
            if (c.getNumberOfViews() > maxViews) {
                maxViews = c.getNumberOfViews();
                mostWatched = c;
            }
        }

        return mostWatched;
    }

    @Override
    public Content getMostWatchedContentFrom(String username) throws UserNotFoundException {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_NULL);
        }

        if (!userService.getUsers().containsKey(username)) {
            throw new UserNotFoundException(
                    String.format("User with name %s is not found", username));
        }

        MeteredUser meteredUser = getMeteredUserByName(username);
        return meteredUser.getMostWatchedContent();

    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(ARGS_CANNOT_BE_NULL);
        }

        if (!userService.getUsers().containsKey(username)) {
            throw new UserNotFoundException(
                    String.format("User with name %s is not found", username));
        }

        User user = userService.getUsers().get(username);

        if (!containsUser(user)) {
            addMeteredUser(user);
        }
        MeteredUser meteredUser = getMeteredUserByName(username);
        return meteredUser.getSortedWatchedCategories();
    }

    private MeteredUser getMeteredUserByName(String username) {

        for (MeteredUser meteredUser : meteredUsers) {
            if (username.equals(meteredUser.getUserName())) {
                return meteredUser;
            }
        }

        return null;
    }

    private boolean containsUser(User user) {
        for (MeteredUser u : meteredUsers) {
            if (user.getName().equals(u.getUserName())) {
                return true;
            }
        }

        return false;
    }

    private void addMeteredUser(User user) {
        MeteredUser meteredUser = new MeteredUser(user);
        meteredUsers.add(meteredUser);
    }
}
