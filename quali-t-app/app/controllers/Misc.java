package controllers;

import api.DetectorService;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import dao.models.QAInstanceDAO;
import exceptions.EntityNotFoundException;
import logics.authentication.Authenticator;
import logics.authentication.UserTaskLogic;
import logics.project.ProjectLogic;
import logics.user.TaskLogic;
import models.authentication.User;
import models.project.Project;
import models.project.nfritem.Instance;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


// TODO emre: rename this class!
public class Misc extends Controller {
    @Inject
    DetectorService detectorService;
    @Inject
    ProjectLogic projectLogic;
    @Inject
    private Authenticator authenticator;
    @Inject
    private UserTaskLogic userTaskLogic;
    @Inject
    private TaskLogic taskLogic;

    @SubjectPresent
    @Transactional
    public Result getTasksOfCurrentUser() {
        Logger.info("getTasksOfCurrentUser called");
        try {
            long userid = Long.parseLong(session().get("userid"));
            List<models.user.Task> tasks = userTaskLogic.getTasksOfUser(userid);
            return ok(Json.toJson(tasks));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public Result getFavoritesOfCurrentUser() {
        Logger.info("getFavoritesOfCurrentUser called");
        try {
            long userid = Long.parseLong(session().get("userid"));
            Set<Project> favorites = authenticator.getUser(userid).getFavorites();
            return ok(Json.toJson(favorites));
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

    @SubjectPresent
    @Transactional
    public Result updateFavorite() {
        Logger.info("updateFavorite called");
        try {
            DynamicForm requestData = Form.form().bindFromRequest();
            Long projectId = Long.valueOf(requestData.get("projectId"));

            long userid = Long.parseLong(session().get("userid"));
            boolean isFavorite = Boolean.parseBoolean(requestData.get("isFavorite"));
            Project projectToFavorite = projectLogic.getProject(projectId);

            User u;
            // add favorite
            if (isFavorite) {
                u = authenticator.getUser(userid).addToFavorites(projectToFavorite);
            } else { // remove favorite
                u = authenticator.getUser(userid).removeFromFavorites(projectToFavorite);
            }
            authenticator.update(u);

            return ok(Json.toJson(u));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Transactional
    public Result validate() {
        // TODO emre: move to a proper place
        Logger.info("Inside validate() for detectors");
        QAInstanceDAO qaInstanceDAO = new QAInstanceDAO();
        String result = detectorService.validateAll((ArrayList<Instance>) qaInstanceDAO.readAll());
        return ok(Json.toJson(result));
    }
}
