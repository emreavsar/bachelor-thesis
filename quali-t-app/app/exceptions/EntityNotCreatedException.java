package exceptions;

/**
 * Created by emre on 13/04/15.
 */
public class EntityNotCreatedException extends AbstractException {
    public EntityNotCreatedException(String message) {
        super(message, 400);
    }
}


