package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.project.QualityPropertyLogic;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by corina on 06.05.2015.
 */
public class QualityPropertyController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private QualityPropertyLogic qualityPropertyLogic;
    @Inject
    private JsonConverter jsonConverter;

    @SubjectPresent
    @Transactional
    public Result getAllQualityProperties() {
        return ok(Json.toJson(qualityPropertyLogic.getAllQualityProperties()));
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public Result createQualityProperty() {
        return catchAbstractException(request(), json -> ok(Json.toJson(qualityPropertyLogic.createQualityProperty(jsonConverter.getQualityPropertyFromJson(json)))));
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public Result updateQualityProperty() {
        return catchAbstractException(request(), json -> ok(Json.toJson(qualityPropertyLogic.updateQualityProperty(jsonConverter.getQualityPropertyFromJson(json)))));
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public Result deleteQualityProperty(long id) {
        return catchAbstractException(id, qualityPropertyId -> {
            qualityPropertyLogic.deleteQualityProperty(qualityPropertyId);
            return status(202);
        });
    }
}
