package logics.search;

import controllers.Customer;
import dao.models.CatalogDAO;
import dao.models.CustomerDAO;
import dao.models.ProjectDAO;
import dao.models.QualityAttributeDAO;
import models.AbstractEntity;
import models.project.Project;
import models.template.Catalog;
import models.template.QA;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by emre on 25/04/15.
 */
public class Search {
    public static HashMap<String, ArrayList<? extends AbstractEntity>> search(String searchArgument) {
        HashMap<String, ArrayList<? extends AbstractEntity>> results = new HashMap<>();

        results.put("customer", searchCustomer(searchArgument));
        results.put("projects", searchProjects(searchArgument));
        results.put("catalogs", searchCatalogs(searchArgument));
        results.put("qualityAttributes", searchQAs(searchArgument));

        return results;
    }

    private static ArrayList<Catalog> searchCatalogs(String searchArgument) {
        ArrayList<Catalog> catalogs;

        CatalogDAO catalogDAO = new CatalogDAO();
        catalogs = (ArrayList<Catalog>) catalogDAO.search(searchArgument);

        return catalogs;
    }

    private static ArrayList<models.project.Customer> searchCustomer(String searchArgument) {
        ArrayList<models.project.Customer> customers;

        CustomerDAO customerDAO = new CustomerDAO();
        customers = customerDAO.search(searchArgument);

        return customers;
    }


    private static ArrayList<Project> searchProjects(String searchArgument) {
        ArrayList<Project> projects;

        ProjectDAO projectDAO = new ProjectDAO();
        projects = projectDAO.search(searchArgument);

        return projects;
    }


    private static ArrayList<QA> searchQAs(String searchArgument) {
        // TODO emre: implement real search
        ArrayList<QA> qas;

        QualityAttributeDAO qualityAttributeDAO = new QualityAttributeDAO();
        qas = (ArrayList<QA>) qualityAttributeDAO.search(searchArgument);

        return qas;
    }
}
