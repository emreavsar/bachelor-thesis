package dao.models;

import dao.AbstractDAO;
import models.project.Customer;

import java.util.ArrayList;

/**
 * Created by corina on 30.03.2015.
 */
public class CustomerDAO extends AbstractDAO<Customer> {

    public Customer findByName(String name) {
        return find("select c from Customer c where c.name=?", name);
    }

    public ArrayList<Customer> search(String searchArgument) {
        ArrayList<Customer> searchResults = new ArrayList<>();
        searchArgument = "%" + searchArgument + "%";
        searchResults = (ArrayList<Customer>) findAll("select c from Customer c where lower(c.name) like ? or  lower(c.address) like ?", searchArgument, searchArgument);
        return searchResults;
    }
}
