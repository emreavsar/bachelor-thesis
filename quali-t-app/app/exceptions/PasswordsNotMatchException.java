package exceptions;

/**
 * Created by emre on 13/04/15.
 */
public class PasswordsNotMatchException extends AbstractException {
    public PasswordsNotMatchException(String message) {
        super(message, 400);
    }
}


