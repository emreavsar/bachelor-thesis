package controllers;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;

/**
 * Created by corina on 01.04.2015.
 */
public class Customer extends Controller {
    public static Result createCustomer() {
        Logger.info("createcustomer called");
        DynamicForm requestData = Form.form().bindFromRequest();
        String name = requestData.get("name");
        String address = requestData.get("address");

        if(name.equals("Quality")) {
            return ok(Json.toJson("ok"));
        } else {
            return notFound();
        }
    }
}
