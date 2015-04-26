package logics.project;

import dao.models.CustomerDAO;
import dao.models.ProjectDAO;
import dao.models.QualityPropertyDAO;
import models.project.Customer;
import models.project.QualityProperty;

import java.util.List;

/**
 * Created by corina on 08.04.2015.
 */
public class Project {
    /**
     * Creates and persists customer.
     *
     * @param name
     * @param address
     * @return Customer
     */

    public static boolean validate(String name) {
        if (name.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static Customer createCustomer(String name, String address) {
        if (validate(name) == true) {
            CustomerDAO customerDAO = new CustomerDAO();
            Customer c = customerDAO.findByName(name);

            if (c == null) {
                c = new Customer(name, address);
                customerDAO.persist(c);
                return c;
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    public static List<Customer> getAllCustomers() {
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> c = customerDAO.readAll();
        return c;
    }

    public static List<QualityProperty> getAllQualityProperties() {
        QualityPropertyDAO qualityPropertyDAO = new QualityPropertyDAO();
        List<QualityProperty> qp = qualityPropertyDAO.readAll();
        return qp;
    }

    public static models.project.Project createProject(String name, Long customerId, Long catalogId, List<Long> qaIds, List<Long> qpIds) {
        ProjectDAO projectDAO = new ProjectDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        C
        Project p = new models.project.Project(name, customerId, qaIds, qpIds);
        return null;
    }
}
