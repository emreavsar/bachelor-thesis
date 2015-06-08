package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.project.ProjectInitiatorLogic;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.JsonConverter;

/**
 * Created by corina on 01.04.2015.
 */
public class ProjectInitiatorController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private ProjectInitiatorLogic projectInitiatorLogic;
    @Inject
    private JsonConverter jsonConverter;

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public Result createProjectInitiator() {
        return catchAbstractException(request(), json -> ok(Json.toJson(projectInitiatorLogic.createProjectInitiator(jsonConverter.getProjectInitiatorFromJson(json)))));
    }

    @SubjectPresent
    @Transactional
    public Result getAllProjectInitiators() {
        Logger.info("getAllProjectInitiators ProjectInitiator called");
        return ok(Json.toJson(projectInitiatorLogic.getAllProjectInitiators()));
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public Result updateProjectInitiator() {
        return catchAbstractException(request(), json -> ok(Json.toJson(projectInitiatorLogic.updateProjectInitiator(jsonConverter.getProjectInitiatorFromJson(json)))));
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public Result deleteProjectInitiator(Long id) {
        return catchAbstractException(id, projectInitiatorId -> {
            projectInitiatorLogic.deleteProjectInitiator(projectInitiatorId);
            return status(202);
        });
    }
}
