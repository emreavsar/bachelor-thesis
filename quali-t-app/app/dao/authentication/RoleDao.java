package dao.authentication;

import dao.AbstractDAO;
import exceptions.EntityNotFoundException;
import models.authentication.Role;
import util.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emre on 31/03/15.
 */
public class RoleDao extends AbstractDAO<Role> {
    public List<Role> findDefaultRoles() throws EntityNotFoundException {
        List<Role> roleList = new ArrayList<>();
        for (String role : GlobalVariables.defaultRoles) {
            roleList.add(find("select r from Role r where r.name = ?", role));
        }
        return roleList;
    }

    public List<Role> findAdminRoles() throws EntityNotFoundException {
        List<Role> roleList = new ArrayList<>();
        for (String role : GlobalVariables.adminRoles) {
            roleList.add(find("select r from Role r where r.name = ?", role));
        }
        return roleList;
    }
}
