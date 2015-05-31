package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.authentication.Authenticator;
import logics.project.ProjectLogic;
import models.authentication.User;
import models.project.Project;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;


public class FavoriteController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private ProjectLogic projectLogic;
    @Inject
    private Authenticator authenticator;

    @SubjectPresent
    @Transactional
    public Result getFavoritesOfCurrentUser() {
        Logger.info("getFavoritesOfCurrentUser called");
        return catchAbstractException(() -> {
            long userid = Long.parseLong(session().get("userid"));
            return ok(Json.toJson(authenticator.getUser(userid).getFavorites()));
        });
    }

    @SubjectPresent
    @Transactional
    // TODO: Logik im Controller!!!!! In Logic Class auslagern und MissingParameterExceptions abfangen
    public Result updateFavorite() {
        Logger.info("updateFavorite called");
        return catchAbstractException(request(), json -> {
            long userId = Long.parseLong(session().get("userid"));
            boolean isFavorite = json.findPath("isFavorite").asBoolean();
            Project projectToFavorite = projectLogic.getProject(json.findPath("projectId").asLong());
            User u;
            // add favorite
            if (isFavorite) {
                u = authenticator.getUser(userId).addToFavorites(projectToFavorite);
            } else { // remove favorite
                u = authenticator.getUser(userId).removeFromFavorites(projectToFavorite);
            }
            authenticator.update(u);

            return ok(Json.toJson(u));
        });
    }
}
