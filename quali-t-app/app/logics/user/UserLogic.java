package logics.user;

import com.google.inject.Inject;
import dao.authentication.RoleDao;
import dao.models.UserDao;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.Helper;
import logics.authentication.Authenticator;
import models.authentication.Role;
import models.authentication.User;

import java.util.ArrayList;
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
    @Inject
    private Authenticator authenticator;

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

    public User createUser(User user, List<Long> roleIds) throws EntityNotFoundException, MissingParameterException, EntityAlreadyExistsException {
        if (user != null && roleIds != null) {
            User newUser = authenticator.registerUser(user.getName(), user.getHashedPassword());
            addUserRoles(newUser, roleIds);
            return userDao.update(newUser);
        }
        throw new MissingParameterException("Please provide all required Parameters!");
    }

    public User updateUser(User user, List<Long> roleIds) throws EntityNotFoundException, MissingParameterException, EntityAlreadyExistsException {
        if (user != null && user.getId() != null && user.getName() != null && user.getHashedPassword() != null && roleIds != null && helper.validate(user.getHashedPassword()) != helper.validate(user.getName())) {
            User persistedUser = userDao.readById(user.getId());
            //check if username already exists
            if (!persistedUser.getName().equals(user.getName())) {
                if (userDao.findByUsername(user.getName()) != null) {
                    throw new EntityAlreadyExistsException("Username already exists!");
                }
            }
            persistedUser.setName(user.getName());
            updateUserRoles(persistedUser, roleIds);
            return userDao.update(persistedUser);
        }
        throw new MissingParameterException("Please provide all required Parameters!");
    }

    private void updateUserRoles(User user, List<Long> roleIds) throws EntityNotFoundException {
        //find roles to remove
        List<Role> rolesToRemove = new ArrayList<>();
        for (Role role : user.getRoles()) {
            if (!roleIds.contains(role.getId())) {
                rolesToRemove.add(role);
            }
        }
        //add new roles
        for (Long roleId : roleIds) {
            Role role = roleDao.readById(roleId);
            if (!user.getRoles().contains(role)) {
                user.addRole(role);
            }
        }
        //remove marked roles form user
        for (Role roleToRemove : rolesToRemove) {
            user.getRoles().remove(roleToRemove);
        }
    }

    private void addUserRoles(User user, List<Long> roleIds) throws EntityNotFoundException {
        user.getRoles().clear();
        for (Long roleId : roleIds) {
            user.addRole(roleDao.readById(roleId));
        }
    }
}
