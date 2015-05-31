package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import dao.models.UserDao;
import logics.authentication.Authenticator;
import models.authentication.Token;
import models.authentication.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Authentication extends Controller implements ExceptionHandlingInterface {
    @Inject
    private Authenticator authenticator;
    @Inject
    private JsonConverter jsonConverter;

    @Transactional
    public Result login() {
        Logger.info("login called");
        return catchAbstractException(request(), json -> {
            User user = jsonConverter.getUserFromJson(json);
            Token t = authenticator.authenticate(user.getName(), user.getHashedPassword(), user.getToken().get(0).getToken());
            session("username", t.getUser().getName());
            session("userid", String.valueOf(t.getUser().getId()));
            return ok(Json.toJson(t));
        });
    }

    @Transactional
    public Result register() {
        return catchAbstractException(request(), json -> {
            User user = jsonConverter.getUserFromJson(json);
            return ok(Json.toJson(authenticator.registerUser(user.getName(), user.getHashedPassword())));
        });
    }

    @SubjectPresent
    @Transactional
    public Result logout() {
        Logger.info("logout called");
        return catchAbstractException(request(), json -> {
            User user = jsonConverter.getUserFromJson(json);
            authenticator.invalidateUserSession(user.getName(), user.getToken().get(0).getToken());
            session().remove("user");
            return status(204);
        });
    }

    @SubjectPresent
    @Transactional
    public Result changePassword() {
        Logger.info("changePassword called");
        return catchAbstractException(() -> {
            DynamicForm requestData = Form.form().bindFromRequest();
            String username = session().get("user");
            String currentPassword = requestData.get("currentPassword");
            String newPassword = requestData.get("newPassword");
            String newPasswordRepeated = requestData.get("newPasswordRepeated");
            UserDao userDao = new UserDao();
            User user = userDao.findByUsername(username);
            if (!authenticator.checkPassword(user, currentPassword)) {
                return status(400, "User and password do not match");
            }
            authenticator.changePassword(username, newPassword, newPasswordRepeated);
            return status(204);
        });
    }
}