package logics.user;

import com.google.inject.Inject;
import dao.models.UserDao;
import dao.user.TaskDao;
import exceptions.EntityNotFoundException;
import models.authentication.User;
import models.user.Task;

import java.util.List;

/**
 * Created by emre on 27/04/15.
 */
public class TaskLogic {
    @Inject
    TaskDao taskDao;

    @Inject
    UserDao userDao;

    public models.user.Task changeState(Long taskId) throws EntityNotFoundException {
        models.user.Task t = taskDao.readById(taskId);
        t.toggleState();
        taskDao.persist(t);
        return t;
    }

    public List<Task> getTasksOfUser(long userid) throws EntityNotFoundException {
        User u = userDao.readById(userid);

        if (u == null) {
            throw new EntityNotFoundException("User not found");
        }

        return u.getTasks();
    }
}
