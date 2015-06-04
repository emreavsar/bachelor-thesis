package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.template.QACategoryLogic;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.JsonConverter;

/**
 * Created by corina on 06.05.2015.
 */
public class QACategoryController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private QACategoryLogic qaCategoryLogic;
    @Inject
    private JsonConverter jsonConverter;

    @SubjectPresent
    @Transactional
    public Result getCategoryTree(long id) {
        return catchAbstractException(id, categoryId -> ok(Json.toJson(qaCategoryLogic.getCategoryTree(categoryId))));
    }

    @SubjectPresent
    @Transactional
    public Result getAllCats() {
        return ok(Json.toJson(qaCategoryLogic.getAllCats()));
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result createCat() {
        return catchAbstractException(request(), json -> ok(Json.toJson(qaCategoryLogic.createCategory(jsonConverter.getCategoryFromJson(json)))));
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result deleteCat(Long id) {
        return catchAbstractException(id, categoryId -> {
            qaCategoryLogic.deleteCategory(categoryId);
            return status(202);
        });
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result updateCat() {
        return catchAbstractException(request(), json -> ok(Json.toJson(qaCategoryLogic.updateCategory(jsonConverter.getCategoryFromJson(json)))));
    }
}
