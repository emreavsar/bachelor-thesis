package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.EntityNotCreatedException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.template.QA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;


/**
 * Created by corina on 09.04.2015.
 */
public class QualityAttribute extends Controller {

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result createQA() {
        try {
            JsonNode json = request().body().asJson();
            return ok(Json.toJson(createVersionedQA(1, json)));
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public static Result getAllQAs() {
        return ok(Json.toJson(logics.template.QualityAttribute.getAllQAs()));
    }


    @SubjectPresent
    @Transactional
    public static Result getQAsByCatalog(long id) throws IOException {
        try {
            return ok(Json.toJson(logics.template.QualityAttribute.getQAsByCatalog(id)));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result updateQA() {
        try {
            JsonNode json = request().body().asJson();
            return ok(Json.toJson(logics.template.QualityAttribute.updateQA(json)));
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    private static QA createVersionedQA(int versionNumber, JsonNode json) throws EntityNotFoundException, MissingParameterException {
        return logics.template.QualityAttribute.createQA(versionNumber, json);
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result deleteQA(Long id) {
        try {
            logics.template.QualityAttribute.deleteQA(id);
            return status(202);
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public static Result getAllStandardCatalogQAs() {
        try {
            return ok(Json.toJson(logics.template.QualityAttribute.getQAsByCatalog(Long.parseLong("6000"))));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result cloneQA(long id) {
        try {
            return ok(Json.toJson(logics.template.QualityAttribute.cloneQA(id)));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (EntityNotCreatedException e) {
            return status(400, e.getMessage());
        }
    }
}
