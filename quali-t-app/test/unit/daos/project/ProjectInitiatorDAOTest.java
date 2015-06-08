package unit.daos.project;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.ProjectInitiatorDAO;
import models.project.ProjectInitiator;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


/**
 * Created by emre on 28/04/15.
 */
public class ProjectInitiatorDAOTest extends AbstractDatabaseTest {

    private ProjectInitiatorDAO projectInitiatorDAO;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        projectInitiatorDAO = getInjector().getInstance(ProjectInitiatorDAO.class);
    }

    @Test
    public void findByNameTest() throws Throwable {
        // ARRANGE
        ProjectInitiator persistedProjectInitiator = AbstractTestDataCreator.createCustomer("IFS", "Rapperswil");
        // ACT
        ProjectInitiator projectInitiator = projectInitiatorDAO.findByName("IFS");
        // ASSERT
        assertThat(projectInitiator.getId()).isEqualTo(persistedProjectInitiator.getId());
        assertThat(projectInitiator.getName()).isEqualTo("IFS");
        assertThat(projectInitiator.getAddress()).isEqualTo("Rapperswil");
    }
}
