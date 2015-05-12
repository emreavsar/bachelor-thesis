package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by corina on 06.05.2015.
 */
public class QualityProperty extends Controller {
    @SubjectPresent
    @Transactional
    public static Result getAllQualityProperties() {
        List<models.project.QualityProperty> qualityProperties = logics.project.QualityProperty.getAllQualityProperties();
        return ok(Json.toJson(qualityProperties));
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result createQualityProperty() {
        try {
            JsonNode json = request().body().asJson();
            return ok(Json.toJson(logics.project.QualityProperty.createQualityProperty(json)));
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result updateQualityProperty() {
        try {
            JsonNode json = request().body().asJson();
            return ok(Json.toJson(logics.project.QualityProperty.updateQualityProperty(json)));
        } catch (Exception e) {
            return status(400, e.getMessage());
        }
    }

    private static models.project.QualityProperty readJson() {
        JsonNode json = request().body().asJson();
        models.project.QualityProperty ent = Json.fromJson(json, models.project.QualityProperty.class);
        Logger.debug(ent.toString());
        return ent;
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result deleteQualityProperty(long id) {
        try {
            logics.project.QualityProperty.deleteQualityProperty(id);
            return status(202);
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }

    }
}
