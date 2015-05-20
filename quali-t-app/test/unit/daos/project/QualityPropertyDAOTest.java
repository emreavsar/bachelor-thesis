package unit.daos.project;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.QualityPropertyDAO;
import exceptions.EntityNotFoundException;
import models.project.QualityProperty;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by corina on 03.05.2015.
 */
public class QualityPropertyDAOTest extends AbstractDatabaseTest {
    private QualityProperty qp1;
    private QualityProperty qp2;
    private List<Long> qpIds;
    private QualityPropertyDAO qualityPropertyDAO;


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        qp1 = AbstractTestDataCreator.createQualityProperty("QP1", "descirption QP1");
        qp2 = AbstractTestDataCreator.createQualityProperty("QP2", "descirption QP2");
        qpIds = new ArrayList<>();
        qpIds.add(qp1.getId());
        qpIds.add(qp2.getId());
        qualityPropertyDAO = getInjector().getInstance(QualityPropertyDAO.class);
    }

    @Test
    public void readAllByIdTestSuccessful() throws EntityNotFoundException {
        // ARRANGE
        // ACT
        List<QualityProperty> qps = qualityPropertyDAO.readAllById(qpIds);
        // ASSERT
        assertThat(qps).containsExactly(qp1, qp2);
    }

    @Test(expected = EntityNotFoundException.class)
    public void readAllByIdTestInvalidId() throws EntityNotFoundException {
        // ARRANGE
        qpIds.add((long) 12345);
        // ACT
        qualityPropertyDAO.readAllById(qpIds);
        // ASSERT
    }

    @Test
    public void testReadByName() {
        // ARRANGE
        // ACT
        QualityProperty persistedQualityProperty = qualityPropertyDAO.findByName("QP1");
        // ASSERT
        assertThat(persistedQualityProperty.getName()).isEqualTo("QP1");
        assertThat(persistedQualityProperty.getId()).isEqualTo(qp1.getId());
    }

    @Test
    public void testReadByInvalidName() {
        // ARRANGE
        // ACT
        QualityProperty persistedQualityProperty = qualityPropertyDAO.findByName("invalid name");
        // ASSERT
        assertThat(persistedQualityProperty).isNull();
    }


}
