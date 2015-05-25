package exceptions;

/**
 * Created by corina on 25.05.2015.
 */
public class CouldNotConvertException extends AbstractException {
    public CouldNotConvertException(String message) {
        super(message, 500);
    }
}
