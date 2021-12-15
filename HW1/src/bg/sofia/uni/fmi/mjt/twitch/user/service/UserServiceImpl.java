package bg.sofia.uni.fmi.mjt.twitch.user.service;

import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.util.Map;

public class UserServiceImpl implements UserService {

    private final Map<String, User> userMap;

    public UserServiceImpl(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    @Override
    public Map<String, User> getUsers() {
        return userMap;
    }
}
