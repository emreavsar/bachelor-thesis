package dao.models;

import dao.AbstractDAO;
import models.project.Project;

import java.util.ArrayList;

/**
 * Created by corina on 30.03.2015.
 */
public class ProjectDAO extends AbstractDAO<Project> {
    public ArrayList<Project> search(String searchArgument) {
//        searchArgument = "%" + searchArgument + "%";
        ArrayList<Project> searchResults = new ArrayList<>();
        searchResults = (ArrayList<Project>) findAll("select p from Project p where p.name like ?", searchArgument);
        return searchResults;
    }
}
