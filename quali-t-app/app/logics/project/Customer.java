package logics.project;

import dao.models.CustomerDAO;
import exceptions.EntityAlreadyExistsException;
import exceptions.MissingParameterException;

import java.util.List;

/**
 * Created by corina on 06.05.2015.
 */
public class Customer {
    static CustomerDAO customerDAO = new CustomerDAO();

    public static boolean validate(String name) {
        return !name.equals("");
    }

    public static models.project.Customer createCustomer(models.project.Customer customer) throws EntityAlreadyExistsException, MissingParameterException {
        if (validate(customer.getName())) {
            models.project.Customer c = customerDAO.findByName(customer.getName());
            if (c == null) {
                return customerDAO.persist(customer);
            } else {
                throw new EntityAlreadyExistsException("Customer name already exists");
            }
        } else {
            throw new MissingParameterException("Missing required paramter");
        }
    }

    public static models.project.Customer updateCustomer(models.project.Customer customer) throws MissingParameterException {
        if (validate(customer.getName())) {
            return customerDAO.update(customer);
        } else {
            throw new MissingParameterException("Name can not be empty");
        }
    }

    public static List<models.project.Customer> getAllCustomers() {
        return customerDAO.readAll();
    }
}
