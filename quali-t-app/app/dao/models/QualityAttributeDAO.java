package dao.models;

import dao.AbstractDAO;
import exceptions.EntityNotFoundException;
import models.template.Catalog;
import models.template.QA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corina on 31.03.2015.
 */
public class QualityAttributeDAO extends AbstractDAO<QA> {
    public List<QA> findByCatalog(Catalog cat) {
        return findAll("select q from CatalogQA q where q.catalog=?", cat);
    }
    
    public List<QA> findAllById(List<Long> qaIds) throws EntityNotFoundException {
        List<QA> qas = new ArrayList();
        for (Long qa : qaIds) {
            qas.add(readById(qa));
        }
        return qas;
    }
}
