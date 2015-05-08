package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.EntityNotFoundException;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
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
        Long parentId = json.findValue("parent").asLong();
        ((ObjectNode) json).remove("parent");
        models.template.QACategory ent = Json.fromJson(json, models.template.QACategory.class);
        Logger.debug(ent.toString());
        if (parentId == 0) {
            try {
                return ok(Json.toJson(logics.template.QACategoryLogic.createCategory(ent)));
            } catch (Exception e) {
                return status(400, e.getMessage());
            }
        } else {
            try {
                return ok(Json.toJson(logics.template.QACategoryLogic.createSubCategory(ent, parentId)));
            } catch (EntityNotFoundException e) {
                return status(400);
            }
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
        }
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result updateCat() {
        DynamicForm requestData = Form.form().bindFromRequest();
        Long id = Long.parseLong(requestData.get("id"));
        String name = requestData.get("name");
        String icon = requestData.get("icon");
        try {
            return ok(Json.toJson(logics.template.QACategoryLogic.updateCat(id, name, icon)));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }
}
