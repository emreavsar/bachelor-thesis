package controllers;

import exceptions.EntityNotFoundException;
import logics.authentication.Authenticator;
import models.project.*;
import models.project.Project;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Misc extends Controller {

    @Transactional
    public static Result getTasksOfCurrentUser() {
        Logger.info("getTasksOfCurrentUser called");
        try {
            long userid = Long.parseLong(session().get("userid"));
            List<models.misc.user.Task> tasks = logics.authentication.Task.getTasksOfUser(userid);
            return ok(Json.toJson(tasks));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Transactional
    public static Result getFavoritesOfCurrentUser() {
        Logger.info("getFavoritesOfCurrentUser called");
        try {
            long userid = Long.parseLong(session().get("userid"));
            Set<Project> favorites = Authenticator.getUser(userid).getFavorites();
            return ok(Json.toJson(favorites));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }
}
