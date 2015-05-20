package logics.project;

import com.google.inject.Inject;
import controllers.Helper;
import dao.models.CustomerDAO;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;

import java.util.List;

/**
 * Created by corina on 06.05.2015.
 */
public class CustomerLogic {
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private ProjectLogic projectLogic;
    @Inject
    private Helper helper;

    public models.project.Customer createCustomer(models.project.Customer customer) throws EntityAlreadyExistsException, MissingParameterException {
        if (customer != null && helper.validate(customer.getName())) {
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

    public models.project.Customer updateCustomer(models.project.Customer updatedCustomer) throws MissingParameterException, EntityNotFoundException, EntityAlreadyExistsException {
        if (updatedCustomer != null && updatedCustomer.getId() != null && helper.validate(updatedCustomer.getName())) {
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

    public List<models.project.Customer> getAllCustomers() {
        return customerDAO.readAll();
    }

    public void deleteCustomer(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            models.project.Customer customer = customerDAO.readById(id);
            for (models.project.Project project : customer.getProjects()) {
                projectLogic.deleteProject(project.getId());
            }
            customerDAO.remove(customer);
        } else {
            throw new MissingParameterException("ID can't be null!");
        }
    }
}
