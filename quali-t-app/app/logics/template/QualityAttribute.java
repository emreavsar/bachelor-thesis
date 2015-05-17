package logics.template;

import dao.models.CatalogDAO;
import dao.models.CatalogQADAO;
import dao.models.QACategoryDAO;
import dao.models.QualityAttributeDAO;
import exceptions.EntityNotCreatedException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.template.CatalogQA;
import models.template.QA;
import models.template.QACategory;
import models.template.QAVar;

import java.util.ArrayList;
import java.util.List;

import static controllers.Helper.validate;
import static logics.template.Catalog.deleteCatalogQA;

/**
 * Created by corina on 09.04.2015.
 */
public class QualityAttribute {
    static CatalogQADAO catalogQADAO = new CatalogQADAO();
    static CatalogDAO catalogDAO = new CatalogDAO();
    static QACategoryDAO qaCategoryDAO = new QACategoryDAO();
    static QualityAttributeDAO qaDAO = new QualityAttributeDAO();

    public static List<QA> getAllQAs() {
        return qaDAO.readAllLatest();
    }

    public static List<CatalogQA> getQAsByCatalog(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            models.template.Catalog cat = catalogDAO.readById(id);
            return catalogQADAO.findByCatalog(cat);
        }
        throw new MissingParameterException("Please provide a valid ID");
    }

    public static QA createQA(QA qa, List<Long> categoryIds, List<QAVar> qaVars) throws MissingParameterException, EntityNotFoundException {
        qa = createQA(qa, categoryIds);
        CatalogQA catalogQA = addQaToCatalog(qa, new Long(-6000));
        logics.template.QualityAttribute.addVarsToQA(catalogQA, qaVars);
        return qa;
    }

    private static QA createQA(QA qa, List<Long> categoryIds) throws MissingParameterException, EntityNotFoundException {
        if (qa != null && categoryIds != null && validate(qa.getDescription())) {
            qa.setId(null);
            qa = qaDAO.persist(qa);
            qa = setCategoriesInQa(qa, categoryIds);
            return qa;
        }
        throw new MissingParameterException("QualityAttribute text can not be emtpy");
    }


    public static QA updateQA(QA qa, List<Long> categoryIds, List<QAVar> qaVars) throws EntityNotFoundException, MissingParameterException {
        if (qa != null && qaVars != null) {
            QA currentQA = qaDAO.readById(qa.getId());
            CatalogQA defaultCatalogQA = catalogQADAO.findByCatalogAndId(catalogDAO.readById(Catalog.defaultCatalog), currentQA);
            //if description is the same and only categories or var values are changed, the standard catalogqa is edited

            if (currentQA.getDescription().equals(qa.getDescription())) {
                if (defaultCatalogQA.getVars().size() == qaVars.size()) {
                    int checkNumber = defaultCatalogQA.getVars().size();
                    for (QAVar var : defaultCatalogQA.getVars()) {
                        for (QAVar varNew : qaVars) {
                            if (var.getVarIndex() == varNew.getVarIndex() && var.getType() == var.getType())
                                checkNumber--;
                        }
                    }
                    if (checkNumber == 0) {
                        setCategoriesInQa(currentQA, categoryIds);
                        defaultCatalogQA.getVars().clear();
                        addVarsToQA(defaultCatalogQA, qaVars);
                        return currentQA;
                    }
                }
                throw new EntityNotFoundException("The new variable values are not compatible with the original QA variables");

            } else {
                //if there is any change in the description, a new version of the QA is created
                qa.setVersionNumber(currentQA.getVersionNumber() + 1);
                QA newQA = qaDAO.persist(createQA(qa, categoryIds));
                for (CatalogQA catalogQA : catalogQADAO.findAllByQA(currentQA)) {
                    models.template.Catalog catalog = catalogQA.getCatalog();
//                    markAsDeleted(catalogQA);
                    deleteCatalogQA(catalogQA.getId());
                    catalogQA.setCatalog(catalog);
                    addVarsToQA(addQaToCatalog(newQA, catalogQA.getCatalog().getId()), qaVars);
                }
                currentQA.setDeleted(true);
                qaDAO.update(currentQA);
                return newQA;
            }
        }
        throw new MissingParameterException("Please provide all Parameters");
    }

    public static QA cloneQA(Long id) throws EntityNotFoundException, EntityNotCreatedException, MissingParameterException {
        if (id != null) {
            CatalogQA originalCatalogQA = catalogQADAO.readById(id);
            if (originalCatalogQA.getCatalog().getId() == Catalog.defaultCatalog) {
                QA originalQA = originalCatalogQA.getQa();
                //create new qa with same description
                QA newQA = originalQA.copyQA();
                //create CatalogQA
                newQA.addCatalogQA(originalCatalogQA.copyCatalogQA());
                qaDAO.persist(newQA);
                //add categories to new QA
                List<Long> categoryIds = new ArrayList<>();
                for (QACategory qaCategory : originalQA.getCategories()) {
                    categoryIds.add(qaCategory.getId());
                }
                setCategoriesInQa(newQA, categoryIds);
                return newQA;
            } else {
                throw new EntityNotCreatedException("It is only allowed to clone QAs from Standard Catalog!");
            }
        } else {
            throw new MissingParameterException("Please provide a valid ID!");
        }
    }

    public static void deleteQA(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            QA qa = qaDAO.readById(id);
            List<CatalogQA> catalogQAList = catalogQADAO.findAllByQA(qa);
            for (CatalogQA catalogQA : catalogQAList) {
//                markAsDeleted(catalogQA);
                deleteCatalogQA(catalogQA.getId());
            }
            qa.setDeleted(true);
            qaDAO.update(qa);
        } else {
            throw new MissingParameterException("Please provide a valid ID!");
        }
    }

    private static QA setCategoriesInQa(QA qa, List<Long> categoryIds) throws EntityNotFoundException, MissingParameterException {
        if (categoryIds != null && qa != null) {
            List<QACategory> qaCategories = qaCategoryDAO.readAllById(categoryIds);
            qa.getCategories().clear();
            qa.addCategories(qaCategories);
            return qaDAO.update(qa);
        }
        throw new MissingParameterException("Please provide valid Parameters");
    }

    private static CatalogQA addVarsToQA(CatalogQA catalogQA, List<QAVar> qaVars) throws MissingParameterException {
        if (catalogQA != null && qaVars != null) {
            catalogQA.addVars(qaVars);
            return catalogQADAO.update(catalogQA);
        }
        throw new MissingParameterException("Please provide valid Parameters");
    }
//    private static CatalogQA addQaToCatalog(QA qa) throws EntityNotFoundException {
//        // TODO refactor default catalog id (-6000) into static ConfigClass.VARIABLE constant
//        return addQaToCatalog(qa, new Long(-6000));
//    }

    private static CatalogQA addQaToCatalog(QA qa, Long catalogId) throws EntityNotFoundException {
        return catalogQADAO.findByCatalogAndId(catalogDAO.persist(catalogDAO.readById(catalogId).addTemplate(qa)), qa);
    }
}