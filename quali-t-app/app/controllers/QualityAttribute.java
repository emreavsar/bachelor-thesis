package controllers;

import models.template.QA;
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
        List<QA> qas = logics.template.QualityAttribute.getQAsByCatalog(id);
        return ok(Json.toJson(qas));
    }
}
