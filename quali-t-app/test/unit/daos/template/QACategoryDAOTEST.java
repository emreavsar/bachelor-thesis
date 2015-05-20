package unit.daos.template;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.QACategoryDAO;
import exceptions.EntityNotFoundException;
import models.template.QACategory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;


/**
 * Created by corina on 03.05.2015.
 */


public class QACategoryDAOTEST extends AbstractDatabaseTest {
    private QACategory superCategory1;
    private QACategory superCategory2;
    private QACategory subCategory1;
    private QACategoryDAO qaCategoryDAO;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        superCategory1 = AbstractTestDataCreator.createCategory("Super1", null, "icon");
        superCategory2 = AbstractTestDataCreator.createCategory("Super2", null, "icon");
        subCategory1 = AbstractTestDataCreator.createCategory("Sub1", superCategory1, "icon");
        qaCategoryDAO = getInjector().getInstance(QACategoryDAO.class);
    }

    @Test
    public void readAllSuperclassesTest() {
        // ARRANGE
        // ACT
        List<QACategory> superCategories = qaCategoryDAO.readAllSuperclasses();
        // ASSERT
        assertThat(superCategories).contains(superCategory1, superCategory2)
                .excludes(subCategory1);
    }

    @Test
    public void readAllByIdTest() throws EntityNotFoundException {
        // ARRANGE
        List<Long> qaIds = new ArrayList<>();
        qaIds.add(subCategory1.getId());
        qaIds.add(superCategory2.getId());
        // ACT
        List<QACategory> categories = qaCategoryDAO.readAllById(qaIds);
        // ASSERT
        assertThat(categories).containsExactly(subCategory1, superCategory2);
    }

}
