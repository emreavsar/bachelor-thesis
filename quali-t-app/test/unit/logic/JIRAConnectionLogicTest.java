package unit.logic;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.interfaces.JIRAConnectionDAO;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.JIRAConnectionLogic;
import models.Interface.JIRAConnection;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by corina on 20.05.2015.
 */
public class JIRAConnectionLogicTest extends AbstractDatabaseTest {
    private JIRAConnection jiraConnection;
    private JIRAConnection persistedjiraConnection;
    private JIRAConnectionLogic jiraConnectionLogic;
    private JIRAConnection jiraConnectionToUpdate;
    private JIRAConnectionDAO jiraConnectionDAO;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        jiraConnection = new JIRAConnection("host", "user", "password");
        persistedjiraConnection = AbstractTestDataCreator.createJiraConnection("host", "user", "password");
        jiraConnectionToUpdate = new JIRAConnection("new host", "new user", "new password");
        jiraConnectionLogic = getInjector().getInstance(JIRAConnectionLogic.class);
        jiraConnectionDAO = getInjector().getInstance(JIRAConnectionDAO.class);
    }

    @Test
    public void testCreateValidJiraConnection() throws MissingParameterException {
        // ARRANGE
        // ACT
        JIRAConnection newJiraConnection = jiraConnectionLogic.createJIRAConnection(jiraConnection);
        // ASSERT
        assertThat(newJiraConnection.getId()).isNotNull();
        assertThat(newJiraConnection.getHostAddress()).isEqualTo("host");
        assertThat(newJiraConnection.getPassword()).isEqualTo("password");
        assertThat(newJiraConnection.getUsername()).isEqualTo("user");
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateJiraConnectionEmptyHost() throws MissingParameterException {
        // ARRANGE
        jiraConnection.setHostAddress("");
        // ACT
        jiraConnectionLogic.createJIRAConnection(jiraConnection);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateJiraConnectionEmptyUser() throws MissingParameterException {
        // ARRANGE
        jiraConnection.setUsername("");
        // ACT
        jiraConnectionLogic.createJIRAConnection(jiraConnection);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateJiraConnectionEmptyPassword() throws MissingParameterException {
        // ARRANGE
        jiraConnection.setPassword("");
        // ACT
        jiraConnectionLogic.createJIRAConnection(jiraConnection);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateNullJiraConnection() throws MissingParameterException {
        // ARRANGE
        // ACT
        jiraConnectionLogic.createJIRAConnection(null);
        // ASSERT
    }

    /////////////
    @Test
    public void testUpdateValidJiraConnection() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        jiraConnectionToUpdate.setId(persistedjiraConnection.getId());
        // ACT
        JIRAConnection newJiraConnection = jiraConnectionLogic.updateJIRAConnection(jiraConnectionToUpdate);
        // ASSERT
        assertThat(newJiraConnection.getId()).isEqualTo(persistedjiraConnection.getId());
        assertThat(newJiraConnection.getHostAddress()).isEqualTo("new host");
        assertThat(newJiraConnection.getPassword()).isEqualTo("new password");
        assertThat(newJiraConnection.getUsername()).isEqualTo("new user");
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateJiraConnectionEmptyHost() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        jiraConnectionToUpdate.setId(persistedjiraConnection.getId());
        jiraConnectionToUpdate.setHostAddress("");
        // ACT
        jiraConnectionLogic.updateJIRAConnection(jiraConnectionToUpdate);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateJiraConnectionEmptyUser() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        jiraConnectionToUpdate.setId(persistedjiraConnection.getId());
        jiraConnectionToUpdate.setUsername("");
        // ACT
        jiraConnectionLogic.updateJIRAConnection(jiraConnectionToUpdate);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateJiraConnectionEmptyPassword() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        jiraConnectionToUpdate.setId(persistedjiraConnection.getId());
        jiraConnectionToUpdate.setPassword("");
        // ACT
        jiraConnectionLogic.updateJIRAConnection(jiraConnectionToUpdate);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateNullJiraConnection() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        jiraConnectionLogic.updateJIRAConnection(null);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateJiraConnectionInvalidId() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        jiraConnectionToUpdate.setId(new Long(9999));
        // ACT
        jiraConnectionLogic.updateJIRAConnection(jiraConnectionToUpdate);
        // ASSERT
    }

    @Test
    public void testDeleteValidJiraConnection() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        jiraConnectionLogic.deleteJIRAConnection(persistedjiraConnection.getId());
        // ASSERT
        assertThat(jiraConnectionDAO.readAll().contains(persistedjiraConnection)).isFalse();
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteJiraConnectionInvalidId() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        jiraConnectionLogic.deleteJIRAConnection(new Long(99999));
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testDeleteJiraConnectionNullId() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        jiraConnectionLogic.deleteJIRAConnection(null);
        // ASSERT
    }

}
