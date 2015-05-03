package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import exceptions.EntityNotFoundException;
import models.project.QualityProperty;
import play.Logger;
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
        Logger.info("create called ########################################################");
        JsonNode json = request().body().asJson();

        String name = json.findValue("name").asText();
        Long customerId = json.findValue("customer").asLong();
        Long catalogId = json.findValue("catalog").asLong();

        JsonNode node = json.findValue("qas");
        List<String> list = node.findValuesAsText("id");
        List<Long> qaIds = Lists.transform(list, Helper.parseLongFunction());

        node = json.findValue("qps");
        list = node.findValuesAsText("id");
        List<Long> qpIds = Lists.transform(list, Helper.parseLongFunction());
        Logger.info("create called");
        Logger.info(name + " " + customerId + " " + catalogId + " " + qaIds.toString() + " " + qpIds.toString());
        try {
            models.project.Project project = logics.project.Project.createProject(name, customerId, catalogId, qaIds, qpIds);
            return ok(Json.toJson(project));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public static Result getAllQualityProperties() {
        List<QualityProperty> qualityProperties = logics.project.QualityProperty.getAllQualityProperties();
        return ok(Json.toJson(qualityProperties));
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
