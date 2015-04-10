package dao.models;

import dao.AbstractDAO;
import models.template.Catalog;
import models.template.QA;

import java.util.List;

/**
 * Created by corina on 31.03.2015.
 */
public class QualityAttributeDAO extends AbstractDAO<QA> {

    public List<QA> findByCatalog(Catalog cat) {
        return findAll("select q from CatalogQA q where q.catalog=?", cat);
//select distinct f from Function f inner join f.persons p where p.name = "Stefan"
    }
}
