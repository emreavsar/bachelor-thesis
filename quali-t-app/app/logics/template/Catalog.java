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

    public static List<models.template.Catalog> getAllCatalogs(){
        CatalogDAO catalogDAO = new CatalogDAO();
        return catalogDAO.readAll();
    }

    public static models.template.Catalog create(String name, String image, List<Long> qas_id) throws EntityNotFoundException {
        CatalogDAO catalogDAO = new CatalogDAO();
        QualityAttributeDAO qaDAO = new QualityAttributeDAO();
        List<QA> qas = qaDAO.findAllById(qas_id);
        models.template.Catalog catalog = new models.template.Catalog(name, "description", image, qas);
        return catalogDAO.persist(catalog);
    }

    public static models.template.CatalogQA addQaToCatalog(Long qaId, Long catalogId) throws EntityNotFoundException {
        CatalogDAO catalogDAO = new CatalogDAO();
        QualityAttributeDAO qaDAO = new QualityAttributeDAO();
        CatalogQADAO catalogQADAO = new CatalogQADAO();
        models.template.Catalog catalog = catalogDAO.readById(catalogId);
        QA qa = qaDAO.readById(qaId);
        catalog.addTemplate(qa);
        catalogDAO.persist(catalog);
        return catalogQADAO.findByCatalogAndId(catalog, qa);
    }
}
