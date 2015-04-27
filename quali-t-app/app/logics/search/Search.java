package logics.search;

import dao.models.CatalogDAO;
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

        results.put("projects", searchProjects(searchArgument));
        results.put("catalogs", searchCatalogs(searchArgument));
        results.put("qualityAttributes", searchQAs(searchArgument));

        return results;
    }

    private static ArrayList<Catalog> searchCatalogs(String searchArgument) {
        // TODO emre: implement real search
        ArrayList<Catalog> catalogs;

        CatalogDAO catalogDAO = new CatalogDAO();
        catalogs = (ArrayList<Catalog>) catalogDAO.readAll();

        return catalogs;
    }

    private static ArrayList<Project> searchProjects(String searchArgument) {
        // TODO emre: implement real search
        ArrayList<Project> projects;

        ProjectDAO projectDAO = new ProjectDAO();
        projects = projectDAO.search(searchArgument);

        return projects;
    }


    private static ArrayList<QA> searchQAs(String searchArgument) {
        // TODO emre: implement real search
        ArrayList<QA> qas;

        QualityAttributeDAO qualityAttributeDAO = new QualityAttributeDAO();
        qas = (ArrayList<QA>) qualityAttributeDAO.readAll();

        return qas;
    }
}
