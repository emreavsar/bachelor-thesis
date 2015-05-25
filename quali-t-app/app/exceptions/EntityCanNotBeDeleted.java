package exceptions;

/**
 * Created by corina on 10.05.2015.
 */
public class EntityCanNotBeDeleted extends AbstractException {
    public EntityCanNotBeDeleted(String message) {
        super(message, 403);
    }
}
