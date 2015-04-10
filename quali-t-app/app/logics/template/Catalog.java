package logics.template;

import dao.models.CatalogDAO;

import java.util.List;

/**
 * Created by corina on 10.04.2015.
 */
public class Catalog {

    public static List<models.template.Catalog> getAllCatalogs(){
        CatalogDAO catalogDAO = new CatalogDAO();
        List<models.template.Catalog> catalogs = catalogDAO.readAll();
        return catalogs;
    }
}
