package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import exceptions.EntityNotFoundException;
import logics.authentication.Authenticator;
import logics.project.ProjectLogic;
import models.authentication.User;
import models.project.Project;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Set;


public class FavoriteController extends Controller {
    @Inject
    ProjectLogic projectLogic;
    @Inject
    private Authenticator authenticator;

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
}
