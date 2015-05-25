package exceptions;

/**
 * Created by corina on 28.04.2015.
 */

public class MissingParameterException extends AbstractException {
    public MissingParameterException(String message) {
        super(message, 422);
    }
}