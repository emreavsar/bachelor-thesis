package dao.models;

import dao.AbstractDAO;
import models.project.Project;

import java.util.ArrayList;

/**
 * Created by corina on 30.03.2015.
 */
public class ProjectDAO extends AbstractDAO<Project> {
    public ArrayList<Project> search(String searchArgument) {
        ArrayList<Project> searchResults = new ArrayList<>();
        searchArgument = "%" + searchArgument + "%";
        searchResults = (ArrayList<Project>) findAll("select p from Project p where lower(p.name) like ?", searchArgument);
        return searchResults;
    }
}
