package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.user.TaskLogic;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;


public class TaskController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private TaskLogic taskLogic;

    @SubjectPresent
    @Transactional
    public Result getTasksOfCurrentUser() {
        Logger.info("getTasksOfCurrentUser called");
        return catchAbstractException(() -> {
            long userid = Long.parseLong(session().get("userid"));
            return ok(Json.toJson(taskLogic.getTasksOfUser(userid)));
        });
    }

    @SubjectPresent
    @Transactional
    public Result toggleStateOfTask() {
        Logger.info("toggleStateOfTask called");
        return catchAbstractException(request(), json -> {
            return ok(Json.toJson(taskLogic.changeState(json.findPath("taskId").asLong())));
        });
    }
}
