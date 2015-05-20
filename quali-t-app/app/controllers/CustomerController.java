package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.project.CustomerLogic;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by corina on 01.04.2015.
 */
public class CustomerController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private CustomerLogic customerLogic;
    @Inject
    private JsonConverter jsonConverter;

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public Result createCustomer() {
        return catchAbstractException(request(), json -> ok(Json.toJson(customerLogic.createCustomer(jsonConverter.getCustomerFromJson(json)))));
    }

    @SubjectPresent
    @Transactional
    public Result getAll() {
        Logger.info("getAllCustomers Customres called");
        return ok(Json.toJson(customerLogic.getAllCustomers()));
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public Result updateCustomer() {
        return catchAbstractException(request(), json -> ok(Json.toJson(customerLogic.updateCustomer(jsonConverter.getCustomerFromJson(json)))));
    }

    @Restrict({@Group("synthesizer"), @Group("admin")})
    @Transactional
    public Result deleteCustomer(Long id) {
        return catchAbstractException(id, customerId -> {
            customerLogic.deleteCustomer(customerId);
            return status(202);
        });
    }
}
