package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.user.UserLogic;
import models.authentication.User;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by corina on 02.06.2015.
 */
public class UserController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private UserLogic userLogic;
    @Inject
    private JsonConverter jsonConverter;

    @SubjectPresent
    @Transactional
    public Result getAllRoles() {
        return ok(Json.toJson(userLogic.getAllRoles()));
    }

    @SubjectPresent
    @Transactional
    public Result createUser() {
        return catchAbstractException(request(), json -> {
            User user = jsonConverter.getUserFromJson(json);
            List<Long> roleIds = jsonConverter.getUserRolesFromJson(json);
            return ok(Json.toJson(userLogic.createUser(user, roleIds)));
        });
    }

    @SubjectPresent
    @Transactional
    public Result updateUser() {
        return catchAbstractException(request(), json -> {
            User user = jsonConverter.getUserFromJson(json);
            List<Long> roleIds = jsonConverter.getUserRolesFromJson(json);
            return ok(Json.toJson(userLogic.updateUser(user, roleIds)));
        });
    }

    @SubjectPresent
    @Transactional
    public Result deleteUser(Long id) {
        return catchAbstractException(id, userId -> {
            userLogic.deleteUser(userId);
            return status(202);
        });
    }

    @SubjectPresent
    @Transactional
    public Result getAllUsers() {
        return ok(Json.toJson(userLogic.getAllUsers()));
    }
}
