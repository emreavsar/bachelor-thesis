package logics.template;

import com.google.inject.Inject;
import dao.models.*;
import exceptions.EntityNotCreatedException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.template.CatalogQA;
import models.template.QA;
import models.template.QACategory;
import models.template.QAVar;
import play.Logger;
import util.GlobalVariables;
import util.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corina on 09.04.2015.
 */

public class QualityAttributeLogic {
    @Inject
    private CatalogQADAO catalogQADAO;
    @Inject
    private CatalogDAO catalogDAO;
    @Inject
    private QACategoryDAO qaCategoryDAO;
    @Inject
    private QualityAttributeDAO qualityAttributeDAO;
    @Inject
    private QAVarDAO qaVarDAO;
    @Inject
    private CatalogLogic catalogLogic;
    @Inject
    private Helper helper;

    public List<QA> getAllQAs() {
        return qualityAttributeDAO.readAllLatest();
    }

    public List<CatalogQA> getQAsByCatalog(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            models.template.Catalog cat = catalogDAO.readById(id);
            return catalogQADAO.findByCatalog(cat);
        }
        throw new MissingParameterException("Please provide a valid ID");
    }

    public QA createQA(QA qa, List<Long> categoryIds, List<QAVar> qaVars) throws MissingParameterException, EntityNotFoundException {
        qa = createQA(qa, categoryIds);
        CatalogQA catalogQA = addQaToCatalog(qa, GlobalVariables.standardCatalog);
        addVarsToQA(catalogQA, qaVars);
        return qa;
    }

    private QA createQA(QA qa, List<Long> categoryIds) throws MissingParameterException, EntityNotFoundException {
        if (qa != null && categoryIds != null && helper.validate(qa.getDescription())) {
            qa.setId(null);
            qa = qualityAttributeDAO.persist(qa);
            qa = setCategoriesInQa(qa, categoryIds);
            return qa;
        }
        throw new MissingParameterException("QualityAttribute text can not be emtpy");
    }


    public QA updateQA(QA qa, List<Long> categoryIds, List<QAVar> qaVars) throws EntityNotFoundException, MissingParameterException {
        if (qa != null && qaVars != null) {
            QA currentQA = qualityAttributeDAO.readById(qa.getId());
            CatalogQA defaultCatalogQA = catalogQADAO.findByCatalogAndId(catalogDAO.readById(GlobalVariables.standardCatalog), currentQA);
            //if description is the same and only categories or var values are changed, the standard catalogqa is edited

            if (currentQA.getDescription().equals(qa.getDescription())) {
                Logger.info("desc is equal");
                if (defaultCatalogQA.getVariables().size() == qaVars.size()) {
                    int checkNumber = defaultCatalogQA.getVariables().size();
                    List<QAVar> varsToDelete = new ArrayList<>();
                    for (QAVar var : defaultCatalogQA.getVariables()) {
                        varsToDelete.add(var);
                        for (QAVar varNew : qaVars) {
                            if (var.getVarIndex() == varNew.getVarIndex() && var.getType() == var.getType())
                                checkNumber--;
                        }
                    }
                    if (checkNumber == 0) {
                        setCategoriesInQa(currentQA, categoryIds);
                        defaultCatalogQA.getVariables().clear();
                        for (QAVar varToDelete : varsToDelete) {
                            qaVarDAO.remove(varToDelete);
                        }
                        addVarsToQA(defaultCatalogQA, qaVars);
                        return currentQA;
                    }
                }
                throw new EntityNotFoundException("The new variable values are not compatible with the original QA variables");

            } else {
                //if there is any change in the description, a new version of the QA is created
                qa.setVersionNumber(currentQA.getVersionNumber() + 1);
                qa.setPreviousVersion(currentQA);
                QA newQA = qualityAttributeDAO.persist(createQA(qa, categoryIds));
                for (CatalogQA catalogQA : catalogQADAO.findAllByQA(currentQA)) {
                    models.template.Catalog catalog = catalogQA.getCatalog();
//                    markAsDeleted(catalogQA);
                    catalogLogic.deleteCatalogQA(catalogQA.getId());
                    catalogQA.setCatalog(catalog);
                    addVarsToQA(addQaToCatalog(newQA, catalogQA.getCatalog().getId()), qaVars);
                }
                currentQA.setDeleted(true);
                qualityAttributeDAO.update(currentQA);
                return newQA;
            }
        }
        throw new MissingParameterException("Please provide all Parameters");
    }

    public QA cloneQA(Long id) throws EntityNotFoundException, EntityNotCreatedException, MissingParameterException {
        if (id != null) {
            CatalogQA originalCatalogQA = catalogQADAO.readById(id);
            if (originalCatalogQA.getCatalog().getId().equals(GlobalVariables.standardCatalog)) {
                QA originalQA = originalCatalogQA.getQa();
                //create new qa with same description
                QA newQA = originalQA.copyQA();
                //create CatalogQA
                newQA.addCatalogQA(originalCatalogQA.copyCatalogQA());
                qualityAttributeDAO.persist(newQA);
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

    public void deleteQA(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            QA qa = qualityAttributeDAO.readById(id);
            List<CatalogQA> catalogQAList = catalogQADAO.findAllByQA(qa);
            for (CatalogQA catalogQA : catalogQAList) {
//                markAsDeleted(catalogQA);
                catalogLogic.deleteCatalogQA(catalogQA.getId());
            }
            qa.setDeleted(true);
            qualityAttributeDAO.update(qa);
        } else {
            throw new MissingParameterException("Please provide a valid ID!");
        }
    }

    private QA setCategoriesInQa(QA qa, List<Long> categoryIds) throws EntityNotFoundException, MissingParameterException {
        if (categoryIds != null && qa != null) {
            List<QACategory> qaCategories = qaCategoryDAO.readAllById(categoryIds);
            qa.getCategories().clear();
            qa.addCategories(qaCategories);
            return qualityAttributeDAO.update(qa);
        }
        throw new MissingParameterException("Please provide valid Parameters");
    }

    private CatalogQA addVarsToQA(CatalogQA catalogQA, List<QAVar> qaVars) throws MissingParameterException {
        if (catalogQA != null && qaVars != null) {
            catalogQA.addVars(qaVars);
            return catalogQADAO.update(catalogQA);
        }
        throw new MissingParameterException("Please provide valid Parameters");
    }

    private CatalogQA addQaToCatalog(QA qa, Long catalogId) throws EntityNotFoundException {
        return catalogQADAO.findByCatalogAndId(catalogDAO.persist(catalogDAO.readById(catalogId).addTemplate(qa)), qa);
    }
}