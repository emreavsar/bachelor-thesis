package exceptions;

/**
 * Created by emre on 13/04/15.
 */
public class EntityNotFoundException extends AbstractException {
    // TODO change 400
    public EntityNotFoundException(String message) {
        super(message, 400);
    }
}


