package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.interfaces.JIRAConnectionLogic;
import logics.interfaces.JIRAExportLogic;
import models.project.Project;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.JsonConverter;

import java.util.List;

/**
 * Created by corina on 13.05.2015.
 */
public class JIRAConnectionController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private JIRAConnectionLogic jiraConnectionLogic;
    @Inject
    private JsonConverter jsonConverter;
    @Inject
    private JIRAExportLogic jiraExportLogic;

    @SubjectPresent
    @Transactional
    public Result getAllJIRAConnections() {
        Logger.info("getAllJIRAConnections called");
        return ok(Json.toJson(jiraConnectionLogic.getAllJIRAConnections()));
    }

    @Restrict({@Group("admin"), @Group("projectmanager")})
    @Transactional
    public Result deleteJIRAConnection(Long id) {
        Logger.info("deleteJIRAConnection called");
        return catchAbstractException(id, jiraConnectionId -> {
            jiraConnectionLogic.deleteJIRAConnection(jiraConnectionId);
            return status(202);
        });
    }

    @Restrict({@Group("admin"), @Group("projectmanager")})
    @Transactional
    public Result createJIRAConnection() {
        Logger.info("createJIRAConnection called");
        return catchAbstractException(request(), json -> ok(Json.toJson(jiraConnectionLogic.createJIRAConnection(jsonConverter.getJiraConnectionFromJson(json)))));
    }

    @Restrict({@Group("admin"), @Group("projectmanager")})
    @Transactional
    public Result updateJIRAConnection() {
        Logger.info("updateJIRAConnection called");
        return catchAbstractException(request(), json -> ok(Json.toJson(jiraConnectionLogic.updateJIRAConnection(jsonConverter.getJiraConnectionFromJson(json)))));
    }

    @Restrict({@Group("analyst"), @Group("admin"), @Group("synthesizer"), @Group("evaluator"), @Group("projectmanager")})
    @Transactional
    public Result export() {
        return catchAbstractException(request(), json -> {
            Logger.info("export called");
            Project project = jsonConverter.getProjectIdFromJson(json);
            List<Long> qualityAttributesToExport = jsonConverter.getQualityAttributeIdsToExportFromJson(json);
            Boolean exportAsRaw = jsonConverter.getExportAsRawBoolean(json);
            return ok(Json.toJson(jiraExportLogic.exportToJira(project, qualityAttributesToExport, exportAsRaw)));
        });
    }
}
