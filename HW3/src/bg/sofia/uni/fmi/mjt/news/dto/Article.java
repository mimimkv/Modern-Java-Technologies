package bg.sofia.uni.fmi.mjt.news.dto;

import com.google.gson.annotations.SerializedName;


import java.util.Objects;

public class Article {
    private final Source source;
    private final String author;
    private final String title;
    private final String description;
    private final String url;
    private final String urlToImage;

    @SerializedName("publishedAt")
    private final String timestamp;
    private final String content;


    public Article(Source source, String author, String title, String description, String url, String urlToImage,
                   String timestamp, String content) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.timestamp = timestamp;
        this.content = content;
    }

    public Source getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(source, article.getSource())
                && Objects.equals(author, article.getAuthor())
                && Objects.equals(title, article.getTitle())
                && Objects.equals(description, article.getDescription())
                && Objects.equals(url, article.getUrl())
                && Objects.equals(urlToImage, article.getUrlToImage())
                && Objects.equals(timestamp, article.getTimestamp())
                && Objects.equals(content, article.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, author, title, description, url, urlToImage, timestamp, content);
    }
}
