package dao.models;

import dao.AbstractDAO;
import models.template.Catalog;

import java.util.ArrayList;

/**
 * Created by corina on 10.04.2015.
 */
public class CatalogDAO extends AbstractDAO<Catalog> {

    public ArrayList<Catalog> search(String searchArgument) {
        ArrayList<Catalog> searchResults = new ArrayList<>();
        searchArgument = "%" + searchArgument + "%";
        searchResults = (ArrayList<Catalog>) findAll("select c from Catalog c where lower(c.name) like ? or  lower(c.description) like ?", searchArgument, searchArgument);
        return searchResults;
    }
}