package exceptions;

/**
 * Created by emre on 20/05/15.
 */
public class AbstractException extends Exception {
    private int statusCode;

    public AbstractException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
