package logics.user;

import com.google.inject.Inject;
import dao.authentication.RoleDao;
import dao.models.UserDao;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.Helper;
import models.authentication.Role;
import models.authentication.User;

import java.util.List;

/**
 * Created by corina on 02.06.2015.
 */


public class UserLogic {
    @Inject
    private UserDao userDao;
    @Inject
    private RoleDao roleDao;
    @Inject
    private Helper helper;

    public List<Role> getAllRoles() {
        return roleDao.readAll();
    }

    public List<User> getAllUsers() {
        return userDao.readAll();
    }


    public void deleteUser(Long id) throws EntityNotFoundException {
        User user = userDao.readById(id);
        user.setFavorites(null);
        user.setRoles(null);
        userDao.remove(user);
    }


    public User createUser(User user, List<Long> roleIds) throws EntityNotFoundException, MissingParameterException {
        if (user != null && user.getName() != null && user.getHashedPassword() != null && roleIds != null && helper.validate(user.getHashedPassword()) != helper.validate(user.getName())) {
            user.setId(null);
            addUserRoles(user, roleIds);
            return userDao.persist(user);
        }
        throw new MissingParameterException("Please provide all required Parameters!");
    }

    private void addUserRoles(User user, List<Long> roleIds) throws EntityNotFoundException {
        user.getRoles().clear();
        for (Long roleId : roleIds) {
            user.addRole(roleDao.readById(roleId));
        }
    }

    public User updateUser(User user, List<Long> roleIds) throws EntityNotFoundException, MissingParameterException {
        if (user != null && user.getId() != null && user.getName() != null && user.getHashedPassword() != null && roleIds != null && helper.validate(user.getHashedPassword()) != helper.validate(user.getName())) {
            User persistedUser = userDao.readById(user.getId());
            persistedUser.setName(user.getName());
            user.removeRoles();
            addUserRoles(user, roleIds);
            return userDao.update(user);
        }
        throw new MissingParameterException("Please provide all required Parameters!");
    }
}
