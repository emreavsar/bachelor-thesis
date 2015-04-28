package unit.daos.user;

import dao.models.UserDao;
import exceptions.EntityNotCreatedException;
import models.authentication.User;
import org.junit.Test;
import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;

import static org.fest.assertions.Assertions.assertThat;

public class UserDaoTest extends AbstractDatabaseTest {

	@Test
	public void createUserTest() throws EntityNotCreatedException {
		AbstractTestDataCreator.createUser("Hans", "1234");
		User user = new UserDao().findByUsername("Hans");
		assertThat(user.getName()).isEqualTo("Hans");
	}
}
