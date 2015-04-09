package controllers;

import models.template.QA;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;




/**
 * Created by corina on 09.04.2015.
 */
public class QualityAttribute extends Controller {

    @Transactional
    public static Result createQA() {
        Logger.info("createQA called");
        DynamicForm requestData = Form.form().bindFromRequest();
        String qaText = requestData.get("qaText");

        QA qa = logics.qualityattribute.QualityAttribute.createQA(qaText);
        if (qa != null) {
            return ok(Json.toJson(qa));
        } else {
            return notFound("QA text empty");
        }

    }
}
