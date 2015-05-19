package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import dao.models.UserDao;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.PasswordsNotMatchException;
import logics.authentication.Authenticator;
import models.authentication.Role;
import models.authentication.Token;
import models.authentication.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class Authentication extends Controller {
    @Inject
    private Authenticator authenticator;

    @Transactional
    public Result login() {
        Logger.info("login called");
        DynamicForm requestData = Form.form().bindFromRequest();
        String username = requestData.get("username");
        String password = requestData.get("password");
        String token = requestData.get("token");

        Token t = null;
        try {
            t = authenticator.authenticate(username, password, token);
            session("username", t.getUser().getName());
            session("userid", String.valueOf(t.getUser().getId()));
            return ok(Json.toJson(t));
        } catch (EntityNotFoundException e) {
            return status(422, e.getMessage());
        } catch (InvalidParameterException e) {
            // TODO emre: multiple catch with same body can be unified maybe?
            return status(400, e.getMessage());
        } catch (PasswordsNotMatchException e) {
            return status(400, e.getMessage());
        }
    }

    @Transactional
    public Result register() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String username = requestData.get("username");
        String password = requestData.get("password");

        ArrayList<Role> roles = new ArrayList<>();
        String message = null;
        try {
            User registeredUser = authenticator.registerUser(username, password);
            return ok(Json.toJson(registeredUser));
        } catch (EntityAlreadyExistsException e) {
            return notFound(e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public Result logout() {
        Logger.info("logout called");

        DynamicForm requestData = Form.form().bindFromRequest();
        String username = requestData.get("username");
        String token = requestData.get("token");

        try {
            authenticator.invalidateUserSession(username, token);
            session().remove("user");
            return ok();
        } catch (EntityNotFoundException e) {
            return notFound(e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public Result changePassword() {
        Logger.info("changePassword called");

        DynamicForm requestData = Form.form().bindFromRequest();
        String username = session().get("user");
        String currentPassword = requestData.get("currentPassword");
        String newPassword = requestData.get("newPassword");
        String newPasswordRepeated = requestData.get("newPasswordRepeated");


        // TODO refactor to Authenticator.java or get user better out of the session!
        UserDao userDao = new UserDao();
        User user = userDao.findByUsername(username);

        try {
            if (!authenticator.checkPassword(user, currentPassword)) {
                return status(400, "User and password do not match");
            }
            authenticator.changePassword(username, newPassword, newPasswordRepeated);
            return ok();
        } catch (PasswordsNotMatchException e) {
            // TODO emre: multiple catch with same body can be unified maybe?
            return status(400, e.getMessage());
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }
}