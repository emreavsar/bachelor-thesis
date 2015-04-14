package dao.models;

import dao.AbstractDAO;
import models.template.Catalog;
import models.template.CatalogQA;

import java.util.List;

/**
 * Created by corina on 13.04.2015.
 */
public class CatalogQADAO extends AbstractDAO<CatalogQA> {
        public List<CatalogQA> findByCatalog(Catalog cat) {
            return findAll("select q from CatalogQA q where q.catalog=?", cat);
        }
}
