package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.user.UserLogic;
import models.authentication.User;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.JsonConverter;

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
        Logger.info("getAllRoles called");
        return ok(Json.toJson(userLogic.getAllRoles()));
    }

    @Restrict(@Group("admin"))
    @Transactional
    public Result createUser() {
        Logger.info("createUser called");
        return catchAbstractException(request(), json -> {
            User user = jsonConverter.getUserFromJson(json);
            List<Long> roleIds = jsonConverter.getUserRolesFromJson(json);
            return ok(Json.toJson(userLogic.createUser(user, roleIds)));
        });
    }

    @Restrict(@Group("admin"))
    @Transactional
    public Result updateUser() {
        Logger.info("updateUser called");
        return catchAbstractException(request(), json -> {
            User user = jsonConverter.getUserFromJson(json);
            List<Long> roleIds = jsonConverter.getUserRolesFromJson(json);
            return ok(Json.toJson(userLogic.updateUser(user, roleIds)));
        });
    }

    @Restrict(@Group("admin"))
    @Transactional
    public Result deleteUser(Long id) {
        Logger.info("deleteUser called");
        return catchAbstractException(id, userId -> {
            userLogic.deleteUser(userId);
            return status(202);
        });
    }

    @SubjectPresent
    @Transactional
    public Result getAllUsers() {
        Logger.info("getAllUsers called");
        return ok(Json.toJson(userLogic.getAllUsers()));
    }
}
