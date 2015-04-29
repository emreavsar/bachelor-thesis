package logics.project;

import dao.models.*;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameter;
import models.project.Customer;
import models.project.QualityProperty;
import models.project.nfritem.Instance;
import models.template.Catalog;
import models.template.CatalogQA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corina on 08.04.2015.
 */
public class Project {
    /**
     * Creates and persists customer.
     *
     * @param name
     * @return Customer
     */

    public static boolean validate(String name) {
        return !name.equals("");
    }

    public static Customer createCustomer(String name, String address) throws MissingParameter, EntityAlreadyExistsException {
        if (validate(name) == true) {
            CustomerDAO customerDAO = new CustomerDAO();
            Customer c = customerDAO.findByName(name);
            if (c == null) {
                c = new Customer(name, address);
                return customerDAO.persist(c);
            } else {
                throw new EntityAlreadyExistsException("Customer name already exists");
            }

        } else {
            throw new MissingParameter("Missing required paramter");
        }
    }

    public static List<Customer> getAllCustomers() {
        CustomerDAO customerDAO = new CustomerDAO();
        return customerDAO.readAll();
    }

    public static List<QualityProperty> getAllQualityProperties() {
        QualityPropertyDAO qualityPropertyDAO = new QualityPropertyDAO();
        return qualityPropertyDAO.readAll();
    }

    public static models.project.Project createProject(String name, Long customerId, Long catalogId, List<Long> qaIds, List<Long> qpIds) throws EntityNotFoundException {
        ProjectDAO projectDAO = new ProjectDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        CatalogDAO catalogDAO = new CatalogDAO();
        QualityPropertyDAO qualityPropertyDAO = new QualityPropertyDAO();
        CatalogQADAO catalogQADAO = new CatalogQADAO();
        QADAO qaDAO = new QADAO();

        Customer customer = customerDAO.readById(customerId);
        Catalog catalog = catalogDAO.readById(catalogId);
        List<CatalogQA> qas = new ArrayList<>();
        for (Long qa : qaIds){
            qas.add(catalogQADAO.findByCatalogAndId(catalog, qaDAO.readById(qa)));
        }

        List<QualityProperty> qps = qualityPropertyDAO.readAllById(qpIds);
        List<Instance> qaInstances = new ArrayList<>();
        for (CatalogQA qa : qas) {
            qaInstances.add(new Instance(qa.getQa().getDescription(), qa, qps));
        }
        models.project.Project p = new models.project.Project(name, customer,  catalog, qaInstances, qps);
        return projectDAO.persist(p);
    }

    public static List<models.project.Project> getAllProjects() {
        ProjectDAO projectDAO = new ProjectDAO();
        return projectDAO.readAll();
    }

    public static models.project.Project getProject(Long id) throws EntityNotFoundException {
        ProjectDAO projectDAO = new ProjectDAO();
        return projectDAO.readById(id);
    }

    public static Customer updateCustomer(Long id, String name, String address) throws EntityNotFoundException, MissingParameter {
        if (validate(name) == true) {
            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = customerDAO.readById(id);
            customer.setAddress(address);
            customer.setName(name);
            return customerDAO.persist(customer);
        } else {
            throw new MissingParameter("Name can not be empty");
        }
    }
}
