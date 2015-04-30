package exceptions;

/**
 * Created by corina on 29.04.2015.
 */
public class EntityAlreadyExistsException extends Exception {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}