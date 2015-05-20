package unit.logic.project;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.QualityPropertyDAO;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.project.QualityPropertyLogic;
import models.project.QualityProperty;
import org.junit.Before;
import org.junit.Test;
import play.Logger;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by corina on 20.05.2015.
 */
public class QualityPropertyLogicTest extends AbstractDatabaseTest {
    private QualityProperty qualityProperty;
    private QualityProperty persistedQualityProperty;
    private QualityPropertyLogic qualityPropertyLogic;
    private QualityProperty qualityPropertyToUpdate;
    private QualityPropertyDAO qualityPropertyDAO;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        qualityProperty = new QualityProperty("name", "description");
        persistedQualityProperty = AbstractTestDataCreator.createQualityProperty("persisted name", "persisted description");
        qualityPropertyToUpdate = new QualityProperty("new name", "new description");
        qualityPropertyLogic = getInjector().getInstance(QualityPropertyLogic.class);
        qualityPropertyDAO = getInjector().getInstance(QualityPropertyDAO.class);
    }

    @Test
    public void testCreateValidQualityProperty() throws MissingParameterException, EntityAlreadyExistsException {
        // ARRANGE
        // ACT
        QualityProperty newQualityProperty = qualityPropertyLogic.createQualityProperty(qualityProperty);
        // ASSERT
        assertThat(newQualityProperty.getId()).isNotNull();
        assertThat(newQualityProperty.getName()).isEqualTo("name");
        assertThat(newQualityProperty.getDescription()).isEqualTo("description");
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateNullQualityProperty() throws MissingParameterException, EntityAlreadyExistsException {
        // ARRANGE
        // ACT
        qualityPropertyLogic.createQualityProperty(null);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateQualityPropertyEmptyName() throws MissingParameterException, EntityAlreadyExistsException {
        // ARRANGE
        qualityProperty.setName("");
        // ACT
        qualityPropertyLogic.createQualityProperty(qualityProperty);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateQualityPropertyEmptyDescription() throws MissingParameterException, EntityAlreadyExistsException {
        // ARRANGE
        qualityProperty.setDescription("");
        // ACT
        qualityPropertyLogic.createQualityProperty(qualityProperty);
        // ASSERT

    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testCreateQualityPropertyAlreadyExists() throws MissingParameterException, EntityAlreadyExistsException {
        // ARRANGE
        qualityProperty.setName("persisted name");
        // ACT
        qualityPropertyLogic.createQualityProperty(qualityProperty);
        // ASSERT
    }

    @Test
    public void testUpdateValidQualityProperty() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        qualityPropertyToUpdate.setId(persistedQualityProperty.getId());
        Logger.info("id of toupdate" + persistedQualityProperty.getId());
        // ACT
        QualityProperty updatedQualityProperty = qualityPropertyLogic.updateQualityProperty(qualityPropertyToUpdate);
        // ASSERT
        assertThat(updatedQualityProperty.getId()).isEqualTo(persistedQualityProperty.getId());
        assertThat(updatedQualityProperty.getName()).isEqualTo("new name");
        assertThat(updatedQualityProperty.getDescription()).isEqualTo("new description");
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateNullQualityProperty() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        // ACT
        qualityPropertyLogic.updateQualityProperty(null);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateQualityPropertyEmptyName() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        qualityPropertyToUpdate.setId(persistedQualityProperty.getId());
        qualityPropertyToUpdate.setName("");
        // ACT
        qualityPropertyLogic.updateQualityProperty(qualityPropertyToUpdate);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateQualityPropertyEmptyDescription() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        qualityPropertyToUpdate.setId(persistedQualityProperty.getId());
        qualityPropertyToUpdate.setDescription("");
        // ACT
        qualityPropertyLogic.updateQualityProperty(qualityPropertyToUpdate);
        // ASSERT

    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testUpdateQualityPropertyAlreadyExists() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        qualityProperty = AbstractTestDataCreator.createQualityProperty("existing name", "existing description");
        qualityPropertyToUpdate.setId(persistedQualityProperty.getId());
        qualityPropertyToUpdate.setName("existing name");
        // ACT
        qualityPropertyLogic.updateQualityProperty(qualityPropertyToUpdate);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateQualityPropertyNullId() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        qualityPropertyToUpdate.setId(null);
        // ACT
        qualityPropertyLogic.updateQualityProperty(qualityPropertyToUpdate);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateQualityPropertyInvalidId() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        qualityPropertyToUpdate.setId(new Long(99999));
        // ACT
        qualityPropertyLogic.updateQualityProperty(qualityPropertyToUpdate);
        // ASSERT
    }

    @Test
    public void testDeleteValidQualityProperty() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        qualityPropertyLogic.deleteQualityProperty(persistedQualityProperty.getId());
        // ASSERT
        assertThat(qualityPropertyDAO.readAll().contains(persistedQualityProperty)).isFalse();
    }

    @Test(expected = MissingParameterException.class)
    public void testDeleteNullQualityProperty() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        qualityPropertyLogic.deleteQualityProperty(null);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteQualityPropertyInvalidId() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        qualityPropertyLogic.deleteQualityProperty(new Long(99999));
        // ASSERT
    }
}
