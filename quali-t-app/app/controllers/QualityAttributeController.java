package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.template.QualityAttributeLogic;
import models.template.QA;
import models.template.QAVar;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.GlobalVariables;
import util.JsonConverter;
import util.VariableConverter;

import java.io.IOException;
import java.util.List;


/**
 * Created by corina on 09.04.2015.
 */
public class QualityAttributeController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private QualityAttributeLogic qualityAttributeLogic;
    @Inject
    private JsonConverter jsonConverter;
    @Inject
    private VariableConverter variableConverter;

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result createQA() {
        Logger.info("createQA called");
        return catchAbstractException(request(), json -> {
            QA qa = jsonConverter.getQaFromJson(json);
            List<Long> categoryIds = jsonConverter.getQaCategoriesFromJson(json);
            List<QAVar> qaVars = variableConverter.getVarsFromJson(json);
            // create QA with all relations
            return ok(Json.toJson(qualityAttributeLogic.createQA(qa, categoryIds, qaVars)));
        });
    }

    @SubjectPresent
    @Transactional
    public Result getAllQAs() {
        Logger.info("getAllQAs called");
        return ok(Json.toJson(qualityAttributeLogic.getAllQAs()));
    }


    @SubjectPresent
    @Transactional
    public Result getQAsByCatalog(Long id) throws IOException {
        Logger.info("getQAsByCatalog called");
        return catchAbstractException(id, qualityAttributeId -> ok(Json.toJson(qualityAttributeLogic.getQAsByCatalog(qualityAttributeId))));
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result updateQA() {
        Logger.info("updateQA called");
        return catchAbstractException(request(), json -> {
            QA qa = jsonConverter.getQaFromJson(json);
            List<Long> categoryIds = jsonConverter.getQaCategoriesFromJson(json);
            List<QAVar> qaVars = variableConverter.getVarsFromJson(json);
            return ok(Json.toJson(qualityAttributeLogic.updateQA(qa, categoryIds, qaVars)));
        });
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result deleteQA(Long id) {
        Logger.info("deleteQA called");
        return catchAbstractException(id, qualityAttributeId -> {
            qualityAttributeLogic.deleteQA(qualityAttributeId);
            return status(202);
        });
    }

    @SubjectPresent
    @Transactional
    public Result getAllStandardCatalogQAs() {
        Logger.info("getAllStandardCatalogQAs called");
        return catchAbstractException(() -> ok(Json.toJson(qualityAttributeLogic.getQAsByCatalog(GlobalVariables.standardCatalog))));
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result cloneQA(long id) {
        Logger.info("cloneQA called");
        return catchAbstractException(id, qualityAttributeId -> ok(Json.toJson(qualityAttributeLogic.cloneQA(qualityAttributeId))));
    }
}
