package controllers;

import api.DetectorService;
import com.google.inject.Inject;
import dao.models.ProjectDAO;
import exceptions.EntityNotFoundException;
import models.project.Project;
import models.project.nfritem.Instance;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;

/**
 * Created by emre on 21/05/15.
 */
public class ValidateInstanceController extends Controller implements ExceptionHandlingInterface {
    @Inject
    DetectorService detectorService;

    @Inject
    ProjectDAO projectDao;

    @Transactional(readOnly = true)
    public Result validate(Long projectId) {
        Logger.info("validate called");

        ArrayList<Instance> qaInstances = new ArrayList<>();

        // TODO move to functional interface
        try {
            Project project = projectDao.readById(projectId);
            ArrayList<Instance> instances = new ArrayList<>(project.getQualityAttributes());
            return ok(Json.toJson(detectorService.validateAll(instances)));
        } catch (EntityNotFoundException e) {
            return status(e.getStatusCode(), e.getMessage());
        }
    }
}
