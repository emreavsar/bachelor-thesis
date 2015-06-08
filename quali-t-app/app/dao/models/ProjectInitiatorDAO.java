package dao.models;

import dao.AbstractDAO;
import exceptions.EntityNotFoundException;
import models.project.ProjectInitiator;

import java.util.ArrayList;

/**
 * Created by corina on 30.03.2015.
 */
public class ProjectInitiatorDAO extends AbstractDAO<ProjectInitiator> {

    public ProjectInitiator findByName(String name) throws EntityNotFoundException {
        return find("select c from ProjectInitiator c where c.name=?", name);
    }

    public ArrayList<ProjectInitiator> search(String searchArgument) throws EntityNotFoundException {
        ArrayList<ProjectInitiator> searchResults = new ArrayList<>();
        searchArgument = "%" + searchArgument + "%";
        searchResults = (ArrayList<ProjectInitiator>) findAll("select c from ProjectInitiator c where lower(c.name) like ? or  lower(c.address) like ?", searchArgument, searchArgument);
        return searchResults;
    }
}
