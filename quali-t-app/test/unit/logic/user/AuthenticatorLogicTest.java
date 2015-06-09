package unit.logic.user;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import com.google.inject.Inject;
import dao.models.UserDao;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import exceptions.PasswordsNotMatchException;
import logics.authentication.Authenticator;
import models.authentication.Token;
import models.authentication.User;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by emre on 28/04/15.
 */
public class AuthenticatorLogicTest extends AbstractDatabaseTest {
    @Inject
    private Authenticator authenticator;
    private User userWithToken;
    private User user;
    private Token token;
    private UserDao userDao;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        authenticator = getInjector().getInstance(Authenticator.class);
        userDao = getInjector().getInstance(UserDao.class);
        AbstractTestDataCreator.createUser("username", "password");
        userWithToken = AbstractTestDataCreator.createUserWithToken("userWithToken", "pw", "token");
    }

//    @Test
//    public void isTokenValidWithValidTokenTest() {
//        LocalDateTime validUntil = LocalDateTime.now().plusDays(30);
//        Token t = new Token("xxx", validUntil, null);
//        assertThat(authenticator.isTokenValid(t)).isTrue();
//    }
//
//    @Test
//    public void isTokenValidWithInvalidTokenTest() {
//        LocalDateTime validUntil = LocalDateTime.now().minusDays(1);
//        Token t = new Token("xxx", validUntil, null);
//        assertThat(authenticator.isTokenValid(t)).isFalse();
//    }

    @Test
    public void testAuthenticateValidUserWihtoutToken() throws EntityNotFoundException, MissingParameterException, PasswordsNotMatchException {
        // ARRANGE
        // ACT
        token = authenticator.authenticate("username", "password", null);
        // ASSERT
        assertThat(token.getToken()).isNotEmpty();
        assertThat(token.getUser().getName()).isEqualTo("username");
    }

    @Test
    public void testAuthenticateValidUserWithToken() throws EntityNotFoundException, MissingParameterException, PasswordsNotMatchException {
        // ARRANGE

        // ACT
        token = authenticator.authenticate("userWithToken", "pw", "token");
        // ASSERT
        assertThat(token.getToken()).isEqualTo("token");
        assertThat(token.getUser().getName()).isEqualTo("userWithToken");
    }


    @Test
    public void testAuthenticateUserWithInvalidToken() throws EntityNotFoundException, MissingParameterException, PasswordsNotMatchException {
        // ARRANGE

        // ACT
        token = authenticator.authenticate("userWithToken", "pw", "12345");
        // ASSERT
        assertThat(token.getToken()).isNotEqualTo("12345");
        assertThat(token.getToken()).isNotEmpty();
    }

    @Test(expected = MissingParameterException.class)
    public void testAuthenticateWihtoutUsername() throws EntityNotFoundException, MissingParameterException, PasswordsNotMatchException {
        // ARRANGE
        // ACT
        token = authenticator.authenticate(null, "pw", "12345");
        // ASSERT
    }

    @Test
    public void testRegisterValidUser() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        user = authenticator.registerUser("newUser", "pw");
        // ASSERT
        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo("newUser");
        assertThat(user.getRoles().size()).isEqualTo(1);
        assertThat(user.getRoles().get(0).getName()).isEqualTo("projectmanager");
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testRegisterUserAlreadyExists() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        user = authenticator.registerUser("username", "pw");
        // ASSERT
    }


    @Test(expected = MissingParameterException.class)
    public void testRegisterUserEmptyPw() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        user = authenticator.registerUser("username", null);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testRegisterUserNullUser() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        user = authenticator.registerUser(null, "pw");
        // ASSERT
    }

    @Test
    public void testInvalidateUserSession() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        authenticator.invalidateUserSession("userWithToken", "token");
        user = userDao.findByUsername("userWithToken");
        // ASSERT
        assertThat(user.getToken()).isEmpty();
    }

    @Test(expected = EntityNotFoundException.class)
    public void testInvalidateUserSessionInvalidUser() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        authenticator.invalidateUserSession("blubb", "token");
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testInvalidateUserSessionNullUser() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        authenticator.invalidateUserSession(null, "token");
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testInvalidateUserSessionNullToken() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        authenticator.invalidateUserSession("userWithToken", null);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testInvalidateUserSessionInvalidToken() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        authenticator.invalidateUserSession("userWithToken", "11111");
        // ASSERT
    }

    @Test
    public void testChangePassword() throws PasswordsNotMatchException, EntityNotFoundException {
        // ARRANGE
        // ACT
        authenticator.changePassword("userWithToken", "pw", "password", "password");
        user = userDao.findByUsername("userWithToken");
        // ASSERT
        assertThat(user.getHashedPassword()).isNotEmpty();
    }

    @Test(expected = PasswordsNotMatchException.class)
    public void testChangePasswordUserPwNotMatch() throws PasswordsNotMatchException, EntityNotFoundException {
        // ARRANGE
        // ACT
        authenticator.changePassword("userWithToken", "password", "password", "password");
        // ASSERT
    }

    @Test(expected = PasswordsNotMatchException.class)
    public void testChangePasswordNewPwNotMatch() throws PasswordsNotMatchException, EntityNotFoundException {
        // ARRANGE
        // ACT
        authenticator.changePassword("userWithToken", "pw", "password1", "password");
        // ASSERT
    }
}
