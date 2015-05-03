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
    static QualityProperty qp1;
    static QualityProperty qp2;
    static List<Long> qpIds;


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        qp1 = AbstractTestDataCreator.createQualityProperty("QP1");
        qp2 = AbstractTestDataCreator.createQualityProperty("QP2");
        qpIds = new ArrayList<>();
        qpIds.add(qp1.getId());
        qpIds.add(qp2.getId());
    }

    @Test
    public void readAllByIdTestSuccessful() throws EntityNotFoundException {
        List<QualityProperty> qps = new QualityPropertyDAO().readAllById(qpIds);
        assertThat(qps).containsExactly(qp1, qp2);
    }

    @Test
    public void readAllByIdTestInvalidId() {
        boolean thrown = false;
        qpIds.add((long) 12345);
        try {
            List<QualityProperty> qps = new QualityPropertyDAO().readAllById(qpIds);
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        assertThat(thrown);
    }
}
