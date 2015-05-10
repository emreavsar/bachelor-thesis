package dao.models;

import dao.AbstractDAO;
import models.template.Catalog;
import models.template.CatalogQA;
import models.template.QA;

import java.util.List;

/**
 * Created by corina on 13.04.2015.
 */
public class CatalogQADAO extends AbstractDAO<CatalogQA> {
    public List<CatalogQA> findByCatalog(Catalog cat) {
        return findAll("select q from CatalogQA q where q.catalog=? and q.deleted=false", cat);
    }

    public CatalogQA findByCatalogAndId(Catalog cat, QA qa) {
        return find("select q from CatalogQA q where q.qa=? and q.catalog=?", qa, cat);
    }

    public List<CatalogQA> findAllByQA(QA qa) {
        return findAll("select q from CatalogQA q where q.qa=?", qa);
    }
}
