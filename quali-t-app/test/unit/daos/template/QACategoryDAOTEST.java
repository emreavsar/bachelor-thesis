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
    static QACategory superCategory1;
    static QACategory superCategory2;
    static QACategory subCategory1;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        superCategory1 = AbstractTestDataCreator.createCategory("Super1", null, "icon");
        superCategory2 = AbstractTestDataCreator.createCategory("Super2", null, "icon");
        subCategory1 = AbstractTestDataCreator.createCategory("Sub1", superCategory1.getId(), "icon");
    }

    @Test
    public void readAllSuperclassesTest() {
        List<QACategory> superCategories = new QACategoryDAO().readAllSuperclasses();
        assertThat(superCategories).contains(superCategory1, superCategory2)
                .excludes(subCategory1);
    }

    @Test
    public void readAllByIdTest() throws EntityNotFoundException {
        List<Long> qaIds = new ArrayList<>();
        qaIds.add(subCategory1.getId());
        qaIds.add(superCategory2.getId());
        List<QACategory> categories = new QACategoryDAO().readAllById(qaIds);
        assertThat(categories).containsExactly(subCategory1, superCategory2);
    }

}
