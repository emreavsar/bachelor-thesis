package unit.logic.template;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
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
    private QA qa;
    private CatalogQA catalogQA;

    public void setUp() throws Exception {
        super.setUp();
        qa = AbstractTestDataCreator.createQA("Test QA");
        catalogQA = new CatalogQA();
        catalogQA.setQa(qa);
        catalogQAList = new ArrayList<>();
        catalogQAList.add(catalogQA);
        catalog = new Catalog("name", "description", "image");
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
            assertThat(catalogQA.getQa()).isEqualTo(qa);
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
        qa.setId(new Long(9999));
        catalogQAList.get(0).setQa(qa);
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
        qa.setId(new Long(9999));
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
        assertThat(newCatalogQA.getQa().getDescription()).isEqualTo(qa.getDescription());
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
}
