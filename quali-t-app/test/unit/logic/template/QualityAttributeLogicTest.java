package unit.logic.template;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.CatalogDAO;
import dao.models.CatalogQADAO;
import dao.models.QADAO;
import exceptions.EntityNotCreatedException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.template.*;
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
    private CatalogQA standardCatalogQA;
    private QA qa;
    private CatalogDAO catalogDAO;
    private QADAO qaDAO;
    private CatalogQADAO catalogQADAO;
    private List<QACategory> qaCategories;

    public void setUp() throws Exception {
        super.setUp();
        categoryIds = new ArrayList<>();
        qaVarsEmpty = new ArrayList<>();
        catalogDAO = new CatalogDAO();
        qaDAO = new QADAO();
        catalogQADAO = new CatalogQADAO();


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
        QA newQA = logics.template.QualityAttribute.createQA(qa, categoryIds, qaVars);
        // ASSERT
        assertThat(newQA.getDescription()).isEqualTo("Test QA");
        assertThat(newQA.getId()).isNotNull();
    }

    @Test
    public void testCreateEmptyQA() throws EntityNotFoundException {
        // ARRANGE
        models.template.QA qa = new QA("");
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.createQA(qa, categoryIds, qaVars);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateNullQA() throws EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.createQA(null, categoryIds, qaVars);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateQANullCategoryIdsList() throws EntityNotFoundException {
        // ARRANGE
        models.template.QA qa = new QA("Test QA");
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.createQA(qa, null, qaVars);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateQANullqaVarsList() throws EntityNotFoundException {
        // ARRANGE
        models.template.QA qa = new QA("Test QA");
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.createQA(qa, categoryIds, null);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCloneValidCatalogQA() throws EntityNotFoundException, MissingParameterException, EntityNotCreatedException {
        // ACT
        QA clonedQA = logics.template.QualityAttribute.cloneQA(standardCatalogQA.getId());
        // ASSERT
        assertThat(clonedQA.getDescription()).isEqualTo(standardCatalogQA.getQa().getDescription());
        assertThat(clonedQA.getCategories().size()).isEqualTo(standardCatalogQA.getQa().getCategories().size());
        for (models.template.QACategory category : standardCatalogQA.getQa().getCategories()) {
            assertThat(clonedQA.getCategories()).contains(category);
        }
        for (CatalogQA clonedCatalogQA : clonedQA.getUsedInCatalog()) {
            assertThat(clonedCatalogQA.getVars().size()).isEqualTo(standardCatalogQA.getVars().size());
            int clonedCatalogQaVarsSize = clonedCatalogQA.getVars().size();
            for (models.template.QAVar var : standardCatalogQA.getVars()) {
                for (models.template.QAVar clonedVar : clonedCatalogQA.getVars()) {
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

    @Test
    public void testCloneCatalogQANotFromStandardCatalog() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        List<QA> qaList = new ArrayList<>();
        Catalog catalog = AbstractTestDataCreator.createCatalog("catalog", "description", "icon", qaList);
        CatalogQA catalogQA = AbstractTestDataCreator.createCatalogQA(qa, catalog);
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.cloneQA(catalogQA.getId());
        } catch (EntityNotCreatedException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCloneNonExistingCatalogQACatalogQA() throws EntityNotCreatedException, MissingParameterException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.cloneQA(new Long(9999));
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCloneNullCatalogQA() throws EntityNotCreatedException, EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.cloneQA(null);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testDeleteValidQA() throws EntityNotCreatedException, EntityNotFoundException, MissingParameterException {
        // ACT
        logics.template.QualityAttribute.deleteQA(qa.getId());
        QA deletedQA = qaDAO.readById(qa.getId());
        // ASSERT
        assertThat(deletedQA.isDeleted());
        for (CatalogQA catalogQA : deletedQA.getUsedInCatalog()) {
            assertThat(catalogQA.isDeleted());
        }
    }

    @Test
    public void testDeleteNonExistingQA() throws MissingParameterException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.deleteQA(new Long(9999));
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testDeleteNullQA() throws EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.deleteQA(null);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateValidQACategories() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        QA qaToUpdate = qaDAO.readById(qa.getId());
        // ACT
        QA updatedQA = logics.template.QualityAttribute.updateQA(qaToUpdate, categoryIds, qaVars);
        // ASSERT
        assertThat(updatedQA.getCategories().size()).isEqualTo(0);
        assertThat(updatedQA.getDescription()).isEqualTo(qaToUpdate.getDescription());
        assertThat(updatedQA.getVersionNumber()).isEqualTo(qaToUpdate.getVersionNumber());
        assertThat(updatedQA.getUsedInCatalog()).isEqualTo(qaToUpdate.getUsedInCatalog());
        assertThat(updatedQA.isDeleted()).isEqualTo(qaToUpdate.isDeleted());
    }

    @Test
    public void testUpdateQANullCategories() throws EntityNotFoundException {
        // ARRANGE
        QA qaToUpdate = qaDAO.readById(qa.getId());
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.updateQA(qaToUpdate, null, qaVars);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateQANonExistingCategories() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        QA qaToUpdate = qaDAO.readById(qa.getId());
        categoryIds.clear();
        categoryIds.add(new Long(99999));
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.updateQA(qaToUpdate, categoryIds, qaVars);
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
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
        QA updatedQA = logics.template.QualityAttribute.updateQA(qaToUpdate, categoryIds, updatedQaVars);
        CatalogQA updatedCatalogQA = catalogQADAO.findByCatalogAndId(catalogDAO.readById(new Long(-6000)), updatedQA);
        // ASSERT
        assertThat(updatedQA.getDescription()).isEqualTo(qaToUpdate.getDescription());
        assertThat(updatedQA.getVersionNumber()).isEqualTo(qaToUpdate.getVersionNumber());
        assertThat(updatedQA.getUsedInCatalog()).isEqualTo(qaToUpdate.getUsedInCatalog());
        assertThat(updatedQA.isDeleted()).isEqualTo(qaToUpdate.isDeleted());
        assertThat(updatedCatalogQA.getVars().size()).isEqualTo(originalCatalogQA.getVars().size());
        for (QAVar var : updatedCatalogQA.getVars()) {
            assertThat(originalCatalogQA.getVars().contains(var));
            for (QAVarVal value : var.getValues()) {
                assertThat(value.getValue()).isEqualTo("99");

            }
        }
    }

    @Test
    public void testUpdateInvalidQAVars() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        QA qaToUpdate = qaDAO.readById(qa.getId());
        List<QAVar> updatedQaVars = qaVars;
        updatedQaVars.add(new QAVar(99));
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.updateQA(qaToUpdate, categoryIds, updatedQaVars);
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateNullQAVars() throws EntityNotFoundException {
        // ARRANGE
        QA qaToUpdate = qaDAO.readById(qa.getId());
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.updateQA(qaToUpdate, categoryIds, null);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateValidQADescription() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        QA qaToUpdate = new QA("new Description");
        qaToUpdate.setId(qa.getId());
        qaToUpdate.setVersionNumber(qa.getVersionNumber());
        // ACT
        QA newQA = logics.template.QualityAttribute.updateQA(qaToUpdate, categoryIds, qaVars);
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

    @Test
    public void testUpdateEmptyQADescription() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        QA qaToUpdate = new QA("");
        qaToUpdate.setId(qa.getId());
        qaToUpdate.setVersionNumber(qa.getVersionNumber());
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.updateQA(qaToUpdate, categoryIds, qaVars);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateNullQADescription() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        QA qaToUpdate = new QA(null);
        qaToUpdate.setId(qa.getId());
        qaToUpdate.setVersionNumber(qa.getVersionNumber());
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.updateQA(qaToUpdate, categoryIds, qaVars);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateNullQA() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.QualityAttribute.updateQA(null, categoryIds, qaVars);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

}
