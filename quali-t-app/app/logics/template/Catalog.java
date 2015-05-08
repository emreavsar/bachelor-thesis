package logics.template;

import dao.models.CatalogDAO;
import dao.models.CatalogQADAO;
import dao.models.QualityAttributeDAO;
import exceptions.EntityNotFoundException;
import models.template.QA;

import java.util.List;

/**
 * Created by corina on 10.04.2015.
 */
public class Catalog {
    static CatalogDAO catalogDAO = new CatalogDAO();
    static QualityAttributeDAO qaDAO = new QualityAttributeDAO();
    static CatalogQADAO catalogQADAO = new CatalogQADAO();

    public static List<models.template.Catalog> getAllCatalogs(){
        return catalogDAO.readAll();
    }

    public static models.template.Catalog create(String name, String image, List<Long> qas_id) throws EntityNotFoundException {
        List<QA> qas = qaDAO.findAllById(qas_id);
        models.template.Catalog catalog = new models.template.Catalog(name, "description", image, qas);
        return catalogDAO.persist(catalog);
    }

    public static models.template.CatalogQA addQaToCatalog(QA qa, models.template.Catalog catalog) throws EntityNotFoundException {
        models.template.Catalog updatedCatalog = catalogDAO.readById(catalog.getId()).addTemplate(qa);
        catalogDAO.persist(updatedCatalog);
        return catalogQADAO.findByCatalogAndId(updatedCatalog, qa);
    }
}
