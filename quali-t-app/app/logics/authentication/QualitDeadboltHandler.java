package logics.authentication;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import dao.models.UserDao;
import exceptions.EntityNotFoundException;
import models.authentication.User;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Created by emre on 06/04/15.
 */
public class QualitDeadboltHandler extends AbstractDeadboltHandler {
    @Override
    public F.Promise<Result> beforeAuthCheck(Http.Context context) {
        return F.Promise.pure(null);
    }

    @Override
    public Subject getSubject(Http.Context context) {
        try {
            return JPA.withTransaction(new play.libs.F.Function0<User>() {
                public User apply() throws EntityNotFoundException {
                    // TODO emre: move to userid instead of username -> easier
                    String u = context.session().get("username");
                    UserDao userDao = new UserDao();
                    User loggedInUser = userDao.findByUsername(u);
                    return loggedInUser;
                }
            });
        } catch (Throwable throwable) {
            play.Logger.error("Error at accessing the database.");
        }

        return null;
    }
}