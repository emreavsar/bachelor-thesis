package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.EntityNotCreatedException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.template.QA;
import models.template.QAVar;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;
import java.util.List;


/**
 * Created by corina on 09.04.2015.
 */
public class QualityAttribute extends Controller implements ExceptionHandlingInterface {

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result createQA() {
        return catchAbstractException(request(), json -> {
            QA qa = Converter.getQaFromJson(json);
            List<Long> categoryIds = Converter.getQaCategoriesFromJson(json);
            List<QAVar> qaVars = VariableConverter.getVarsFromJson(json);
            // create QA with all relations
            return ok(Json.toJson(logics.template.QualityAttribute.createQA(qa, categoryIds, qaVars)));
        });
    }

    @SubjectPresent
    @Transactional
    public static Result getAllQAs() {
        return ok(Json.toJson(logics.template.QualityAttribute.getAllQAs()));
    }


    @SubjectPresent
    @Transactional
    public Result getQAsByCatalog(Long id) throws IOException {
        return catchAbstractException(id, theId -> {
            return ok(Json.toJson(logics.template.QualityAttribute.getQAsByCatalog(theId)));
        });
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result updateQA() {
        try {
            //convert JSON to Objects
            JsonNode json = request().body().asJson();
            QA qa = Converter.getQaFromJson(json);
            List<Long> categoryIds = Converter.getQaCategoriesFromJson(json);
            List<QAVar> qaVars = VariableConverter.getVarsFromJson(json);
            return ok(Json.toJson(logics.template.QualityAttribute.updateQA(qa, categoryIds, qaVars)));
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result deleteQA(Long id) {
        try {
            logics.template.QualityAttribute.deleteQA(id);
            return status(202);
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public static Result getAllStandardCatalogQAs() {
        try {
            return ok(Json.toJson(logics.template.QualityAttribute.getQAsByCatalog(new Long(-6000))));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
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
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }
}
