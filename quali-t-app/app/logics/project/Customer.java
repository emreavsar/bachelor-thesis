package logics.project;

import com.fasterxml.jackson.databind.JsonNode;
import dao.models.CustomerDAO;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;

import java.util.List;

import static logics.project.Project.deleteProject;

/**
 * Created by corina on 06.05.2015.
 */
public class Customer {
    static CustomerDAO customerDAO = new CustomerDAO();

    public static boolean validate(String name) {
        return !name.equals("");
    }

    public static models.project.Customer createCustomer(JsonNode json) throws EntityAlreadyExistsException, MissingParameterException {
        models.project.Customer customer = new models.project.Customer(json.findPath("name").asText(), json.findPath("address").asText());
        if (validate(customer.getName())) {
            models.project.Customer c = customerDAO.findByName(customer.getName());
            if (c == null) {
                customer.setProjects(null);
                return customerDAO.persist(customer);
            } else {
                throw new EntityAlreadyExistsException("Customer name already exists");
            }
        } else {
            throw new MissingParameterException("Missing required paramter");
        }
    }

    public static models.project.Customer updateCustomer(JsonNode json) throws MissingParameterException, EntityNotFoundException {
        models.project.Customer customer = customerDAO.readById(json.findPath("id").asLong());
        customer.setAddress(json.findPath("address").asText());
        customer.setName(json.findPath("name").asText());
        if (validate(customer.getName())) {
            return customerDAO.update(customer);
        } else {
            throw new MissingParameterException("Name can not be empty");
        }
    }

    public static List<models.project.Customer> getAllCustomers() {
        return customerDAO.readAll();
    }

    public static void deleteCustomer(Long id) throws EntityNotFoundException {
        models.project.Customer customer = customerDAO.readById(id);
        for (models.project.Project project : customer.getProjects()) {
            deleteProject(project.getId());
        }
        customerDAO.remove(customer);
    }
}
