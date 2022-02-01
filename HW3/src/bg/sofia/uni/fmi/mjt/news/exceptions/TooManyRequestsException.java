package bg.sofia.uni.fmi.mjt.news.exceptions;

public class TooManyRequestsException extends Exception {

    public TooManyRequestsException(String message) {
        super(message);
    }
}
