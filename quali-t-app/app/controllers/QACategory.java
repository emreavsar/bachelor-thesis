package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by corina on 06.05.2015.
 */
public class QACategory extends Controller {

    @SubjectPresent
    @Transactional
    public static Result getCategoryTree(long id) {
        try {
            return ok(Json.toJson(logics.template.QACategoryLogic.getCategoryTree(id)));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public static Result getAllCats() {
        List<models.template.QACategory> cats = logics.template.QACategoryLogic.getAllCats();
        return ok(Json.toJson(cats));
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result createCat() {
        JsonNode json = request().body().asJson();
            try {
                return ok(Json.toJson(logics.template.QACategoryLogic.createCategory(Converter.getCategoryFromJson(json))));
            } catch (Exception e) {
                return status(400, e.getMessage());
            }

    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result deleteCat(Long id) {
        try {
            logics.template.QACategoryLogic.deleteCategory(id);
            return status(202);
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result updateCat() {
        JsonNode json = request().body().asJson();
        try {
            return ok(Json.toJson(logics.template.QACategoryLogic.updateCat(Converter.getCategoryFromJson(json))));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }
}
