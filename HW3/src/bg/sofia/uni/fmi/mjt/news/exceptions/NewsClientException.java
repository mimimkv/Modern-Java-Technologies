package bg.sofia.uni.fmi.mjt.news.exceptions;


public class NewsClientException extends Exception {

    public NewsClientException(String message) {
        super(message);
    }

    public NewsClientException(String message, Exception e) {
        super(message, e);
    }
}
