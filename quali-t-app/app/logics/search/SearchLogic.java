package logics.search;

import com.google.inject.Inject;
import dao.models.CatalogDAO;
import dao.models.ProjectInitiatorDAO;
import dao.models.ProjectDAO;
import dao.models.QualityAttributeDAO;
import exceptions.EntityNotFoundException;
import models.AbstractEntity;
import models.project.Project;
import models.project.ProjectInitiator;
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
    private ProjectInitiatorDAO projectInitiatorDAO;
    @Inject
    private ProjectDAO projectDAO;
    @Inject
    private QualityAttributeDAO qualityAttributeDAO;

    public HashMap<String, ArrayList<? extends AbstractEntity>> search(String searchArgument) throws EntityNotFoundException {
        HashMap<String, ArrayList<? extends AbstractEntity>> results = new HashMap<>();

        results.put("projectInitiator", searchProjectInitiator(searchArgument));
        results.put("projects", searchProjects(searchArgument));
        results.put("catalogs", searchCatalogs(searchArgument));
        results.put("qualityAttributes", searchQAs(searchArgument));

        return results;
    }

    private ArrayList<Catalog> searchCatalogs(String searchArgument) throws EntityNotFoundException {
        ArrayList<Catalog> catalogs;

        catalogs = (ArrayList<Catalog>) catalogDAO.search(searchArgument);

        return catalogs;
    }

    private ArrayList<ProjectInitiator> searchProjectInitiator(String searchArgument) throws EntityNotFoundException {
        ArrayList<ProjectInitiator> projectInitiators;

        projectInitiators = projectInitiatorDAO.search(searchArgument);

        return projectInitiators;
    }


    private ArrayList<Project> searchProjects(String searchArgument) throws EntityNotFoundException {
        ArrayList<Project> projects;

        projects = projectDAO.search(searchArgument);

        return projects;
    }


    private ArrayList<QA> searchQAs(String searchArgument) throws EntityNotFoundException {
        ArrayList<QA> qas;

        qas = (ArrayList<QA>) qualityAttributeDAO.search(searchArgument);

        return qas;
    }
}
