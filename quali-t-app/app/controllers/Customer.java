package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by corina on 01.04.2015.
 */
public class Customer extends Controller {
    @Inject
    private logics.project.Customer customerLogic;

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public Result createCustomer() {
        try {
            JsonNode json = request().body().asJson();
            models.project.Customer customer = customerLogic.createCustomer(Converter.getCustomerFromJson(json));
            return ok(Json.toJson(customer));
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        } catch (EntityAlreadyExistsException e) {
            return status(400, e.getMessage());
        }
    }

    @SubjectPresent
    @Transactional
    public Result getAll() {
        Logger.info("getAllCustomers Customres called");
        List<models.project.Customer> customers = customerLogic.getAllCustomers();
        return ok(Json.toJson(customers));
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public Result updateCustomer() {
        try {
            JsonNode json = request().body().asJson();
            models.project.Customer customer = customerLogic.updateCustomer(Converter.getCustomerFromJson(json));
            return ok(Json.toJson(customer));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        } catch (EntityAlreadyExistsException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public Result deleteCustomer(Long id) {
        try {
            customerLogic.deleteCustomer(id);
            return status(202);
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }

}
