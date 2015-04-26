package controllers;

import models.template.CatalogQA;
import models.template.QA;
import models.template.QACategory;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;


/**
 * Created by corina on 09.04.2015.
 */
public class QualityAttribute extends Controller {

    @Transactional
    public static Result createQA() {
        Logger.info("createQA called");
        DynamicForm requestData = Form.form().bindFromRequest();
        String qaText = requestData.get("qaText");

        QA qa = logics.template.QualityAttribute.createQA(qaText);
        if (qa != null) {
            return ok(Json.toJson(qa));
        } else {
            return notFound("QA text empty");
        }
    }

    @Transactional
    public static Result getAllQAs(){
        Logger.info("getAllCustomers QAs called");
        List<QA> qas = logics.template.QualityAttribute.getAllQAs();
        return ok(Json.toJson(qas));
    }

    @Transactional
    public static Result getQAsByCatalog(long id) {
        Logger.info("getQAsbyCatalogID QAs called");
        List<CatalogQA> qas = logics.template.QualityAttribute.getQAsByCatalog(id);
        return ok(Json.toJson(qas));
    }

    @Transactional
    public static Result createSubCat(Long id, String name) {
        QACategory cat = logics.template.QualityAttribute.createSubCat(id, name);
        return ok(Json.toJson(cat));
    }
    @Transactional
    public static Result getCategoryTree(long id) {
        QACategory cat = logics.template.QualityAttribute.getCategoryTree(id);
        return ok(Json.toJson(cat));
    }

    @Transactional
    public static Result getAllCats() {
        List<QACategory> cats = logics.template.QualityAttribute.getAllCats();
        return ok(Json.toJson(cats));
    }
    @Transactional
    public static Result createCat() {
        Logger.info("creatcat controller called");
        DynamicForm requestData = Form.form().bindFromRequest();
        String name = requestData.get("name");
        String parent = requestData.get("parent");
        if (parent.equals("")) {
            QACategory cat = logics.template.QualityAttribute.createCat(name, null);
            return ok(Json.toJson(cat));
        } else
        {
            Long parentid = Long.parseLong(parent);
            QACategory cat = logics.template.QualityAttribute.createCat(name, parentid);
            return ok(Json.toJson(cat));
        }
    }
}
