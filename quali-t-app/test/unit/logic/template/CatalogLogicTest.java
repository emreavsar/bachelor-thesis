package unit.logic.template;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.CatalogDAO;
import dao.models.CatalogQADAO;
import exceptions.EntityCanNotBeDeleted;
import exceptions.EntityCanNotBeUpdated;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.template.Catalog;
import models.template.CatalogQA;
import models.template.QA;
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

    public void setUp() throws Exception {
        super.setUp();
        catalogQADAO = new CatalogQADAO();
        persistedQa = AbstractTestDataCreator.createQA("Test QA");
        persistedCatalog = AbstractTestDataCreator.createCatalog("name", "image", "description", new ArrayList<>());
        catalogQA = new CatalogQA();
        catalogQA.setQa(persistedQa);
        catalogQAList = new ArrayList<>();
        catalogQAList.add(catalogQA);
        catalog = new Catalog("name", "description", "image");
        updatedCatalog = new Catalog("new name", "new description", "new image");
    }

    @Test
    public void testCreateValidCatalog() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        Catalog newCatalog = logics.template.Catalog.createCatalog(catalog, catalogQAList);
        // ASSERT
        assertThat(newCatalog.getId()).isNotNull();
        assertThat(newCatalog.getName()).isEqualTo("name");
        assertThat(newCatalog.getDescription()).isEqualTo("description");
        assertThat(newCatalog.getPictureURL()).isEqualTo("image");
        assertThat(newCatalog.getTemplates().size()).isEqualTo(1);
        for (CatalogQA catalogQA : newCatalog.getTemplates()) {
            assertThat(catalogQA.getQa()).isEqualTo(persistedQa);
        }
    }

    @Test
    public void testCreateCatalogWihoutName() throws EntityNotFoundException {
        // ARRANGE
        catalog = new Catalog("", "description", "image");
        boolean thrown = false;
        // ACT
        try {
            logics.template.Catalog.createCatalog(catalog, catalogQAList);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateNullCatalog() throws EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.Catalog.createCatalog(null, catalogQAList);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateCatalogWithEmptyCatalogQAList() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        Catalog newCatalog = logics.template.Catalog.createCatalog(catalog, new ArrayList<>());
        // ASSERT
        assertThat(newCatalog.getId()).isNotNull();
        assertThat(newCatalog.getName()).isEqualTo("name");
        assertThat(newCatalog.getDescription()).isEqualTo("description");
        assertThat(newCatalog.getPictureURL()).isEqualTo("image");
        assertThat(newCatalog.getTemplates().size()).isEqualTo(0);
    }

    @Test
    public void testCreateCatalogWithNullCatalogQAList() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        Catalog newCatalog = logics.template.Catalog.createCatalog(catalog, null);
        // ASSERT
        assertThat(newCatalog.getId()).isNotNull();
        assertThat(newCatalog.getName()).isEqualTo("name");
        assertThat(newCatalog.getDescription()).isEqualTo("description");
        assertThat(newCatalog.getPictureURL()).isEqualTo("image");
        assertThat(newCatalog.getTemplates().size()).isEqualTo(0);
    }

    @Test
    public void testCreateCatalogWithInvalidCatalogQA() throws EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        catalogQAList.clear();
        catalogQAList.add(new CatalogQA());
        // ACT
        try {
            logics.template.Catalog.createCatalog(catalog, catalogQAList);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateCatalogWithInvalidQAId() throws MissingParameterException {
        // ARRANGE
        boolean thrown = false;
        persistedQa.setId(new Long(9999));
        catalogQAList.get(0).setQa(persistedQa);
        // ACT
        try {
            logics.template.Catalog.createCatalog(catalog, catalogQAList);
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateCatalogWithNullQA() throws EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        persistedQa.setId(new Long(9999));
        catalogQAList.get(0).setQa(null);
        // ACT
        try {
            logics.template.Catalog.createCatalog(catalog, catalogQAList);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateValidCatalogQA() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        Catalog catalog = AbstractTestDataCreator.createCatalog("name", "image", "description", new ArrayList<>());
        catalogQA.setCatalog(catalog);
        // ACT
        CatalogQA newCatalogQA = logics.template.Catalog.createCatalogQA(catalogQA);
        // ASSERT
        assertThat(newCatalogQA.getCatalog().getName()).isEqualTo(catalog.getName());
        assertThat(newCatalogQA.getCatalog().getId()).isEqualTo(catalog.getId());
        assertThat(newCatalogQA.getQa().getDescription()).isEqualTo(persistedQa.getDescription());
        assertThat(newCatalogQA.getId()).isNotNull();
    }

    @Test
    public void testCreateCatalogQAWithNullCatalog() throws EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            CatalogQA newCatalogQA = logics.template.Catalog.createCatalogQA(catalogQA);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateCatalogQAWithInvalidCatalogId() throws MissingParameterException {
        // ARRANGE
        boolean thrown = false;
        catalog.setId(new Long(9999));
        catalogQA.setCatalog(catalog);
        // ACT
        try {
            CatalogQA newCatalogQA = logics.template.Catalog.createCatalogQA(catalogQA);
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateCatalogQAWithoutCatalogId() throws EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        catalogQA.setCatalog(catalog);
        // ACT
        try {
            CatalogQA newCatalogQA = logics.template.Catalog.createCatalogQA(catalogQA);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateCatalogQAWithNullQA() throws EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        catalogQA.setQa(null);
        // ACT
        try {
            CatalogQA newCatalogQA = logics.template.Catalog.createCatalogQA(catalogQA);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateCatalogQAWithInvalidQAId() throws EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        catalogQA.getQa().setId(new Long(9999));
        // ACT
        try {
            CatalogQA newCatalogQA = logics.template.Catalog.createCatalogQA(catalogQA);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testCreateCatalogQAWithoutQAId() throws EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        catalogQA.getQa().setId(null);
        // ACT
        try {
            CatalogQA newCatalogQA = logics.template.Catalog.createCatalogQA(catalogQA);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateValidCatalog() throws EntityNotFoundException, MissingParameterException, EntityCanNotBeUpdated {
        // ARRANGE
        Catalog catalogToUpdate = AbstractTestDataCreator.createCatalog("name", "image", "description", new ArrayList<>());
        updatedCatalog.setId(catalogToUpdate.getId());
        // ACT
        updatedCatalog = logics.template.Catalog.updateCatalog(updatedCatalog);
        // ASSERT
        assertThat(updatedCatalog.getName()).isEqualTo("new name");
        assertThat(updatedCatalog.getDescription()).isEqualTo("new description");
        assertThat(updatedCatalog.getPictureURL()).isEqualTo("new image");
        assertThat(updatedCatalog.getId()).isEqualTo(catalogToUpdate.getId());
    }

    @Test
    public void testUpdateStandardCatalog() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        boolean thrown = false;
        updatedCatalog.setId(new Long(-6000));
        // ACT
        try {
            logics.template.Catalog.updateCatalog(updatedCatalog);
        } catch (EntityCanNotBeUpdated e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateNullCatalog() throws EntityNotFoundException, EntityCanNotBeUpdated {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.Catalog.updateCatalog(null);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateEmptyCatalogName() throws EntityNotFoundException, EntityCanNotBeUpdated {
        // ARRANGE
        updatedCatalog.setName("");
        boolean thrown = false;
        // ACT
        try {
            logics.template.Catalog.updateCatalog(updatedCatalog);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateNullCatalogId() throws EntityNotFoundException, EntityCanNotBeUpdated {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.Catalog.updateCatalog(updatedCatalog);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateInvalidCatalogId() throws MissingParameterException, EntityCanNotBeUpdated {
        // ARRANGE
        boolean thrown = false;
        updatedCatalog.setId(new Long(9999));
        // ACT
        try {
            logics.template.Catalog.updateCatalog(updatedCatalog);
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
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
        updatedCatalogQA = logics.template.Catalog.updateCatalogQA(updatedCatalogQA);
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

    @Test
    public void testUpdateCatalogQAInvalidCatalogId() throws MissingParameterException, EntityCanNotBeUpdated {
        // ARRANGE
        boolean thrown = false;
        CatalogQA catalogQAToUpdate = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        catalogQAToUpdate.getCatalog().setId(new Long(9999));
        // ACT
        try {
            logics.template.Catalog.updateCatalogQA(catalogQAToUpdate);
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateCatalogQANullCatalogId() throws EntityNotFoundException, EntityCanNotBeUpdated {
        // ARRANGE
        boolean thrown = false;
        CatalogQA catalogQAToUpdate = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        catalogQAToUpdate.getCatalog().setId(null);
        // ACT
        try {
            logics.template.Catalog.updateCatalogQA(catalogQAToUpdate);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateCatalogQAInvalidQaId() throws MissingParameterException, EntityCanNotBeUpdated {
        // ARRANGE
        boolean thrown = false;
        CatalogQA catalogQAToUpdate = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        catalogQAToUpdate.getQa().setId(new Long(9999));
        // ACT
        try {
            logics.template.Catalog.updateCatalogQA(catalogQAToUpdate);
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testUpdateCatalogQANullQaId() throws EntityNotFoundException, EntityCanNotBeUpdated {
        // ARRANGE
        boolean thrown = false;
        CatalogQA catalogQAToUpdate = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        catalogQAToUpdate.getQa().setId(null);
        // ACT
        try {
            logics.template.Catalog.updateCatalogQA(catalogQAToUpdate);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testDeleteValidCatalog() throws EntityCanNotBeDeleted, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        boolean thrown = false;
        CatalogDAO catalogDAO = new CatalogDAO();
        CatalogQA catalogQA = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        // ACT
        logics.template.Catalog.deleteCatalog(persistedCatalog.getId());
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
        ;
    }

    @Test
    public void testDeleteNullCatalogId() throws EntityCanNotBeDeleted, EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.Catalog.deleteCatalog(null);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testDeleteInvalidCatalogId() throws EntityCanNotBeDeleted, MissingParameterException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.Catalog.deleteCatalog(new Long(9999));
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testDeleteValidCatalogQA() throws EntityCanNotBeDeleted, MissingParameterException, EntityNotFoundException {
        // ARRANGE
        CatalogQA catalogQA = AbstractTestDataCreator.createCatalogQA(persistedQa, persistedCatalog);
        // ACT
        logics.template.Catalog.deleteCatalogQA(catalogQA.getId());
        CatalogQA deletedCatalogQA = catalogQADAO.readById(catalogQA.getId());
        // ASSERT
        assertThat(deletedCatalogQA.isDeleted()).isTrue();
        assertThat(deletedCatalogQA.getCatalog()).isNull();
    }

    @Test
    public void testDeleteInvalidCatalogQAId() throws EntityCanNotBeDeleted, MissingParameterException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.Catalog.deleteCatalogQA(new Long(9999));
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }

    @Test
    public void testDeleteNullCatalogQAId() throws EntityCanNotBeDeleted, EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            logics.template.Catalog.deleteCatalogQA(null);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown).isTrue();
    }
}
