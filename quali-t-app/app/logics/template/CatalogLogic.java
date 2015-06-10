package logics.template;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import dao.models.CatalogDAO;
import dao.models.CatalogQADAO;
import dao.models.QACategoryDAO;
import dao.models.QualityAttributeDAO;
import exceptions.EntityCanNotBeDeleted;
import exceptions.EntityCanNotBeUpdated;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.template.Catalog;
import models.template.CatalogQA;
import models.template.QA;
import models.template.QACategory;
import play.libs.Json;
import util.GlobalVariables;
import util.Helper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by corina on 10.04.2015.
 */
public class CatalogLogic {
    @Inject
    private CatalogDAO catalogDAO;
    @Inject
    private QualityAttributeDAO qualityAttributeDAO;
    @Inject
    private CatalogQADAO catalogQADAO;
    @Inject
    private QACategoryDAO qaCategoryDAO;
    @Inject
    private Helper helper;
    @Inject
    private QualityAttributeLogic qualityAttributeLogic;

    public List<models.template.Catalog> getAllCatalogs() {
        return catalogDAO.readAll();
    }

    public List<Catalog> getAllEditableCatalogs() {
        List<Catalog> catalogList = new ArrayList<>();
        for (Catalog catalog : getAllCatalogs()) {
            if (!catalog.getId().equals(GlobalVariables.standardCatalog)) {
                catalogList.add(catalog);
            }
        }
        return catalogList;
    }

