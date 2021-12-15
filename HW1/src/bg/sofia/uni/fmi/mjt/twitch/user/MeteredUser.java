package bg.sofia.uni.fmi.mjt.twitch.user;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MeteredUser {

    private final User user;
    private int totalViewsByOthers;
    private Map<Category, Integer> viewsByCategory;
    private Map<Content, Integer> viewedContent;


    public MeteredUser(User user) {
        this.user = user;
        viewsByCategory = new EnumMap<>(Category.class);
        viewedContent = new HashMap<>();
    }

    public void watchContent(Content content) {
        content.startWatching(user);
        incrementViews(content);
        addContent(content);
    }

    public Content getMostWatchedContent() {
        Content mostWatched = null;
        int currWatchCount;
        int maxWatchCount = 0;

        for (Map.Entry<Content, Integer> contentEntry : viewedContent.entrySet()) {
            currWatchCount = contentEntry.getValue();

            if (currWatchCount > maxWatchCount) {
                maxWatchCount = currWatchCount;
                mostWatched = contentEntry.getKey();
            }
        }

        return mostWatched;
    }


    private void incrementViews(Content content) {
        Category category = content.getMetadata().category();
        if (!viewsByCategory.containsKey(category)) {
            viewsByCategory.put(category, 0);
        }

        viewsByCategory.put(category, viewsByCategory.get(category) + 1);
    }

    private void addContent(Content content) {
        if (!viewedContent.containsKey(content)) {
            viewedContent.put(content, 0);
        }

        viewedContent.put(content, viewedContent.get(content) + 1);
    }

    public String getUserName() {
        return this.user.getName();
    }

    public List<Category> getSortedWatchedCategories() {

        if (viewsByCategory == null || viewsByCategory.isEmpty()) {
            return List.copyOf(Collections.emptyList());
        }

        List<Category> categoryList = new ArrayList<>(viewsByCategory.keySet());
        //categoryList.sort(categoryComparator);
        Collections.sort(categoryList, categoryComparator);
        return List.copyOf(categoryList);
    }

    private Comparator<Category> categoryComparator = new Comparator<Category>() {
        @Override
        public int compare(Category o1, Category o2) {

            int views1 = viewsByCategory.get(o1);
            int views2 = viewsByCategory.get(o2);

            return views2 - views1;
        }
    };

    public int getTotalViewsByOthers() {
        return totalViewsByOthers;
    }

    public void incrementViewsByOthers() {
        ++totalViewsByOthers;
    }

    public User getUser() {
        return user;
    }
}
