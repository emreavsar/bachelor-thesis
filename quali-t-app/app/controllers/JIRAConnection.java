package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by corina on 13.05.2015.
 */
public class JIRAConnection extends Controller {
    @SubjectPresent
    @Transactional
    public static Result getAllJIRAConnections() {
        return ok(Json.toJson(logics.JIRAConnectionLogic.getAllJIRAConnections()));
    }

    @SubjectPresent
    @Transactional
    public static Result deleteJIRAConnection(Long id) {
        try {
            logics.JIRAConnectionLogic.deleteJIRAConnection(id);
            return status(202);
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public static Result createJIRAConnection() {
        JsonNode json = request().body().asJson();
        try {
            return ok(Json.toJson(logics.JIRAConnectionLogic.createJIRAConnection(Converter.getJiraConnectionFromJson(json))));
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public static Result updateJIRAConnection() {
        try {
            JsonNode json = request().body().asJson();
            return ok(Json.toJson(logics.JIRAConnectionLogic.updateJIRAConnection(Converter.getJiraConnectionFromJson(json))));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());

        }
    }
}
