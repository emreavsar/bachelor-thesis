package logics.template;

import com.fasterxml.jackson.databind.JsonNode;
import dao.models.CatalogDAO;
import dao.models.CatalogQADAO;
import dao.models.QualityAttributeDAO;
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

    public static models.template.Catalog create(String name, String image, JsonNode qas) throws EntityNotFoundException {
//        List<QA> qas = qaDAO.findAllById(qas_id);
        models.template.Catalog catalog = catalogDAO.persist(new models.template.Catalog(name, "description", image));
        for (Iterator<JsonNode> qaNodes = qas.elements(); qaNodes.hasNext(); ) {
            JsonNode qaNode = qaNodes.next();
            QA qa = qaDAO.readById(qaNode.findValue("id").asLong());
            CatalogQA catalogQA = addQaToCatalog(qa, catalog);
            addVarsToQA(catalogQA, qaNode);
        }

//        qas = JsPath.json.findValuesAsText("id");
//        models.template.Catalog catalog = new models.template.Catalog(name, "description", image, qas);
        return catalog;
    }

    public static CatalogQA addQaToCatalog(QA qa, models.template.Catalog catalog) throws EntityNotFoundException {
        models.template.Catalog updatedCatalog = catalogDAO.readById(catalog.getId()).addTemplate(qa);
        catalogDAO.persist(updatedCatalog);
        return catalogQADAO.findByCatalogAndId(updatedCatalog, qa);
    }
}
