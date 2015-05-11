package logics.template;

import com.fasterxml.jackson.databind.JsonNode;
import dao.models.CatalogDAO;
import dao.models.CatalogQADAO;
import dao.models.QualityAttributeDAO;
import exceptions.EntityCanNotBeDeleted;
import exceptions.EntityNotFoundException;
import models.template.CatalogQA;
import models.template.QA;

import java.util.Iterator;
import java.util.List;

import static logics.template.Variables.addVarsToQA;

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

    public static models.template.Catalog create(JsonNode json) throws EntityNotFoundException {
        models.template.Catalog catalog = catalogDAO.persist(new models.template.Catalog());
        catalog = update(json, catalog.getId());
        //get node with QAs and create them with variables
        JsonNode qas = json.findValue("selectedQualityAttributes");
        for (Iterator<JsonNode> qaNodes = qas.elements(); qaNodes.hasNext(); ) {
            JsonNode qaNode = qaNodes.next();
            QA qa = qaDAO.readById(qaNode.findValue("id").asLong());
            CatalogQA catalogQA = addQaToCatalog(qa, catalog);
            addVarsToQA(catalogQA, qaNode);
        }
        return catalog;
    }

    public static CatalogQA addQaToCatalog(QA qa, models.template.Catalog catalog) throws EntityNotFoundException {
        models.template.Catalog updatedCatalog = catalogDAO.readById(catalog.getId()).addTemplate(qa);
        catalogDAO.persist(updatedCatalog);
        return catalogQADAO.findByCatalogAndId(updatedCatalog, qa);
    }

    public static void deleteCatalog(long id) throws EntityNotFoundException, EntityCanNotBeDeleted {
        if (id != 6000) {
            models.template.Catalog catalog = catalogDAO.readById(id);
            for (CatalogQA catalogQA : catalog.getTemplates()) {
                catalogQA.setDeleted(true);
                catalogQA.setCatalog(null);
                catalogQADAO.persist(catalogQA);
            }
            catalogDAO.remove(catalogDAO.readById(id));
        } else {
            throw new EntityCanNotBeDeleted("It is not possible to delete the Standard Catalog!");
        }
    }

    public static models.template.CatalogQA createCatalogQA(JsonNode qaNode, JsonNode catalogQANode) throws EntityNotFoundException {
        QA qa = qaDAO.readById(qaNode.findPath("id").asLong());
        models.template.Catalog catalog = catalogDAO.readById(catalogQANode.findPath("catalog").asLong());
        CatalogQA catalogQA = addQaToCatalog(qa, catalog);
        addVarsToQA(catalogQA, catalogQANode);
        return catalogQA;
    }

    public static void removeQaFromCatalog(Long id) throws EntityNotFoundException {
        CatalogQA catalogQA = catalogQADAO.readById(id);
        catalogQA.setDeleted(true);
        catalogQADAO.update(catalogQA);
    }

    public static models.template.Catalog update(JsonNode json, Long catalogId) throws EntityNotFoundException {
        models.template.Catalog updatedCatalog = catalogDAO.readById(catalogId);
        updatedCatalog.setDescription(json.findPath("description").asText());
        updatedCatalog.setName(json.findPath("name").asText());
        updatedCatalog.setPictureURL(json.findPath("image").asText());
        return catalogDAO.update(updatedCatalog);
    }

    public static models.template.CatalogQA updateCatalogQA(JsonNode catalogQANode) throws EntityNotFoundException {
        CatalogQA catalogQA = catalogQADAO.readById(catalogQANode.findPath("id").asLong());
        models.template.Catalog catalog = catalogDAO.readById(catalogQANode.findPath("catalog").asLong());
        removeQaFromCatalog(catalogQA.getId());
        CatalogQA newCatalogQA = addQaToCatalog(catalogQA.getQa(), catalog);
        addVarsToQA(newCatalogQA, catalogQANode);
        return newCatalogQA;
    }
}
