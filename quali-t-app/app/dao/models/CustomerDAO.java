package dao.models;

import dao.AbstractDAO;
import models.project.Customer;

/**
 * Created by corina on 30.03.2015.
 */
public class CustomerDAO extends AbstractDAO<Customer> {

    public Customer findByName(String name) {
        return find("select c from Customer c where c.name=?", name);
    }
}
