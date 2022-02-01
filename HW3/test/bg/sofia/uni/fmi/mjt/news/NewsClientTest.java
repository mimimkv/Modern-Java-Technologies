package bg.sofia.uni.fmi.mjt.news;

import bg.sofia.uni.fmi.mjt.news.dto.Article;
import bg.sofia.uni.fmi.mjt.news.dto.NewsCollection;
import bg.sofia.uni.fmi.mjt.news.dto.Source;
import bg.sofia.uni.fmi.mjt.news.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsClientException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ServerErrorException;
import bg.sofia.uni.fmi.mjt.news.exceptions.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.news.exceptions.UnauthorizedApiKeyException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewsClientTest {

    private NewsClient client;
    private static Gson gson;

    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    @BeforeAll
    public static void setUpGson() {
        gson = new GsonBuilder().create();
    }

    @BeforeEach
    public void setUp() {
        client = new NewsClient(httpClientMock);
    }

    @Test
    public void testGetArticlesKeywordsAreEmptyThrowsException() {
        RequestParameter parameter = RequestParameter.builder(new String[]{" ", ""}).build();
        assertThrows(IllegalArgumentException.class, () -> client.getArticles(parameter),
                "IllegalArgumentException expected when keywords are null or empty");
    }

    @Test
    public void testGetArticlesBadRequestException() throws IOException, InterruptedException {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        assertThrows(BadRequestException.class,
                () -> client.getArticles(RequestParameter.builder(new String[]{"random"}).build()),
                "BadRequestException expected when a parameter is missing or not configured");
    }

    @Test
    public void testGetArticlesServerErrorException() throws IOException, InterruptedException {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        int serverErrorCode = 500;
        when(httpResponseMock.statusCode()).thenReturn(serverErrorCode);
        assertThrows(ServerErrorException.class,
                () -> client.getArticles(RequestParameter.builder(new String[]{"random"}).build()),
                "ServerErrorException expected when something went wrong on the server");
    }

    @Test
    public void testGetArticlesTooManyRequestsException() throws IOException, InterruptedException {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        int tooManyRequestsCode = 429;
        when(httpResponseMock.statusCode()).thenReturn(tooManyRequestsCode);
        assertThrows(TooManyRequestsException.class,
                () -> client.getArticles(RequestParameter.builder(new String[]{"random"}).build()),
                "TooManyRequestsException expected when limit of requests is exceeded");
    }

    @Test
    public void testGetArticlesUnauthorizedException() throws IOException, InterruptedException {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);
        assertThrows(UnauthorizedApiKeyException.class,
                () -> client.getArticles(RequestParameter.builder(new String[]{"random"}).build()),
                "UnauthorizedApiKeyException expected when API key is missing or is not correct");
    }

    @Test
    public void testGetArticlesWhenNewsClientExceptionIsThrown() throws IOException, InterruptedException {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        int randomErrorCode = 439;
        when(httpResponseMock.statusCode()).thenReturn(randomErrorCode);
        assertThrows(NewsClientException.class,
                () -> client.getArticles(RequestParameter.builder(new String[]{"random"}).build()),
                "NewsClientException expected when some error occurred");
    }

    @Test
    public void testGetArticlesOneArticleReturned() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        List<Article> expected = Collections.singletonList(new Article(
                new Source("01", "bbc"), "john", "New shop",
                "H&M opens this weekend at Paradise center", "some url", "url to image",
                "12.02.2022",
                "some content"
        ));

        NewsCollection news = new NewsCollection("ok", 1, expected);

        String json = gson.toJson(news);
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(json);

        List<Article> actual = client.getArticles(RequestParameter.builder(new String[]{"random"})
                .setCategory("sports").setCountry("us").build());

        assertIterableEquals(expected, actual,
                "getArticles does not return the correct collection of articles when one article matches the search");
    }


    @Test
    public void testGetArticlesTwoArticlesReturned() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        List<Article> expected = List.of(new Article(
                        new Source("01", "bbc"), "john", "New shop",
                        "H&M opens this weekend at Paradise center", "some url", "url to image",
                        "12.02.2022",
                        "some content"),
                new Article(new Source("02", "bbc"), "john", "New shop",
                        "some description", "some url", "url to image",
                        "18.02.2022",
                        "some content"));

        NewsCollection news = new NewsCollection("ok", 1, expected);

        String json = gson.toJson(news);
        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(json);

        List<Article> actual = client.getArticles(RequestParameter.builder(new String[]{"random"}).build());

        assertIterableEquals(expected, actual,
                "getArticles does not return the correct collection of articles when two articles match the search");
    }


}