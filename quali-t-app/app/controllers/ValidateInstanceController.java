package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.google.inject.Inject;
import logics.project.ValidationLogic;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by emre on 21/05/15.
 */
public class ValidateInstanceController extends Controller implements ExceptionHandlingInterface {
    @Inject
    ValidationLogic validationLogic;

    @Restrict({@Group("synthesizer"), @Group("admin"), @Group("evaluator"), @Group("analyst"), @Group("projectmanager")})
    @Transactional(readOnly = true)
    public Result validate(Long id) {
        Logger.info("validate called");
        return catchAbstractException(id, projectId -> {
            return ok(Json.toJson(validationLogic.validate(projectId)));
        });
    }
}