package logics.project;

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
        return name != null && !name.isEmpty();
    }

    public static models.project.Customer createCustomer(models.project.Customer customer) throws EntityAlreadyExistsException, MissingParameterException {
        if (customer != null && validate(customer.getName())) {
            models.project.Customer c = customerDAO.findByName(customer.getName());
            if (c == null) {
                customer.setId(null);
                return customerDAO.persist(customer);
            } else {
                throw new EntityAlreadyExistsException("Customer name already exists");
            }
            } else {
            throw new MissingParameterException("Missing required paramter");
            }
    }

    public static models.project.Customer updateCustomer(models.project.Customer updatedCustomer) throws MissingParameterException, EntityNotFoundException, EntityAlreadyExistsException {
        if (updatedCustomer != null && updatedCustomer.getId() != null && validate(updatedCustomer.getName())) {
            models.project.Customer c = customerDAO.findByName(updatedCustomer.getName());
            if (c == null) {
                models.project.Customer customer = customerDAO.readById(updatedCustomer.getId());
                customer.setAddress(updatedCustomer.getAddress());
                customer.setName(updatedCustomer.getName());
                return customerDAO.update(customer);
            } else {
                throw new EntityAlreadyExistsException("Customer name already exists");
            }
        } else {
            throw new MissingParameterException("Missing required paramter");
        }
    }

    public static List<models.project.Customer> getAllCustomers() {
        return customerDAO.readAll();
    }

    public static void deleteCustomer(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            models.project.Customer customer = customerDAO.readById(id);
            for (models.project.Project project : customer.getProjects()) {
                deleteProject(project.getId());
            }
            customerDAO.remove(customer);
        } else {
            throw new MissingParameterException("ID can't be null!");
        }
    }
}
