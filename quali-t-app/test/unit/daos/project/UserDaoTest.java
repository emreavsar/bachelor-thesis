package unit.daos.project;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.UserDao;
import exceptions.EntityAlreadyExistsException;
import models.authentication.User;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class UserDaoTest extends AbstractDatabaseTest {

    @Test
    public void createUserTest() throws EntityAlreadyExistsException {
        AbstractTestDataCreator.createUser("Hans", "1234");
        User user = new UserDao().findByUsername("Hans");
        assertThat(user.getName()).isEqualTo("Hans");
    }
}