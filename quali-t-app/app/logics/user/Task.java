package logics.user;

import dao.user.TaskDao;
import exceptions.EntityNotFoundException;

/**
 * Created by emre on 27/04/15.
 */
public class Task {

    public static models.user.Task changeState(Long taskId) throws EntityNotFoundException {
        TaskDao taskDao = new TaskDao();
        models.user.Task t = taskDao.readById(taskId);
        t.toggleState();
        taskDao.persist(t);
        return t;
    }
}
