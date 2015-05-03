package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import dao.models.CustomerDAO;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
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
        } catch (MissingParameterException e) {
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

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result updateCustomer() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String name = requestData.get("name");
        String address = requestData.get("address");
        Long id = Long.parseLong(requestData.get("id"));

        try {
            models.project.Customer customer = Project.updateCustomer(id, name, address);
            return ok(Json.toJson(customer));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public static Result deleteCustomer(Long id) {
        try {
            CustomerDAO customerDAO = new CustomerDAO();
            customerDAO.remove(customerDAO.readById(id));
            return status(202);
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }
}
