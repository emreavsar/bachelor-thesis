package dao.models;

import dao.AbstractDAO;
import models.authentication.User;

/**
 * Created by emre on 30/03/15.
 */
public class UserDao extends AbstractDAO<User> {
    public User findByUsername(String name) {
        return find("select u from User u where u.name=?", name);
    }
}
