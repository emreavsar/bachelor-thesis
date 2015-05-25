package exceptions;

/**
 * Created by corina on 21.05.2015.
 */
public class InvalidConnectionParameter extends AbstractException {
    public InvalidConnectionParameter(String message) {
        super(message, 422);
    }
}