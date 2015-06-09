package unit.daos.template;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.CatalogQADAO;
import exceptions.EntityNotFoundException;
import models.template.Catalog;
import models.template.CatalogQA;
import models.template.QA;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by corina on 03.05.2015.
 */
public class CatalogQADAOTest extends AbstractDatabaseTest {

    static Catalog catalog;
    static QA qa1;
    static QA qa2;
    static QA qa3;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        List<QA> qas = new ArrayList<>();

        qa1 = AbstractTestDataCreator.createQA("Test QA1");
        qa2 = AbstractTestDataCreator.createQA("Test QA2");
        qa3 = AbstractTestDataCreator.createQA("Test QA3");
        qas.add(qa1);
        qas.add(qa2);
        catalog = AbstractTestDataCreator.createCatalog("Catalog", "icon", "description", qas);
        AbstractTestDataCreator.createCatalog("Catalog2", "icon", "descirption", qas);
    }

    @Test
    public void findByCatalogTest() throws EntityNotFoundException {
        List<CatalogQA> catalogQAs = new CatalogQADAO().findByCatalog(catalog);
        assertThat(catalogQAs.contains(qa1));
        assertThat(catalogQAs.contains(qa2));
    }

    @Test
    public void findByCatalgTestEmptyCatalog() throws EntityNotFoundException {
        Catalog emptyCatalog = AbstractTestDataCreator.createCatalog("Catalog2", "icon", "description", new ArrayList<>());
        List<CatalogQA> catalogQAs = new CatalogQADAO().findByCatalog(emptyCatalog);
        assertThat(catalogQAs.isEmpty());
    }

    @Test
    public void findByCatalgTestUnsavedCatalog() {
        boolean thrown = false;
        try {
            List<CatalogQA> catalogQAs = new CatalogQADAO().findByCatalog(new Catalog());
        } catch (Exception e) {
            thrown = true;
        }
        assertThat(thrown);
    }

    @Test
    public void findByCatalogAndIdTestSuccessful() throws EntityNotFoundException {
        CatalogQA catalogQA = new CatalogQADAO().findByCatalogAndId(catalog, qa1);
        assertThat(catalogQA.getCatalog().equals(catalog));
        assertThat(catalogQA.getQa().equals(qa1));
    }

    @Test
    public void findByCatalogAndIdTestFailed() throws EntityNotFoundException {
        boolean thrown = false;
        try {
            CatalogQA catalogQA = new CatalogQADAO().findByCatalogAndId(catalog, qa3);
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertThat(thrown);
    }
}
