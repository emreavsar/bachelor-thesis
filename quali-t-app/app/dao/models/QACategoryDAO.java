package dao.models;

import dao.AbstractDAO;
import models.template.QACategory;

import java.util.List;

/**
 * Created by corina on 13.04.2015.
 */
public class QACategoryDAO extends AbstractDAO<QACategory> {
    public List<QACategory> readAllSuperclasses() {
        return findAll("select c from QACategory c where c.parent =  NULL");
    }
}
