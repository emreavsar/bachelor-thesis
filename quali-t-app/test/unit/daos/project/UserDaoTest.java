package unit.daos.project;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.UserDao;
import exceptions.EntityAlreadyExistsException;
import exceptions.MissingParameterException;
import models.authentication.User;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class UserDaoTest extends AbstractDatabaseTest {
    private UserDao userDao;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        userDao = getInjector().getInstance(UserDao.class);
    }

    @Test
    public void createUserTest() throws EntityAlreadyExistsException, MissingParameterException {
        // ARRANGE
        AbstractTestDataCreator.createUser("Hans", "1234");
        // ACT
        User user = userDao.findByUsername("Hans");
        // ASSERT
        assertThat(user.getName()).isEqualTo("Hans");
    }
}