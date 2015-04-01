package dao.authentication;

import dao.AbstractDAO;
import models.authentication.Role;
import models.authentication.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emre on 31/03/15.
 */
public class RoleDao extends AbstractDAO<Role> {
    public List<Role> findDefaultRoles() {
        // TODO implement default roles here, atm it is returning all roles!
        return findAll("select r from Role r");
    }
}
