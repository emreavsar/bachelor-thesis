package controllers;

import exceptions.EntityNotFoundException;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;


public class Task extends Controller {

    @Transactional
    public static Result getTasksOfUser(long userid) {
        Logger.info("getTasksOfUser called");
        try {
            List<models.task.Task> tasks = logics.authentication.Task.getTasksOfUser(userid);
            return ok(Json.toJson(tasks));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }
}
