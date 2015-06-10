package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.authentication.Authenticator;
import logics.user.FavoriteLogic;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.JsonConverter;


public class FavoriteController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private FavoriteLogic favoriteLogic;
    @Inject
    private Authenticator authenticator;
    @Inject
    private JsonConverter jsonConverter;

    @SubjectPresent
    @Transactional
    public Result getFavoritesOfCurrentUser() {
        Logger.info("getFavoritesOfCurrentUser called");
        return catchAbstractException(() -> {
            Long userid = Long.parseLong(session().get("userid"));
            return ok(Json.toJson(authenticator.getUser(userid).getFavorites()));
        });
    }

    @SubjectPresent
    @Transactional
    public Result updateFavorite() {
        Logger.info("updateFavorite called");
        return catchAbstractException(request(), json -> {
            Long userId = Long.parseLong(session().get("userid"));
            return ok(Json.toJson(favoriteLogic.setFavoriteProject(userId, jsonConverter.getFavoriteProject(json))));
        });
    }
}
