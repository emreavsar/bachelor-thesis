package dao.models;

import dao.AbstractDAO;
import exceptions.EntityNotFoundException;
import models.template.QACategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corina on 13.04.2015.
 */
public class QACategoryDAO extends AbstractDAO<QACategory> {
    public List<QACategory> readAllSuperclasses() {
        return findAll("select c from QACategory c where c.parent =  NULL");
    }

    public List<QACategory> readAllById(List<Long> categoryIds) throws EntityNotFoundException {
        List<QACategory> categories = new ArrayList<>();
        for (Long id : categoryIds) {
            categories.add(readById(id));
        }
        return categories;
    }
}
