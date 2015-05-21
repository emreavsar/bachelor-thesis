package unit.logic.template;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.CatalogDAO;
import dao.models.CatalogQADAO;
import dao.models.QADAO;
import exceptions.EntityNotCreatedException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.template.QualityAttributeLogic;
import models.template.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by corina on 14.05.2015.
 */
public class QualityAttributeLogicTest extends AbstractDatabaseTest {

    private List<Long> categoryIds;
    private List<QAVar> qaVarsEmpty;
    private List<QAVar> qaVars;
    private List<QACategory> qaCategories;
    private CatalogQA standardCatalogQA;
    private QA qa;
    private CatalogDAO catalogDAO;
    private QADAO qaDAO;
    private CatalogQADAO catalogQADAO;
    private QualityAttributeLogic qualityAttributeLogic;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        qualityAttributeLogic = getInjector().getInstance(QualityAttributeLogic.class);
        qaDAO = getInjector().getInstance(QADAO.class);
        catalogDAO = getInjector().getInstance(CatalogDAO.class);
        catalogQADAO = getInjector().getInstance(CatalogQADAO.class);
        
        categoryIds = new ArrayList<>();
        qaVarsEmpty = new ArrayList<>();

        //create QA with categories
        qaCategories = new ArrayList<>();
        qaCategories.add(AbstractTestDataCreator.createCategory("category 1", null, "icon"));
        qa = AbstractTestDataCreator.createQA("Test QA", qaCategories);
        //create var with values for QA
        qaVars = new ArrayList<>();
        List<QAVarVal> qaVarVals = new ArrayList<>();
        qaVarVals.add(new QAVarVal("val1", ValueType.TEXT));
        qaVarVals.add(new QAVarVal("33", ValueType.NUMBER));
        QAVar qaVar = new QAVar(1);
        qaVar.addValues(qaVarVals);
        qaVars.add(qaVar);
        //create Standard CatalogQA
        CatalogDAO catalogDAO = new CatalogDAO();
        standardCatalogQA = AbstractTestDataCreator.createCatalogQA(qa, catalogDAO.readById(new Long(-6000)), qaVars);

    }

    @Test
    public void testCreateValidQA() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        models.template.QA qa = new QA("Test QA");
        // ACT
        QA newQA = qualityAttributeLogic.createQA(qa, categoryIds, qaVars);
        // ASSERT
        assertThat(newQA.getDescription()).isEqualTo("Test QA");
        assertThat(newQA.getId()).isNotNull();
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateEmptyQA() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        models.template.QA qa = new QA("");
        // ACT
        qualityAttributeLogic.createQA(qa, categoryIds, qaVars);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateNullQA() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        qualityAttributeLogic.createQA(null, categoryIds, qaVars);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateQANullCategoryIdsList() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        models.template.QA qa = new QA("Test QA");
        // ACT
        qualityAttributeLogic.createQA(qa, null, qaVars);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateQANullqaVarsList() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        models.template.QA qa = new QA("Test QA");
        // ACT
        qualityAttributeLogic.createQA(qa, categoryIds, null);
        // ASSERT
    }

    @Test
    public void testCloneValidCatalogQA() throws EntityNotFoundException, MissingParameterException, EntityNotCreatedException {
        // ACT
        QA clonedQA = qualityAttributeLogic.cloneQA(standardCatalogQA.getId());
        // ASSERT
        assertThat(clonedQA.getDescription()).isEqualTo(standardCatalogQA.getQa().getDescription());
        assertThat(clonedQA.getCategories().size()).isEqualTo(standardCatalogQA.getQa().getCategories().size());
        for (models.template.QACategory category : standardCatalogQA.getQa().getCategories()) {
            assertThat(clonedQA.getCategories()).contains(category);
        }
        for (CatalogQA clonedCatalogQA : clonedQA.getUsedInCatalog()) {
            assertThat(clonedCatalogQA.getVariables().size()).isEqualTo(standardCatalogQA.getVariables().size());
            int clonedCatalogQaVarsSize = clonedCatalogQA.getVariables().size();
            for (models.template.QAVar var : standardCatalogQA.getVariables()) {
                for (models.template.QAVar clonedVar : clonedCatalogQA.getVariables()) {
                    if (clonedVar.getVarIndex() == var.getVarIndex() && clonedVar.isExtendable() == var.isExtendable() && clonedVar.getType() == var.getType()) {
                        assertThat(clonedVar.getValues().size()).isEqualTo(var.getValues().size());
                        int clonedVarValuesSize = clonedVar.getValues().size();
                        for (QAVarVal val : var.getValues()) {
                            for (QAVarVal clonedVal : clonedVar.getValues()) {
                                if (clonedVal.getValue() == val.getValue() && clonedVal.getType() == val.getType()) {
                                    clonedVarValuesSize--;
                                }
                            }
                        }
                        assertThat(clonedVarValuesSize).isEqualTo(0);
                        clonedCatalogQaVarsSize--;
                    }
                }
            }
            assertThat(clonedCatalogQaVarsSize).isEqualTo(0);
        }
    }

    @Test(expected = EntityNotCreatedException.class)
    public void testCloneCatalogQANotFromStandardCatalog() throws EntityNotFoundException, MissingParameterException, EntityNotCreatedException {
        // ARRANGE
        List<QA> qaList = new ArrayList<>();
        Catalog catalog = AbstractTestDataCreator.createCatalog("catalog", "description", "icon", qaList);
        CatalogQA catalogQA = AbstractTestDataCreator.createCatalogQA(qa, catalog);
        // ACT
        qualityAttributeLogic.cloneQA(catalogQA.getId());
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCloneNonExistingCatalogQACatalogQA() throws EntityNotCreatedException, MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        qualityAttributeLogic.cloneQA(new Long(9999));
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCloneNullCatalogQA() throws EntityNotCreatedException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        qualityAttributeLogic.cloneQA(null);
        // ASSERT
    }

    @Test
    public void testDeleteValidQA() throws EntityNotCreatedException, EntityNotFoundException, MissingParameterException {
        // ACT
        qualityAttributeLogic.deleteQA(qa.getId());
        QA deletedQA = qaDAO.readById(qa.getId());
        // ASSERT
        assertThat(deletedQA.isDeleted());
        for (CatalogQA catalogQA : deletedQA.getUsedInCatalog()) {
            assertThat(catalogQA.isDeleted());
        }
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNonExistingQA() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        qualityAttributeLogic.deleteQA(new Long(9999));
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testDeleteNullQA() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        qualityAttributeLogic.deleteQA(null);
        // ASSERT
    }

    @Test
    public void testUpdateValidQACategories() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        QA qaToUpdate = qaDAO.readById(qa.getId());
        // ACT
        QA updatedQA = qualityAttributeLogic.updateQA(qaToUpdate, categoryIds, qaVars);
        // ASSERT
        assertThat(updatedQA.getCategories().size()).isEqualTo(0);
        assertThat(updatedQA.getDescription()).isEqualTo(qaToUpdate.getDescription());
        assertThat(updatedQA.getVersionNumber()).isEqualTo(qaToUpdate.getVersionNumber());
        assertThat(updatedQA.getUsedInCatalog()).isEqualTo(qaToUpdate.getUsedInCatalog());
        assertThat(updatedQA.isDeleted()).isEqualTo(qaToUpdate.isDeleted());
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateQANullCategories() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        QA qaToUpdate = qaDAO.readById(qa.getId());
        // ACT
        qualityAttributeLogic.updateQA(qaToUpdate, null, qaVars);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateQANonExistingCategories() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        QA qaToUpdate = qaDAO.readById(qa.getId());
        categoryIds.clear();
        categoryIds.add(new Long(99999));
        // ACT
        qualityAttributeLogic.updateQA(qaToUpdate, categoryIds, qaVars);
        // ASSERT
    }

    @Test
    public void testUpdateValidQAVars() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        QA qaToUpdate = qaDAO.readById(qa.getId());
        CatalogQA originalCatalogQA = catalogQADAO.readById(standardCatalogQA.getId());
        List<QAVar> updatedQaVars = qaVars;
        for (QAVar var : updatedQaVars) {
            for (QAVarVal value : var.getValues()) {
                value.setValue("99");
            }
        }
        // ACT
        QA updatedQA = qualityAttributeLogic.updateQA(qaToUpdate, categoryIds, updatedQaVars);
        CatalogQA updatedCatalogQA = catalogQADAO.findByCatalogAndId(catalogDAO.readById(new Long(-6000)), updatedQA);
        // ASSERT
        assertThat(updatedQA.getDescription()).isEqualTo(qaToUpdate.getDescription());
        assertThat(updatedQA.getVersionNumber()).isEqualTo(qaToUpdate.getVersionNumber());
        assertThat(updatedQA.getUsedInCatalog()).isEqualTo(qaToUpdate.getUsedInCatalog());
        assertThat(updatedQA.isDeleted()).isEqualTo(qaToUpdate.isDeleted());
        assertThat(updatedCatalogQA.getVariables().size()).isEqualTo(originalCatalogQA.getVariables().size());
        for (QAVar var : updatedCatalogQA.getVariables()) {
            assertThat(originalCatalogQA.getVariables().contains(var));
            for (QAVarVal value : var.getValues()) {
                assertThat(value.getValue()).isEqualTo("99");

            }
        }
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateInvalidQAVars() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        QA qaToUpdate = qaDAO.readById(qa.getId());
        List<QAVar> updatedQaVars = qaVars;
        updatedQaVars.add(new QAVar(99));
        // ACT
        qualityAttributeLogic.updateQA(qaToUpdate, categoryIds, updatedQaVars);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateNullQAVars() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        QA qaToUpdate = qaDAO.readById(qa.getId());
        // ACT
        qualityAttributeLogic.updateQA(qaToUpdate, categoryIds, null);
        // ASSERT
    }

    @Test
    public void testUpdateValidQADescription() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        QA qaToUpdate = new QA("new Description");
        qaToUpdate.setId(qa.getId());
        qaToUpdate.setVersionNumber(qa.getVersionNumber());
        // ACT
        QA newQA = qualityAttributeLogic.updateQA(qaToUpdate, categoryIds, qaVars);
        // ASSERT
        assertThat(qa.isDeleted()).isTrue();
        assertThat(newQA.getDescription()).isEqualTo(qaToUpdate.getDescription());
        assertThat(newQA.getVersionNumber()).isEqualTo(qa.getVersionNumber() + 1);
        assertThat(qaToUpdate.getUsedInCatalog().size()).isEqualTo(newQA.getUsedInCatalog().size());
        int originalUsedInCatalogSize = qaToUpdate.getUsedInCatalog().size();
        for (CatalogQA originalCatalogQA : qa.getUsedInCatalog()) {
            assertThat(originalCatalogQA.isDeleted()).isTrue();
            for (CatalogQA newCatalogQA : newQA.getUsedInCatalog()) {
                if (originalCatalogQA.getCatalog().getId() == newCatalogQA.getCatalog().getId()) {
                    originalUsedInCatalogSize--;
                }
            }
        }
        assertThat(originalUsedInCatalogSize).isEqualTo(0);
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateEmptyQADescription() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        QA qaToUpdate = new QA("");
        qaToUpdate.setId(qa.getId());
        qaToUpdate.setVersionNumber(qa.getVersionNumber());
        // ACT
        qualityAttributeLogic.updateQA(qaToUpdate, categoryIds, qaVars);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateNullQADescription() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        QA qaToUpdate = new QA();
        qaToUpdate.setId(qa.getId());
        qaToUpdate.setVersionNumber(qa.getVersionNumber());
        // ACT
        qualityAttributeLogic.updateQA(qaToUpdate, categoryIds, qaVars);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateNullQA() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        qualityAttributeLogic.updateQA(null, categoryIds, qaVars);
        // ASSERT
    }

}
