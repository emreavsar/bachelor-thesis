package logics.user;

import com.google.inject.Inject;
import dao.models.UserDao;
import dao.user.TaskDao;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
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

    public models.user.Task changeState(Long taskId) throws EntityNotFoundException, MissingParameterException {
        if (taskId != null && taskId != 0) {
            models.user.Task t = taskDao.readById(taskId);
            t.toggleState();
            taskDao.persist(t);
            return t;
        }
        throw new MissingParameterException("Please provide a valid taskId!");
    }

    public List<Task> getTasksOfUser(long userid) throws EntityNotFoundException {
        User u = userDao.readById(userid);

        if (u == null) {
            throw new EntityNotFoundException("User not found");
        }

        return u.getTasks();
    }
}
