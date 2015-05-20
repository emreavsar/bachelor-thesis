package logics.authentication;

import com.google.inject.Inject;
import dao.models.UserDao;
import exceptions.EntityNotFoundException;
import models.authentication.User;

import java.util.List;

/**
 * Created by emre on 25/04/15.
 */
public class UserTaskLogic {
    @Inject
    UserDao userDao;

    public List<models.user.Task> getTasksOfUser(long userid) throws EntityNotFoundException {
        User u = userDao.readById(userid);

        if (u == null) {
            throw new EntityNotFoundException("User not found");
        }

        return u.getTasks();
    }
}
