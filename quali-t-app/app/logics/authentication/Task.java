package logics.authentication;

import dao.models.UserDao;
import exceptions.EntityNotFoundException;
import models.authentication.User;

import java.util.List;

/**
 * Created by emre on 25/04/15.
 */
public class Task {

    public static List<models.misc.user.Task> getTasksOfUser(long userid) throws EntityNotFoundException {
        UserDao userDao = new UserDao();
        User u = userDao.readById(userid);

        if (u == null) {
            throw new EntityNotFoundException("User not found");
        }

        return u.getTasks();
    }
}
