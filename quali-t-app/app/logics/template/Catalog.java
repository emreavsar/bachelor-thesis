package logics.template;

import dao.models.CatalogDAO;
import dao.models.CatalogQADAO;
import dao.models.QualityAttributeDAO;
import exceptions.EntityCanNotBeDeleted;
import exceptions.EntityCanNotBeUpdated;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.template.CatalogQA;
import models.template.QA;
import play.Logger;

import java.util.List;

import static controllers.Helper.validate;

/**
 * Created by corina on 10.04.2015.
 */
public class Catalog {
    // TODO refactor default catalog id (-6000) into static ConfigClass.VARIABLE constant
    public final static long defaultCatalog = new Long(-6000);
    static CatalogDAO catalogDAO = new CatalogDAO();
    static QualityAttributeDAO qaDAO = new QualityAttributeDAO();
    static CatalogQADAO catalogQADAO = new CatalogQADAO();

    public static List<models.template.Catalog> getAllCatalogs() {
        return catalogDAO.readAll();
    }

    public static models.template.Catalog createCatalog(models.template.Catalog catalog, List<CatalogQA> newCatalogQAs) throws MissingParameterException, EntityNotFoundException {
        if (catalog != null && validate(catalog.getName())) {
            catalog.setId(null);
            models.template.Catalog newCatalog = catalogDAO.persist(catalog);
            if (newCatalogQAs != null) {
                //create CatalogQAs
                for (CatalogQA catalogQA : newCatalogQAs) {
                    if (catalogQA.getQa() != null && catalogQA.getQa().getId() != null) {
                        QA qa = qaDAO.readById(catalogQA.getQa().getId());
                        catalogQA.setQa(qa);
                        catalogQA.setCatalog(newCatalog);
                        catalog.addCatalogQA(catalogQA);
                        catalog.addCatalogQA(catalogQADAO.persist(catalogQA));
                    } else {
                        throw new MissingParameterException("Please provide all required Parameters for the CatalogQAs");
                    }
                }
            }
            return catalog;
        } else {
            throw new MissingParameterException("Please provide all required Catalog Parameters");
        }
    }

    public static models.template.CatalogQA createCatalogQA(CatalogQA catalogQA) throws EntityNotFoundException, MissingParameterException {
        if (catalogQA != null) {
            return catalogQADAO.persist(addQaToCatalog(catalogQA));
        }
        throw new MissingParameterException("Please provide a valid CatalogQA");
    }

    public static models.template.Catalog updateCatalog(models.template.Catalog catalog) throws EntityNotFoundException, EntityCanNotBeUpdated, MissingParameterException {
        if (catalog != null && validate(catalog.getName()) && catalog.getId() != null) {
            if (catalog.getId() != -6000) {
                models.template.Catalog updatedCatalog = catalogDAO.readById(catalog.getId());
                updatedCatalog.setDescription(catalog.getDescription());
                updatedCatalog.setName(catalog.getName());
                updatedCatalog.setPictureURL(catalog.getPictureURL());
                return catalogDAO.update(updatedCatalog);
            } else {
                throw new EntityCanNotBeUpdated("It is not allowed to edit the Standard Catalog!");
            }
        }
        throw new MissingParameterException("Please provide all required Catalog Parameters");
    }

    public static models.template.CatalogQA updateCatalogQA(CatalogQA catalogQA) throws EntityNotFoundException, MissingParameterException {
        deleteCatalogQA(catalogQA.getId());
        catalogQA.setId(null);
        CatalogQA newCatalogQA = addQaToCatalog(catalogQA);
        catalogQADAO.persist(newCatalogQA);
        Logger.info("catalogQA persisted " + newCatalogQA.getId());
//        return catalogQADAO.persist(addQaToCatalog(catalogQA));
        return newCatalogQA;
    }

//    private static CatalogQA addQaToCatalog(QA qa, models.template.Catalog catalog) throws EntityNotFoundException {
//        return addQaToCatalog(qa, catalog.getId());
//    }

    public static void deleteCatalog(Long id) throws EntityNotFoundException, EntityCanNotBeDeleted {
        // TODO refactor default catalog id (-6000) into static ConfigClass.VARIABLE constant
        if (id != -6000 && id != null) {
            models.template.Catalog catalog = catalogDAO.readById(id);
            for (CatalogQA catalogQA : catalog.getTemplates()) {
                catalogQA.setDeleted(true);
                catalogQA.setCatalog(null);
                catalogQADAO.persist(catalogQA);
                Logger.info("just deleted catalogqa " + catalogQA.getId());
            }
            catalogDAO.remove(catalogDAO.readById(id));
        } else {
            throw new EntityCanNotBeDeleted("It is not possible to delete the Standard Catalog!");
        }
    }

    public static void deleteCatalogQA(Long id) throws EntityNotFoundException {
        CatalogQA catalogQA = catalogQADAO.readById(id);
        catalogQA.setDeleted(true);
        Logger.info("deleted id " + catalogQA.getId() + catalogQA.isDeleted());
        catalogQADAO.update(catalogQA);
    }

    public static CatalogQA addQaToCatalog(QA qa) throws EntityNotFoundException {
        // TODO refactor default catalog id (-6000) into static ConfigClass.VARIABLE constant
        return addQaToCatalog(qa, new Long(-6000));
    }

    public static CatalogQA addQaToCatalog(QA qa, Long catalogId) throws EntityNotFoundException {
        return catalogQADAO.findByCatalogAndId(catalogDAO.persist(catalogDAO.readById(catalogId).addTemplate(qa)), qa);
    }

    private static CatalogQA addQaToCatalog(CatalogQA catalogQA) throws EntityNotFoundException, MissingParameterException {
        if (catalogQA.getQa() != null && catalogQA.getCatalog() != null && catalogQA.getQa().getId() != null & catalogQA.getCatalog().getId() != null) {
            catalogQA.setQa(qaDAO.readById(catalogQA.getQa().getId()));
            Logger.info("set qa id " + catalogQA.getQa().getId());
            catalogQA.setCatalog(catalogDAO.readById(catalogQA.getCatalog().getId()));
            Logger.info("set catalog id " + catalogQA.getCatalog().getId());
            CatalogQA newCatalogQA = catalogQADAO.persist(catalogQA);
            Logger.info("new catalog qa" + newCatalogQA.getId());
//            return catalogQADAO.persist(catalogQA);
            return newCatalogQA;
        }
        throw new MissingParameterException("Please provide a valid CatalogQA");
    }

    public static Object getCatalogQA(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            return catalogQADAO.readById(id);
        }
        throw new MissingParameterException("Please provide an ID!");
    }
}
