package logics.template;

import dao.models.CatalogDAO;
import dao.models.QualityAttributeDAO;
import exceptions.EntityNotFoundException;
import models.template.QA;

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

    public static models.template.Catalog create(String name, String image, List<Long> qas_id) throws EntityNotFoundException {
        CatalogDAO catalogDAO = new CatalogDAO();
        QualityAttributeDAO qaDAO = new QualityAttributeDAO();
        List<QA> qas = qaDAO.findAllById(qas_id);
        models.template.Catalog catalog = new models.template.Catalog(name, "description", image, qas);
        catalogDAO.persist(catalog);
        return catalog;
    }
}
