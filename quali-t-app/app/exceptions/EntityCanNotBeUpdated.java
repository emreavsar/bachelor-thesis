package exceptions;

/**
 * Created by corina on 16.05.2015.
 */
public class EntityCanNotBeUpdated extends AbstractException {
    public EntityCanNotBeUpdated(String message) {
        super(message, 403);
    }
}
