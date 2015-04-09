package controllers;

import logics.project.Project;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by corina on 01.04.2015.
 */
public class Customer extends Controller {

    @Transactional
    public static Result createCustomer() {
        Logger.info("createcustomer called");
        DynamicForm requestData = Form.form().bindFromRequest();
        String name = requestData.get("name");
        String address = requestData.get("address");

        models.project.Customer customer = Project.createCustomer(name, address);
        if (customer != null) {
            return ok(Json.toJson(customer));
        } else {
            return notFound("User already exists or name empty");
        }
    }

    @Transactional
    public static Result getAll(){
        Logger.info("getAllCustomers Customres called");
        List<models.project.Customer> customers = Project.getAllCustomers();
        return ok(Json.toJson(customers));
    }
}
