package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.project.nfritem.Instance;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;


/**
 * Created by corina on 10.04.2015.
 */
public class Project extends Controller {

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result createProject() {
        JsonNode json = request().body().asJson();
        try {
            models.project.Project project = Converter.getProjectFromJson(json);
            List<Long> qualityAttributeIdList = Converter.getQualityAttributeIdsFromJson(json);
            List<Long> qualityPropertyIdList = Converter.getQualityPropertiesFromJson(json);
            return ok(Json.toJson(logics.project.Project.createProject(project, qualityAttributeIdList, qualityPropertyIdList)));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public static Result getAllProjects() {
        List<models.project.Project> projects = logics.project.Project.getAllProjects();
        return ok(Json.toJson(projects));
    }

    @SubjectPresent
    @Transactional
    public static Result getProject(long id) {
        try {
            models.project.Project project = logics.project.Project.getProject(id);
            return ok(Json.toJson(project));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result deleteProject(Long id) {
        try {
            logics.project.Project.deleteProject(id);
            return status(202);
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result updateProject() {
        JsonNode json = request().body().asJson();
        try {
            models.project.Project project = Converter.getCompleteProjectFromJson(json);
            List<Long> qualityPropertyList = Converter.getQualityPropertiesFromJson(json);
            return ok(Json.toJson(logics.project.Project.updateProject(project, qualityPropertyList)));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result createInstance() {
        JsonNode json = request().body().asJson();
        try {
            models.project.Project project = logics.project.Project.createInstance(new Instance());
            return ok(Json.toJson(project));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result deleteInstance(Long id) {
        try {
            logics.project.Project.deleteInstance(id);
            return status(202);
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result updateInstance() {
        JsonNode json = request().body().asJson();
        Instance instance = Converter.getInstanceFromJson(json);
        try {
            return ok(Json.toJson(logics.project.Project.updateInstance(instance)));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result cloneInstance(Long id) {
        try {
            return ok(Json.toJson(logics.project.Project.cloneInstance(id)));
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }
}
