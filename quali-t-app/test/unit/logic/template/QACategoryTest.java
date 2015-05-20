package unit.logic.template;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.QACategoryDAO;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.template.QACategoryLogic;
import models.template.QACategory;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


/**
 * Created by corina on 20.05.2015.
 */
public class QACategoryTest extends AbstractDatabaseTest {
    private QACategory qaCategory;
    private QACategory persistedQaCategory;
    private QACategoryLogic qaCategoryLogic;
    private QACategory qaCategorytoUpdate;
    private QACategoryDAO qaCategoryDAO;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        qaCategory = new QACategory(null, "category", "icon");
        persistedQaCategory = AbstractTestDataCreator.createCategory("category", null, "icon");
        qaCategorytoUpdate = new QACategory(null, "new category", "new icon");
        qaCategoryLogic = getInjector().getInstance(QACategoryLogic.class);
        qaCategoryDAO = getInjector().getInstance(QACategoryDAO.class);
    }
    @Test
    public void testCreateValidRootCategory() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        QACategory newQACategory = qaCategoryLogic.createCategory(qaCategory);
        // ASSERT
        assertThat(newQACategory.getId()).isNotNull();
        assertThat(newQACategory.getName()).isEqualTo("category");
        assertThat(newQACategory.getIcon()).isEqualTo("icon");
        assertThat(newQACategory.getParent()).isNull();
    }

    @Test
    public void testCreateValidSubCategory() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        qaCategory.setParent(persistedQaCategory);
        // ACT
        QACategory newQACategory = qaCategoryLogic.createCategory(qaCategory);
        // ASSERT
        assertThat(newQACategory.getId()).isNotNull();
        assertThat(newQACategory.getName()).isEqualTo("category");
        assertThat(newQACategory.getIcon()).isEqualTo("icon");
        assertThat(newQACategory.getParent()).isEqualTo(persistedQaCategory);
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCategoryEmptyName() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        qaCategory.setName("");
        // ACT
        qaCategoryLogic.createCategory(qaCategory);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateNullCategory() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        qaCategoryLogic.createCategory(null);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateSubCategoryInvalidParent() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        QACategory parent = new QACategory();
        qaCategory.setParent(parent);
        // ACT
        qaCategoryLogic.createCategory(qaCategory);
        // ASSERT
    }
    @Test(expected = EntityNotFoundException.class)
    public void testCreateSubCategoryInvalidParentId() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        QACategory parent = new QACategory();
        parent.setId(new Long(99999));
        qaCategory.setParent(parent);
        // ACT
        qaCategoryLogic.createCategory(qaCategory);
        // ASSERT
    }

    ///////////////////
    @Test
    public void testUpdateValidCategory() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        qaCategorytoUpdate.setId(persistedQaCategory.getId());
        // ACT
        QACategory newQACategory = qaCategoryLogic.updateCategory(qaCategorytoUpdate);
        // ASSERT
        assertThat(newQACategory.getId()).isEqualTo(persistedQaCategory.getId());
        assertThat(newQACategory.getName()).isEqualTo("new category");
        assertThat(newQACategory.getIcon()).isEqualTo("new icon");
        assertThat(newQACategory.getParent()).isNull();
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateCategoryEmptyName() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        qaCategorytoUpdate.setId(persistedQaCategory.getId());
        qaCategorytoUpdate.setName("");
        // ACT
        qaCategoryLogic.updateCategory(qaCategorytoUpdate);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateNullCategory() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        qaCategoryLogic.updateCategory(null);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateCategoryNullId() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        qaCategory.setId(null);
        // ACT
        qaCategoryLogic.updateCategory(qaCategory);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateCategoryInvalidId() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        qaCategory.setId(new Long(99999));
        // ACT
        qaCategoryLogic.updateCategory(qaCategory);
        // ASSERT
    }

    @Test
    public void testDeleteValidCategory() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        QACategory subCategory = AbstractTestDataCreator.createCategory("sub category", null, "icon");
        // ACT
        qaCategoryLogic.deleteCategory(subCategory.getId());
        // ASSERT
        assertThat(qaCategoryDAO.readAll().contains(subCategory)).isFalse();
        assertThat(persistedQaCategory.getCategories().size()).isEqualTo(0);
    }

    @Test
    public void testDeleteValidCategoryTree() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        QACategory subCategory = AbstractTestDataCreator.createCategory("name", persistedQaCategory, "icon");
        // ACT
        qaCategoryLogic.deleteCategory(persistedQaCategory.getId());
        // ASSERT
        assertThat(qaCategoryDAO.readAll().contains(persistedQaCategory)).isFalse();
        assertThat(qaCategoryDAO.readAll().contains(subCategory)).isFalse();
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteCategoryInvalidId() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        qaCategoryLogic.deleteCategory(new Long(99999));
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testDeleteCategoryNullId() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        qaCategoryLogic.deleteCategory(null);
        // ASSERT
    }

}
