package bg.sofia.uni.fmi.mjt.news;

import bg.sofia.uni.fmi.mjt.news.dto.Article;
import bg.sofia.uni.fmi.mjt.news.dto.NewsCollection;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsClientException;
import bg.sofia.uni.fmi.mjt.news.factory.ExceptionFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class NewsClient {

    private static Gson gson;

    private static final int PAGES_COUNT = 3;
    private static final int NEWS_PER_PAGE_COUNT = 30;
    private static final String API_ENDPOINT_SCHEME = "http";
    private static final String API_ENDPOINT_HOST = "newsapi.org";
    private static final String API_ENDPOINT_PATH = "/v2/top-headlines";
    private static final String REQUEST_PAGE_SIZE = String.format("&pageSize=%d", NEWS_PER_PAGE_COUNT);
    private static final String REQUEST_PAGE_NUMBER = "&page=";

    private static final String API_KEY = "INSERT_YOUR_API_KEY_HERE";

    private NewsCollection newsCollection;


    private final HttpClient client;
    private final String apiKey;

    public NewsClient(HttpClient client) {
        this(client, API_KEY);
    }

    public NewsClient(HttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;

        gson = new GsonBuilder().create();
    }

    public List<Article> getArticles(RequestParameter parameters) throws Exception {
        String[] keywords = parameters.getKeywords();
        String country = parameters.getCountry() == null ? "" : parameters.getCountry();
        String category = parameters.getCategory() == null ? "" : parameters.getCategory();
        assertKeywordsNotNull(keywords);

        HttpResponse<String> response;
        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(getUri(country, category, 1, keywords)).build();
            System.out.println(request.uri());
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new NewsClientException("Could not retrieve news", e);
        }

        if (response.statusCode() != HttpURLConnection.HTTP_OK) {
            throw ExceptionFactory.of(response);
        }

        newsCollection = gson.fromJson(response.body(), NewsCollection.class);
        int pages = newsCollection.getTotalResults() % NEWS_PER_PAGE_COUNT == 0 ?
                (newsCollection.getTotalResults() / NEWS_PER_PAGE_COUNT)
                : (newsCollection.getTotalResults() / NEWS_PER_PAGE_COUNT + 1);

        getNewsFromNextPages(pages, country, category, keywords);
        return newsCollection.getArticles();
    }

    private void getNewsFromNextPages(int pages, String country, String category, String... keywords)
            throws NewsClientException {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        try {
            for (int i = 2; i <= pages && i <= PAGES_COUNT; i++) {
                HttpRequest request1 = HttpRequest.newBuilder()
                        .uri(getUri(country, category, i, keywords))
                        .build();
                System.out.println(request1.uri());

                futures.add(client.sendAsync(request1, HttpResponse.BodyHandlers.ofString())
                        .thenApply(response -> {
                            if (response.statusCode() != HttpURLConnection.HTTP_OK) {
                                try {
                                    throw ExceptionFactory.of(response);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            return response.body();
                        }));

            }

            CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

            for (var response : futures) {
                NewsCollection news1 = gson.fromJson(response.get(), NewsCollection.class);
                newsCollection.addElements(news1.getArticles());
            }
        } catch (Exception e) {
            throw new NewsClientException("Could not retrieve news", e);
        }
    }

    private URI getUri(String country, String category, int pageNumber, String... keywords) throws URISyntaxException {
        StringBuilder query = new StringBuilder("q=");
        query.append(String.join("+", keywords));

        if (!country.isBlank()) {
            query.append("&country=").append(country);
        }

        if (!category.isBlank()) {
            query.append("&category=").append(category);
        }

        query.append(REQUEST_PAGE_SIZE);
        query.append(REQUEST_PAGE_NUMBER).append(pageNumber);

        return new URI(API_ENDPOINT_SCHEME,
                API_ENDPOINT_HOST,
                API_ENDPOINT_PATH,
                query + "&apiKey=" + apiKey,
                null);
    }

    private void assertKeywordsNotNull(String... keywords) {
        if (keywords == null || Arrays.stream(keywords).allMatch(String::isBlank)) {
            throw new IllegalArgumentException("Keywords cannot be null or blank");
        }
    }

}
