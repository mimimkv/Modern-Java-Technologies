package bg.sofia.uni.fmi.mjt.news.dto;

import java.util.List;

public class NewsCollection {

    private final String status;
    private final int totalResults;
    private final List<Article> articles;

    public NewsCollection(String status, int totalResults, List<Article> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void addElements(List<Article> other) {
        articles.addAll(other);
    }
}
