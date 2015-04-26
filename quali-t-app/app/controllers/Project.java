package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
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

    @Transactional
    public static Result createProject() {
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

        Logger.info(name + " " + customerId+ " " + catalogId+ " " + qaIds.toString()+ " " + qpIds.toString());
        models.project.Project project = logics.project.Project.createProject(name, customerId, catalogId, qaIds, qpIds);
//        models.template.Catalog catalog = logics.template.Catalog.create(name, image, qas_id);
        if(name.equals("") | qaIds.isEmpty()) {
            return badRequest("Missing parameter [name] or no selected QAs");
        } else {
            return ok("Name: " + name + " QAs: " + qaIds + " QPs: " + qpIds);
        }
    }

    @Transactional
    public static Result getAllQualityProperties() {
        List<QualityProperty> qualityProperties = logics.project.Project.getAllQualityProperties();
        return ok(Json.toJson(qualityProperties));
    }
}
