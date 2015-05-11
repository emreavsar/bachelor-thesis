package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.EntityNotFoundException;
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
            models.project.Project project = logics.project.Project.createProject(json);
            return ok(Json.toJson(project));
        } catch (EntityNotFoundException e) {
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
}
