package dao.authentication;

import dao.AbstractDAO;
import models.authentication.Role;
import models.authentication.User;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by emre on 31/03/15.
 */
public class RoleDao extends AbstractDAO<Role> {
    public List<Role> findDefaultRoles() {
        // TODO implement default roles here, atm it is returning all roles!
        Long crudRole = 2001L;
        return findAll("select r from Role r where r.id in (?)", crudRole);
    }
}
