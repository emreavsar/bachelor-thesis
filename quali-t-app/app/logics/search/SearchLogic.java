package logics.search;

import com.google.inject.Inject;
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
public class SearchLogic {
    @Inject
    private CatalogDAO catalogDAO;
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private ProjectDAO projectDAO;
    @Inject
    private QualityAttributeDAO qualityAttributeDAO;

    public HashMap<String, ArrayList<? extends AbstractEntity>> search(String searchArgument) {
        HashMap<String, ArrayList<? extends AbstractEntity>> results = new HashMap<>();

        results.put("customer", searchCustomer(searchArgument));
        results.put("projects", searchProjects(searchArgument));
        results.put("catalogs", searchCatalogs(searchArgument));
        results.put("qualityAttributes", searchQAs(searchArgument));

        return results;
    }

    private ArrayList<Catalog> searchCatalogs(String searchArgument) {
        ArrayList<Catalog> catalogs;

        catalogs = (ArrayList<Catalog>) catalogDAO.search(searchArgument);

        return catalogs;
    }

    private ArrayList<models.project.Customer> searchCustomer(String searchArgument) {
        ArrayList<models.project.Customer> customers;

        customers = customerDAO.search(searchArgument);

        return customers;
    }


    private ArrayList<Project> searchProjects(String searchArgument) {
        ArrayList<Project> projects;

        projects = projectDAO.search(searchArgument);

        return projects;
    }


    private ArrayList<QA> searchQAs(String searchArgument) {
        // TODO emre: implement real search
        ArrayList<QA> qas;

        qas = (ArrayList<QA>) qualityAttributeDAO.search(searchArgument);

        return qas;
    }
}
