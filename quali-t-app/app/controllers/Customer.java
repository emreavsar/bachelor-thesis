package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import exceptions.EntityAlreadyExistsException;
import exceptions.MissingParameter;
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

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result createCustomer() {
        Logger.info("createcustomer called");
        DynamicForm requestData = Form.form().bindFromRequest();
        String name = requestData.get("name");
        String address = requestData.get("address");

        try {
            models.project.Customer customer = Project.createCustomer(name, address);
            return ok(Json.toJson(customer));
        } catch (EntityAlreadyExistsException e) {
            return status(400, e.getMessage());
        } catch (MissingParameter e) {
            return status(400, e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public static Result getAll(){
        Logger.info("getAllCustomers Customres called");
        List<models.project.Customer> customers = Project.getAllCustomers();
        return ok(Json.toJson(customers));
    }
}
