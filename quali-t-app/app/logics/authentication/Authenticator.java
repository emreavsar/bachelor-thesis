package logics.authentication;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dao.authentication.RoleDao;
import dao.authentication.TokenDao;
import dao.models.UserDao;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.PasswordsNotMatchException;
import models.authentication.Role;
import models.authentication.Token;
import models.authentication.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import play.Logger;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by emre on 31/03/15.
 */
@Singleton
public class Authenticator {
    @Inject
    private UserDao userDao;
    @Inject
    private TokenDao tokenDao;
    @Inject
    private RoleDao roleDao;

    public Authenticator() {
        Logger.info("constructor Authenticator() is called");
    }

    public User update(User u) {
        userDao.update(u);
        return u;
    }

    /**
     * Does simple authentication. Returns the user if authentication was ok. Otherwise null.
     *
     * @param username
     * @param password
     * @param token
     * @return user
     */
    public Token authenticate(String username, String password, String token) throws EntityNotFoundException, InvalidParameterException, PasswordsNotMatchException {
        if (username == null) {
            throw new InvalidParameterException("Username must not be specified!");
        }

        UserDao userDao = new UserDao();
        User u = userDao.findByUsername(username);

        // if user exists, check password
        if (u != null) {
            // check for token (could be invalid or non-existing)
            Token tokenOfUser = null;
            if (token != null) {
                tokenOfUser = tokenDao.findByToken(token);
            }

            if (tokenOfUser != null) {
                if (isTokenOfUser(tokenOfUser, u) && isTokenValid(tokenOfUser)) {
                    return tokenOfUser;
                }
            } else {
                // no token
                if (checkPassword(u, password)) {
                    Token newToken = generateToken(u);
                    u.getToken().add(newToken);
                    userDao.persist(u);
                    return newToken;
                }
            }
        }
        // if no token found or created
        throw new EntityNotFoundException("User or password / token not matching");
    }

    /**
     * Checks the validUntil property of the token and compares it with todays date
     *
     * @param tokenOfUser
     * @return
     */
    public boolean isTokenValid(Token tokenOfUser) {
        Logger.info("in tokenIsValid, argument=" + tokenOfUser);
        DateTime validUntil = tokenOfUser.getValidUntil();
        Logger.info("isvaliduntilbefore? = validUntil.isBefore(new DateTime())");
        if (validUntil.isAfter(new DateTime())) {
            return true;
        }

        return false;
    }

    /**
     * Checks the token if its available and belonging to the given user
     *
     * @param token
     * @param u
     * @return
     */
    public boolean isTokenOfUser(Token token, User u) {
        return token != null && token.getUser().getId().equals(u.getId());
    }

    /**
     * Checks wether the password of the user is correct or not
     *
     * @param user
     * @param password
     * @return
     */
    public boolean checkPassword(@Nullable User user, @NotNull String password) throws PasswordsNotMatchException, EntityNotFoundException {
        if (user != null) {
            if (user.getHashedPassword().equals(calculatePasswordHash(user.getSalt(), password))) {
                return true;
            } else {
                throw new PasswordsNotMatchException("Wrong password provided");
            }
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    /**
     * Calculates the password based on the hash
     *
     * @param salt
     * @param password
     * @return
     */
    private String calculatePasswordHash(@NotNull String salt, @NotNull String password) {
        String hashedPassword = "";

        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("SHA");
            //Add password bytes to digest
            md.update(salt.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest(password.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    /**
     * Generates a token for the corresponding user which is valid for 6 months
     *
     * @param user
     * @return
     */
    public Token generateToken(User user) {
        String generatedToken = generateTokenString();

        DateTime date = new DateTime();
        date = date.plusMonths(6);
        Token t = new Token(generatedToken, date, user);

        return t;
    }

    public static String generateTokenString() {
        return RandomStringUtils.randomAlphanumeric(20).toString();
    }

    /**
     * Creates the user in the database with a salt, hashed password and default rules.
     * Returns the message of the backend
     *
     * @param username
     * @param password
     * @return
     */
    public User registerUser(String username, String password) throws EntityAlreadyExistsException {
        // Default roles for registered user
        List<Role> defaultRoles = roleDao.findDefaultRoles();

        // check if user already exists
        User user = userDao.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setName(username);
            user.initSalt();
            user.setHashedPassword(calculatePasswordHash(user.getSalt(), password));
            user.getRoles().addAll(roleDao.findDefaultRoles());
            userDao.persist(user);
            return user;
        } else {
            throw new EntityAlreadyExistsException("User already exists");
        }
    }

    /**
     * Invalidates the users session by removing the token. Removes the token only if token belongs to the given user
     *
     * @param username
     * @param token
     * @return
     */
    public void invalidateUserSession(String username, String token) throws EntityNotFoundException {
        User user = userDao.findByUsername(username);
        Token userToken = tokenDao.findByToken(token);

        if (userToken != null) {
            // prevent that a user can invalidate sessions of others
            if (userToken.getUser().equals(user)) {
                user.removeToken(userToken);
                userDao.persist(user);
                tokenDao.remove(userToken);
            } else {
                throw new EntityNotFoundException("Error at invalidating user session.");
            }
        } else {
            throw new EntityNotFoundException("No token for user found");
        }
    }

    public void changePassword(String username, String newPassword, String newPasswordRepeated) throws PasswordsNotMatchException {
        User user = userDao.findByUsername(username);

        if (newPassword.equals(newPasswordRepeated)) {
            user.setHashedPassword(calculatePasswordHash(user.getSalt(), newPassword));
        } else {
            throw new PasswordsNotMatchException("New and repeated password do not match!");
        }
    }

    public User getUser(long userid) throws EntityNotFoundException {
        User u = userDao.readById(userid);
        if (u == null) {
            throw new EntityNotFoundException("No user found");
        }
        return u;
    }
}
