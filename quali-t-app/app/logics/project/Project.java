package logics.project;

import dao.models.*;
import exceptions.EntityNotFoundException;
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
        projectDAO.persist(p);
        return p;
    }
}
