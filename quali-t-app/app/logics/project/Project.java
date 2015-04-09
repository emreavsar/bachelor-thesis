package logics.project;

import dao.models.CustomerDAO;
import models.project.Customer;

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
}
