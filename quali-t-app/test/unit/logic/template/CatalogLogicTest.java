package unit.logic.template;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.CatalogDAO;
import dao.models.CatalogQADAO;
import exceptions.EntityCanNotBeDeleted;
import exceptions.EntityCanNotBeUpdated;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.template.CatalogLogic;
import models.template.Catalog;
import models.template.CatalogQA;
import models.template.QA;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by corina on 16.05.2015.
 */
public class CatalogLogicTest extends AbstractDatabaseTest {

    private Catalog catalog;
    private List<CatalogQA> catalogQAList;
    private QA persistedQa;
    private CatalogQA catalogQA;
    private Catalog updatedCatalog;
    private Catalog persistedCatalog;
    private CatalogQADAO catalogQADAO;
    private CatalogDAO catalogDAO;
    private CatalogLogic catalogLogic;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        persistedQa = AbstractTestDataCreator.createQA("Test QA");
        persistedCatalog = AbstractTestDataCreator.createCatalog("name", "image", "description", new ArrayList<>());
        catalogQA = new CatalogQA();
        catalogQA.setQa(persistedQa);
        catalogQAList = new ArrayList<>();
        catalogQAList.add(catalogQA);
        catalog = new Catalog("name", "description", "image");
        updatedCatalog = new Catalog("new name", "new description", "new image");
        catalogQADAO = getInjector().getInstance(CatalogQADAO.class);
        catalogLogic = getInjector().getInstance(CatalogLogic.class);
        catalogDAO = getInjector().getInstance(CatalogDAO.class);
    }

    @Test
    public void testCreateValidCatalog() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        catalogQAList.clear();
        catalogQAList.add(AbstractTestDataCreator.createCatalogQA(persistedQa, catalogDAO.readById(new Long(-6000))));
        // ACT
        Catalog newCatalog = catalogLogic.createCatalog(catalog, catalogQAList);
        // ASSERT
        assertThat(newCatalog.getId()).isNotNull();
        assertThat(newCatalog.getName()).isEqualTo("name");
        assertThat(newCatalog.getDescription()).isEqualTo("description");
        assertThat(newCatalog.getImage()).isEqualTo("image");
        assertThat(newCatalog.getTemplates().size()).isEqualTo(1);
        for (CatalogQA catalogQA : newCatalog.getTemplates()) {
            assertThat(catalogQA.getQa()).isEqualTo(persistedQa);
        }
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCatalogWihoutName() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        catalog = new Catalog("", "description", "image");
        // ACT
        catalogLogic.createCatalog(catalog, catalogQAList);
        // ASSERT

    }

    @Test(expected = MissingParameterException.class)
    public void testCreateNullCatalog() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        catalogLogic.createCatalog(null, catalogQAList);
        // ASSERT
    }

    @Test
    public void testCreateCatalogWithEmptyCatalogQAList() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        Catalog newCatalog = catalogLogic.createCatalog(catalog, new ArrayList<>());
        // ASSERT
        assertThat(newCatalog.getId()).isNotNull();
        assertThat(newCatalog.getName()).isEqualTo("name");
        assertThat(newCatalog.getDescription()).isEqualTo("description");
        assertThat(newCatalog.getImage()).isEqualTo("image");
        assertThat(newCatalog.getTemplates().size()).isEqualTo(0);
    }

    @Test
    public void testCreateCatalogWithNullCatalogQAList() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        Catalog newCatalog = catalogLogic.createCatalog(catalog, null);
        // ASSERT
        assertThat(newCatalog.getId()).isNotNull();
        assertThat(newCatalog.getName()).isEqualTo("name");
        assertThat(newCatalog.getDescription()).isEqualTo("description");
        assertThat(newCatalog.getImage()).isEqualTo("image");
        assertThat(newCatalog.getTemplates().size()).isEqualTo(0);
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCatalogWithInvalidCatalogQA() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        catalogQAList.clear();
        catalogQAList.add(new CatalogQA());
        // ACT
        catalogLogic.createCatalog(catalog, catalogQAList);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCreateCatalogWithInvalidQAId() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        persistedQa.setId(new Long(9999));
        catalogQAList.get(0).setQa(persistedQa);
        // ACT
        catalogLogic.createCatalog(catalog, catalogQAList);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCatalogWithNullQA() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        persistedQa.setId(new Long(9999));
        catalogQAList.get(0).setQa(null);
        // ACT
        catalogLogic.createCatalog(catalog, catalogQAList);
        // ASSERT
    }

    @Test
    public void testCreateValidCatalogQA() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        Catalog catalog = AbstractTestDataCreator.createCatalog("name", "image", "description", new ArrayList<>());
        catalogQA.setCatalog(catalog);
        // ACT
        CatalogQA newCatalogQA = catalogLogic.createCatalogQA(catalogQA);
        // ASSERT
        assertThat(newCatalogQA.getCatalog().getName()).isEqualTo(catalog.getName());
        assertThat(newCatalogQA.getCatalog().getId()).isEqualTo(catalog.getId());
        assertThat(newCatalogQA.getQa().getDescription()).isEqualTo(persistedQa.getDescription());
        assertThat(newCatalogQA.getId()).isNotNull();
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCatalogQAWithNullCatalog() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        CatalogQA newCatalogQA = catalogLogic.createCatalogQA(catalogQA);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCreateCatalogQAWithInvalidCatalogId() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        catalog.setId(new Long(9999));
        catalogQA.setCatalog(catalog);
        // ACT
        CatalogQA newCatalogQA = catalogLogic.createCatalogQA(catalogQA);
        // ASSERT

    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCatalogQAWithoutCatalogId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        catalogQA.setCatalog(catalog);
        // ACT
        CatalogQA newCatalogQA = catalogLogic.createCatalogQA(catalogQA);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCatalogQAWithNullQA() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        catalogQA.setQa(null);
        // ACT
        CatalogQA newCatalogQA = catalogLogic.createCatalogQA(catalogQA);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCatalogQAWithInvalidQAId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        catalogQA.getQa().setId(new Long(9999));
        // ACT
        CatalogQA newCatalogQA = catalogLogic.createCatalogQA(catalogQA);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCatalogQAWithoutQAId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        catalogQA.getQa().setId(null);
        // ACT
        CatalogQA newCatalogQA = catalogLogic.createCatalogQA(catalogQA);
        // ASSERT
    }

    @Test
    public void testUpdateValidCatalog() throws EntityNotFoundException, MissingParameterException, EntityCanNotBeUpdated {
        // ARRANGE
        Catalog catalogToUpdate = AbstractTestDataCreator.createCatalog("name", "image", "description", new ArrayList<>());
        updatedCatalog.setId(catalogToUpdate.getId());
        // ACT
        updatedCatalog = catalogLogic.updateCatalog(updatedCatalog);
        // ASSERT
        assertThat(updatedCatalog.getName()).isEqualTo("new name");
        assertThat(updatedCatalog.getDescription()).isEqualTo("new description");
        assertThat(updatedCatalog.getImage()).isEqualTo("new image");
        assertThat(updatedCatalog.getId()).isEqualTo(catalogToUpdate.getId());
    }

    @Test(expected = EntityCanNotBeUpdated.class)
    public void testUpdateStandardCatalog() throws EntityNotFoundException, MissingParameterException, EntityCanNotBeUpdated {
        // ARRANGE
        updatedCatalog.setId(new Long(-6000));
        // ACT
        catalogLogic.updateCatalog(updatedCatalog);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateNullCatalog() throws EntityNotFoundException, EntityCanNotBeUpdated, MissingParameterException {
        // ARRANGE
        // ACT
        catalogLogic.updateCatalog(null);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateEmptyCatalogName() throws EntityNotFoundException, EntityCanNotBeUpdated, MissingParameterException {
        // ARRANGE
        updatedCatalog.setName("");
        // ACT
        catalogLogic.updateCatalog(updatedCatalog);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateNullCatalogId() throws EntityNotFoundException, EntityCanNotBeUpdated, MissingParameterException {
        // ARRANGE
        // ACT
        catalogLogic.updateCatalog(updatedCatalog);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateInvalidCatalogId() throws MissingParameterException, EntityCanNotBeUpdated, EntityNotFoundException {
        // ARRANGE
        updatedCatalog.setId(new Long(9999));
        // ACT
        catalogLogic.updateCatalog(updatedCatalog);
        // ASSERT
    }

    @Test
    public void testUpdateValidCatalogQA() throws MissingParameterException, EntityCanNotBeUpdated, EntityNotFoundException {
        // ARRANGE
        CatalogQA catalogQAToUpdate = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        CatalogQA updatedCatalogQA = new CatalogQA();
        Catalog tempCatalog = new Catalog();
        tempCatalog.setId(catalogQAToUpdate.getCatalog().getId());
        QA tempQa = new QA();
        tempQa.setId(persistedQa.getId());
        updatedCatalogQA.setCatalog(tempCatalog);
        updatedCatalogQA.setQa(tempQa);
        updatedCatalogQA.setId(catalogQAToUpdate.getId());
        // ACT
        updatedCatalogQA = catalogLogic.updateCatalogQA(updatedCatalogQA);
        catalogQAToUpdate = catalogQADAO.readById(catalogQAToUpdate.getId());
        // ASSERT
        assertThat(updatedCatalogQA.getCatalog().getName()).isEqualTo(persistedCatalog.getName());
        assertThat(updatedCatalogQA.getCatalog().getId()).isEqualTo(persistedCatalog.getId());
        assertThat(updatedCatalogQA.getQa().getDescription()).isEqualTo(persistedQa.getDescription());
        assertThat(updatedCatalogQA.getQa().getId()).isEqualTo(persistedQa.getId());
        assertThat(updatedCatalogQA.getId()).isNotNull();
        assertThat(updatedCatalogQA.getId()).isNotEqualTo(catalogQAToUpdate.getId());
        assertThat(catalogQAToUpdate.isDeleted()).isTrue();
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateCatalogQAInvalidCatalogId() throws MissingParameterException, EntityCanNotBeUpdated, EntityNotFoundException {
        // ARRANGE
        CatalogQA catalogQAToUpdate = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        catalogQAToUpdate.getCatalog().setId(new Long(9999));
        // ACT
        catalogLogic.updateCatalogQA(catalogQAToUpdate);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateCatalogQANullCatalogId() throws EntityNotFoundException, EntityCanNotBeUpdated, MissingParameterException {
        // ARRANGE
        CatalogQA catalogQAToUpdate = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        catalogQAToUpdate.getCatalog().setId(null);
        // ACT
        catalogLogic.updateCatalogQA(catalogQAToUpdate);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateCatalogQAInvalidQaId() throws MissingParameterException, EntityCanNotBeUpdated, EntityNotFoundException {
        // ARRANGE
        CatalogQA catalogQAToUpdate = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        catalogQAToUpdate.getQa().setId(new Long(9999));
        // ACT
        catalogLogic.updateCatalogQA(catalogQAToUpdate);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateCatalogQANullQaId() throws EntityNotFoundException, EntityCanNotBeUpdated, MissingParameterException {
        // ARRANGE
        CatalogQA catalogQAToUpdate = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        catalogQAToUpdate.getQa().setId(null);
        // ACT
        catalogLogic.updateCatalogQA(catalogQAToUpdate);
        // ASSERT
    }

    @Test
    public void testDeleteValidCatalog() throws EntityCanNotBeDeleted, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        boolean thrown = false;
        CatalogDAO catalogDAO = new CatalogDAO();
        CatalogQA catalogQA = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        // ACT
        catalogLogic.deleteCatalog(persistedCatalog.getId());
        CatalogQA deletedCatalogQA = catalogQADAO.readById(catalogQA.getId());
        try {
            catalogDAO.readById(persistedCatalog.getId());
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
        assertThat(deletedCatalogQA.isDeleted()).isTrue();
        assertThat(deletedCatalogQA.getCatalog()).isNull();
    }

    @Test(expected = MissingParameterException.class)
    public void testDeleteNullCatalogId() throws EntityCanNotBeDeleted, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        catalogLogic.deleteCatalog(null);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteInvalidCatalogId() throws EntityCanNotBeDeleted, MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        catalogLogic.deleteCatalog(new Long(9999));
        // ASSERT
    }

    @Test
    public void testDeleteValidCatalogQA() throws EntityCanNotBeDeleted, MissingParameterException, EntityNotFoundException {
        // ARRANGE
        CatalogQA catalogQA = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        // ACT
        catalogLogic.deleteCatalogQA(catalogQA.getId());
        CatalogQA deletedCatalogQA = catalogQADAO.readById(catalogQA.getId());
        // ASSERT
        assertThat(deletedCatalogQA.isDeleted()).isTrue();
        assertThat(deletedCatalogQA.getCatalog()).isNull();
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteInvalidCatalogQAId() throws EntityCanNotBeDeleted, MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        catalogLogic.deleteCatalogQA(new Long(9999));
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testDeleteNullCatalogQAId() throws EntityCanNotBeDeleted, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        catalogLogic.deleteCatalogQA(null);
        // ASSERT
    }
}
