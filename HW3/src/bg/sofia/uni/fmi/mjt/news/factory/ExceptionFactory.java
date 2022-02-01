package bg.sofia.uni.fmi.mjt.news.factory;

import bg.sofia.uni.fmi.mjt.news.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.news.exceptions.NewsClientException;
import bg.sofia.uni.fmi.mjt.news.exceptions.ServerErrorException;
import bg.sofia.uni.fmi.mjt.news.exceptions.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.news.exceptions.UnauthorizedApiKeyException;

import java.net.HttpURLConnection;
import java.net.http.HttpResponse;

public class ExceptionFactory {

    private static final int TOO_MANY_REQUESTS = 429;
    private static final int SERVER_ERROR = 500;

    public static Exception of(HttpResponse<String> response) {

        return switch (response.statusCode()) {
            case HttpURLConnection.HTTP_BAD_REQUEST -> new BadRequestException(response.body());
            case HttpURLConnection.HTTP_UNAUTHORIZED -> new UnauthorizedApiKeyException(response.body());
            case TOO_MANY_REQUESTS -> new TooManyRequestsException(response.body());
            case SERVER_ERROR -> new ServerErrorException(response.body());
            default -> new NewsClientException(response.body());
        };
    }
}
