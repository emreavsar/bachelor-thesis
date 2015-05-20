package unit.daos.template;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.QualityAttributeDAO;
import exceptions.EntityNotFoundException;
import models.template.QA;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by corina on 03.05.2015.
 */
public class QualityAttributeDAOTest extends AbstractDatabaseTest {
    private QA qa1;
    private QA qa2;
    private List<Long> qaIds;

    private QualityAttributeDAO qualityAttributeDAO;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        qualityAttributeDAO = getInjector().getInstance(QualityAttributeDAO.class);

        qa1 = AbstractTestDataCreator.createQA("Test QA1");
        qa2 = AbstractTestDataCreator.createQA("Test QA2");
        qaIds = new ArrayList<>();
        qaIds.add(qa1.getId());
        qaIds.add(qa2.getId());

    }

    @Test
    public void findAllByIdSuccessful() throws EntityNotFoundException {
        // ARRANGE
        // ACT
        List<QA> qas = qualityAttributeDAO.findAllById(qaIds);
        // ASSERT
        assertThat(qas).containsExactly(qa1, qa2);
    }

    @Test(expected = EntityNotFoundException.class)
    public void findByCatalogTestInvalidCatalog() throws EntityNotFoundException {
        // ARRANGE
        qaIds.add((long) 10000);
        // ACT
        List<QA> qas = qualityAttributeDAO.findAllById(qaIds);
        // ASSERT
    }

}
