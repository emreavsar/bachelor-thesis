package logics.user;

import com.google.inject.Inject;
import dao.user.TaskDao;
import exceptions.EntityNotFoundException;

/**
 * Created by emre on 27/04/15.
 */
public class TaskLogic {
    @Inject
    TaskDao taskDao;

    public models.user.Task changeState(Long taskId) throws EntityNotFoundException {
        models.user.Task t = taskDao.readById(taskId);
        t.toggleState();
        taskDao.persist(t);
        return t;
    }
}
