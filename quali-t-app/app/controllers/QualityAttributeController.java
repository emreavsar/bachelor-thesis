package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.template.QualityAttributeLogic;
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
public class QualityAttributeController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private QualityAttributeLogic qualityAttributeLogic;
    @Inject
    private JsonConverter jsonConverter;

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result createQA() {
        return catchAbstractException(request(), json -> {
            QA qa = jsonConverter.getQaFromJson(json);
            List<Long> categoryIds = jsonConverter.getQaCategoriesFromJson(json);
            List<QAVar> qaVars = VariableConverter.getVarsFromJson(json);
            // create QA with all relations
            return ok(Json.toJson(qualityAttributeLogic.createQA(qa, categoryIds, qaVars)));
        });
    }

    @SubjectPresent
    @Transactional
    public Result getAllQAs() {
        return ok(Json.toJson(qualityAttributeLogic.getAllQAs()));
    }


    @SubjectPresent
    @Transactional
    public Result getQAsByCatalog(Long id) throws IOException {
        return catchAbstractException(id, qualityAttributeId -> ok(Json.toJson(qualityAttributeLogic.getQAsByCatalog(qualityAttributeId))));
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result updateQA() {
        return catchAbstractException(request(), json -> {
            QA qa = jsonConverter.getQaFromJson(json);
            List<Long> categoryIds = jsonConverter.getQaCategoriesFromJson(json);
            List<QAVar> qaVars = VariableConverter.getVarsFromJson(json);
            return ok(Json.toJson(qualityAttributeLogic.updateQA(qa, categoryIds, qaVars)));
        });
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result deleteQA(Long id) {
        return catchAbstractException(id, qualityAttributeId -> {
            qualityAttributeLogic.deleteQA(qualityAttributeId);
            return status(202);
        });
    }

    @SubjectPresent
    @Transactional
    public Result getAllStandardCatalogQAs() {
        return catchAbstractException(() -> ok(Json.toJson(qualityAttributeLogic.getQAsByCatalog(new Long(-6000)))));
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result cloneQA(long id) {
        return catchAbstractException(id, qualityAttributeId -> ok(Json.toJson(qualityAttributeLogic.cloneQA(qualityAttributeId))));
    }
}
