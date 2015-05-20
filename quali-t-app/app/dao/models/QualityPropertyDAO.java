package dao.models;

import dao.AbstractDAO;
import exceptions.EntityNotFoundException;
import models.project.QualityProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corina on 24.04.2015.
 */
public class QualityPropertyDAO extends AbstractDAO<QualityProperty> {
    public List<QualityProperty> readAllById(List<Long> qpIds) throws EntityNotFoundException {
        List<QualityProperty> qps = new ArrayList();
        for (Long qp : qpIds) {
            qps.add(readById(qp));
        }
        return qps;
    }

    public QualityProperty findByName(String name) {
        return find("select q from QualityProperty q where q.name=?", name);
    }
}
