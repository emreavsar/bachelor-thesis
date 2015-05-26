package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import exceptions.EntityNotFoundException;
import logics.user.TaskLogic;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;


public class TaskController extends Controller {
    @Inject
    private TaskLogic taskLogic;

    @SubjectPresent
    @Transactional
    public Result getTasksOfCurrentUser() {
        Logger.info("getTasksOfCurrentUser called");
        try {
            long userid = Long.parseLong(session().get("userid"));
            List<models.user.Task> tasks = taskLogic.getTasksOfUser(userid);
            return ok(Json.toJson(tasks));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public Result toggleStateOfTask() {
        Logger.info("toggleStateOfTask called");

        DynamicForm requestData = Form.form().bindFromRequest();
        Long taskId = Long.valueOf(requestData.get("taskId"));

        try {
            models.user.Task t = taskLogic.changeState(taskId);
            return ok(Json.toJson(t));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }
}
