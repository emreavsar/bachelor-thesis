package unit.logic.project;


import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.project.ProjectInitiatorLogic;
import models.project.ProjectInitiator;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ProjectInitiatorLogicTest extends AbstractDatabaseTest {

    private ProjectInitiator projectInitiator;
    private ProjectInitiatorLogic projectInitiatorLogic;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        projectInitiator = new ProjectInitiator("Name", "Address");
        projectInitiatorLogic = getInjector().getInstance(ProjectInitiatorLogic.class);
    }

    //createProjectInitiator Tests
    @Test
    public void testCreateValidCustomer() throws MissingParameterException, EntityAlreadyExistsException, InterruptedException, EntityNotFoundException {
        // ACT
        ProjectInitiator newProjectInitiator = projectInitiatorLogic.createProjectInitiator(projectInitiator);
        // ASSERT
        assertThat(newProjectInitiator.getId()).isNotNull();
        assertThat(newProjectInitiator.getName()).isEqualTo("Name");
        assertThat(newProjectInitiator.getAddress()).isEqualTo("Address");
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCustomerEmptyName() throws EntityAlreadyExistsException, MissingParameterException, EntityNotFoundException {
        // ARRANGE
        ProjectInitiator newProjectInitiator = new ProjectInitiator("", "Address");
        // ACT
        projectInitiatorLogic.createProjectInitiator(newProjectInitiator);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCustomerNullName() throws EntityAlreadyExistsException, MissingParameterException, EntityNotFoundException {
        // ARRANGE
        ProjectInitiator newProjectInitiator = new ProjectInitiator(null, "Address");
        // ACT
        projectInitiatorLogic.createProjectInitiator(newProjectInitiator);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateNullCustomer() throws EntityAlreadyExistsException, MissingParameterException, EntityNotFoundException {
        // ARRANGE
        ProjectInitiator newProjectInitiator = null;
        // ACT
        projectInitiatorLogic.createProjectInitiator(newProjectInitiator);
        // ASSERT
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testCreateCustomerAlreadyExists() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        projectInitiatorLogic.createProjectInitiator(projectInitiator);
        // ACT
        projectInitiatorLogic.createProjectInitiator(projectInitiator);
        // ASSERT
    }

    //updateProjectInitiator Tests

    @Test
    public void testUpdateValidCustomer() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ACT
        ProjectInitiator originalProjectInitiator = AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        projectInitiator.setId(originalProjectInitiator.getId());
        ProjectInitiator updatedProjectInitiator = projectInitiatorLogic.updateProjectInitiator(projectInitiator);
        // ASSERT
        assertThat(updatedProjectInitiator.getId()).isEqualTo(originalProjectInitiator.getId());
        assertThat(updatedProjectInitiator.getName()).isEqualTo("Name");
        assertThat(updatedProjectInitiator.getAddress()).isEqualTo("Address");
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateCustomerEmptyName() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        ProjectInitiator originalProjectInitiator = AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        projectInitiator.setId(originalProjectInitiator.getId());
        projectInitiator.setName("");
        // ACT
        projectInitiatorLogic.updateProjectInitiator(projectInitiator);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateCustomerNullName() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        ProjectInitiator originalProjectInitiator = AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        projectInitiator.setId(originalProjectInitiator.getId());
        projectInitiator.setName(null);
        // ACT
        projectInitiatorLogic.updateProjectInitiator(projectInitiator);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateNullCustomer() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        ProjectInitiator projectInitiator = null;
        // ACT
        projectInitiatorLogic.updateProjectInitiator(projectInitiator);
        // ASSERT
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testUpdateCustomerAlreadyExists() throws MissingParameterException, EntityNotFoundException, EntityAlreadyExistsException {
        // ARRANGE
        AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        ProjectInitiator projectInitiator = AbstractTestDataCreator.createCustomer("Name", "Adress");
        projectInitiator.setName("Name original");
        // ACT
        projectInitiatorLogic.updateProjectInitiator(projectInitiator);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateCustomerWithNullId() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        projectInitiatorLogic.updateProjectInitiator(projectInitiator);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateNonExistingCustomer() throws EntityAlreadyExistsException, MissingParameterException, EntityNotFoundException {
        // ARRANGE
        projectInitiator.setId(Long.parseLong("999999"));
        // ACT
        projectInitiatorLogic.updateProjectInitiator(projectInitiator);
        // ASSERT
    }

    @Test
    public void testDeletedValidcustomer() throws EntityNotFoundException, MissingParameterException {
        ProjectInitiator projectInitiatorToDelet = AbstractTestDataCreator.createCustomer("Name", "Adress");
        projectInitiatorLogic.deleteProjectInitiator(projectInitiatorToDelet.getId());
        assertThat(projectInitiatorLogic.getAllProjectInitiators().contains(projectInitiatorToDelet)).isFalse();
    }

    @Test(expected = MissingParameterException.class)
    public void testDeletedNullCustomer() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        projectInitiatorLogic.deleteProjectInitiator(null);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeletedNonExistingCustomer() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        projectInitiatorLogic.deleteProjectInitiator(Long.parseLong("999999"));
        // ASSERT
    }

}
