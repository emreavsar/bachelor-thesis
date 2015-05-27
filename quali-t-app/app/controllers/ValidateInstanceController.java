package controllers;

import api.DetectorService;
import com.google.inject.Inject;
import logics.project.ProjectLogic;
import models.project.nfritem.Instance;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by emre on 21/05/15.
 */
public class ValidateInstanceController extends Controller implements ExceptionHandlingInterface {
    @Inject
    DetectorService detectorService;
    @Inject
    ProjectLogic projectLogic;

    @Transactional(readOnly = true)
    public Result validate(Long id) {
        Logger.info("validate called");
        return catchAbstractException(id, projectId -> {
            List<Instance> instances = projectLogic.getQualityAttributes(projectId);
            return ok(Json.toJson(detectorService.validateAll(instances)));
        });
    }
}