    public Object getCatalogQA(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            return catalogQADAO.readById(id);
        }
        throw new MissingParameterException("Please provide an ID!");
    }

    public models.template.Catalog createCatalog(models.template.Catalog catalog, List<CatalogQA> newCatalogQAs) throws MissingParameterException, EntityNotFoundException {
        if (catalog != null && helper.validate(catalog.getName())) {
            catalog.setId(null);
            models.template.Catalog newCatalog = catalogDAO.persist(catalog);
            if (newCatalogQAs != null) {
                //create CatalogQAs
                CatalogQA standardCatalogQA;
                for (CatalogQA catalogQA : newCatalogQAs) {
                    if (catalogQA.getQa() != null && catalogQA.getQa().getId() != null) {
                        QA qa = qualityAttributeDAO.readById(catalogQA.getQa().getId());
                        standardCatalogQA = catalogQADAO.findByCatalogAndId(catalogDAO.readById(GlobalVariables.standardCatalog), qa);
                        catalogQA = standardCatalogQA.copyCatalogQA();
                        catalogQA.setCatalog(newCatalog);
                        catalogQA.setQa(qa);
                        catalog.addCatalogQA(catalogQA);
                        catalog.addCatalogQA(catalogQADAO.persist(catalogQA));
                    } else {
                        throw new MissingParameterException("Please provide all required Parameters for the CatalogQAs");
                    }
                }
            }
            return catalog;
        }
        throw new MissingParameterException("Please provide all required Catalog Parameters");
    }

    public models.template.CatalogQA createCatalogQA(CatalogQA catalogQA) throws EntityNotFoundException, MissingParameterException {
        if (catalogQA != null) {
            CatalogQA newCatalogQA = addQaToCatalog(catalogQA);
            return catalogQADAO.persist(newCatalogQA);
        }
        throw new MissingParameterException("Please provide a valid CatalogQA");
    }

    public models.template.Catalog updateCatalog(models.template.Catalog catalog) throws EntityNotFoundException, EntityCanNotBeUpdated, MissingParameterException {
        if (catalog != null && helper.validate(catalog.getName()) && catalog.getId() != null) {
            if (!catalog.getId().equals(GlobalVariables.standardCatalog)) {
                models.template.Catalog updatedCatalog = catalogDAO.readById(catalog.getId());
                updatedCatalog.setDescription(catalog.getDescription());
                updatedCatalog.setName(catalog.getName());
                updatedCatalog.setImage(catalog.getImage());
                return catalogDAO.update(updatedCatalog);
            } else {
                throw new EntityCanNotBeUpdated("It is not allowed to edit the Standard Catalog!");
            }
        }
        throw new MissingParameterException("Please provide all required Catalog Parameters");
    }

    public models.template.CatalogQA updateCatalogQA(CatalogQA catalogQA) throws EntityNotFoundException, MissingParameterException {
        models.template.Catalog catalog = catalogQA.getCatalog();
        deleteCatalogQA(catalogQA.getId());

        catalogQA.setCatalog(catalog);
        return catalogQADAO.persist(addQaToCatalog(catalogQA));
    }

    public void deleteCatalog(Long id) throws EntityNotFoundException, EntityCanNotBeDeleted, MissingParameterException {
        if (id != null) {
            if (!id.equals(GlobalVariables.standardCatalog)) {
                models.template.Catalog catalog = catalogDAO.readById(id);
                for (CatalogQA catalogQA : catalog.getTemplates()) {
                    deleteCatalogQA(catalogQA.getId());
                }
                catalogDAO.remove(catalogDAO.readById(id));
            } else {
                throw new EntityCanNotBeDeleted("It is not possible to delete the Standard Catalog!");
            }
        } else {
            throw new MissingParameterException("Please provide an ID!");
        }
    }

    public void deleteCatalogQA(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            CatalogQA catalogQA = catalogQADAO.readById(id);
            deleteCatalogQA(catalogQA);
        } else {
            throw new MissingParameterException("Please provide an ID!");
        }
    }

    private void deleteCatalogQA(CatalogQA catalogQA) throws EntityNotFoundException, MissingParameterException {
        if (catalogQA != null && catalogQA.getId() != null) {
            catalogQA.setDeleted(true);
            catalogQA.setCatalog(null);
            catalogQADAO.update(catalogQA);
        } else {
            throw new MissingParameterException("Please provide a valid CatalogQA!");
        }
    }

    private CatalogQA addQaToCatalog(CatalogQA catalogQA) throws EntityNotFoundException, MissingParameterException {
        if (catalogQA.getQa() != null && catalogQA.getCatalog() != null && catalogQA.getCatalog().getId() != null && catalogQA.getQa().getId() != null) {
            catalogQA.setQa(qualityAttributeDAO.readById(catalogQA.getQa().getId()));
            catalogQA.setCatalog(catalogDAO.readById(catalogQA.getCatalog().getId()));
            catalogQA.setId(null);
//            return catalogQADAO.persist(catalogQA);
            return catalogQA;
        }
        throw new MissingParameterException("Please provide a valid CatalogQA");
    }

    public JsonNode getCatalogToExport(Long id) throws MissingParameterException, EntityNotFoundException {
        JsonNode catalogNode = Json.toJson(getCatalog(id));
        JsonNode qualityAttributeNode = Json.toJson(qualityAttributeLogic.getQAsByCatalog(id));
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("catalog", catalogNode);
        node.put("qualityAttributes", qualityAttributeNode);
        List<String> fieldsToRemove = new ArrayList<>();
        fieldsToRemove.add("id");
        fieldsToRemove.add("qaInstances");
        return node.without(fieldsToRemove);
    }

    public Catalog getCatalog(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            return catalogDAO.readById(id);
        }
        throw new MissingParameterException("Please provide a valid ID!");
    }

    public Catalog importCatalog(Catalog catalog) throws EntityNotFoundException, MissingParameterException {
        setQaCategories(catalog);
        Catalog newCatalog = catalogDAO.persist(catalog);
        Catalog standardCatalog = catalogDAO.readById(GlobalVariables.standardCatalog);
        for (CatalogQA catalogQA : newCatalog.getTemplates()) {
            CatalogQA standardCatalogQA = catalogQA.copyCatalogQA();
            standardCatalogQA.setCatalog(standardCatalog);
            standardCatalogQA.setQa(catalogQA.getQa());
            catalogQADAO.persist(standardCatalogQA);
        }
        return newCatalog;
    }

    private void setQaCategories(Catalog catalog) throws EntityNotFoundException {
        List<QACategory> qaCategoriesList = new ArrayList<>();
        for (CatalogQA catalogQA : catalog.getTemplates()) {
            qaCategoriesList.clear();
            for (QACategory qaCategory : catalogQA.getQa().getCategories()) {
                qaCategoriesList.add(qaCategoryDAO.findByName(qaCategory.getName()));
            }
            catalogQA.getQa().getCategories().clear();
            catalogQA.getQa().addCategories(qaCategoriesList);
        }
    }
}
