package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.JIRAConnectionLogic;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by corina on 13.05.2015.
 */
public class JIRAConnectionController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private JIRAConnectionLogic jiraConnectionLogic;
    @Inject
    private JsonConverter jsonConverter;

    public Result export() {
        return play.mvc.Results.TODO;
    }

    @SubjectPresent
    @Transactional
    public Result getAllJIRAConnections() {
        return ok(Json.toJson(jiraConnectionLogic.getAllJIRAConnections()));
    }

    @SubjectPresent
    @Transactional
    public Result deleteJIRAConnection(Long id) {
        return catchAbstractException(id, jiraConnectionId -> {
            jiraConnectionLogic.deleteJIRAConnection(jiraConnectionId);
            return status(202);
        });
    }

    @SubjectPresent
    @Transactional
    public Result createJIRAConnection() {
        return catchAbstractException(request(), json -> ok(Json.toJson(jiraConnectionLogic.createJIRAConnection(jsonConverter.getJiraConnectionFromJson(json)))));
    }

    @SubjectPresent
    @Transactional
    public Result updateJIRAConnection() {
        return catchAbstractException(request(), json -> ok(Json.toJson(jiraConnectionLogic.updateJIRAConnection(jsonConverter.getJiraConnectionFromJson(json)))));
    }
}
