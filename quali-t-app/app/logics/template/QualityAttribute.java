package logics.template;

import dao.models.CatalogDAO;
import dao.models.CatalogQADAO;
import dao.models.QACategoryDAO;
import dao.models.QualityAttributeDAO;
import models.template.CatalogQA;
import models.template.QA;
import models.template.QACategory;
import play.Logger;

import java.util.List;

/**
 * Created by corina on 09.04.2015.
 */
public class QualityAttribute {
    public static QA createQA(String qaText) {
        if (qaText.equals("")) {
            return null; }
        else {
            QualityAttributeDAO qaDAO = new QualityAttributeDAO();
            QA qa = new QA(qaText);
            qaDAO.persist(qa);
            return qa;
        }
    }

    public static List<QA> getAllQAs() {
        QualityAttributeDAO qaDAO = new QualityAttributeDAO();
        List<QA> qas = qaDAO.readAll();
        return qas;
    }

    public static List<CatalogQA> getQAsByCatalog(long id) {
        CatalogQADAO catqaDAO = new CatalogQADAO();
        QualityAttributeDAO qadao= new QualityAttributeDAO();
        CatalogDAO catalogDAO = new CatalogDAO();
        models.template.Catalog cat = catalogDAO.readById(id);
        Logger.info("logic called " + cat);
        List<CatalogQA> qas = catqaDAO.findByCatalog(cat);
        return qas;
    }

    public static QACategory createSubCat(Long id, String name) {
        QACategoryDAO catDao = new QACategoryDAO();
        QACategory parent = catDao.readById(id);
        QACategory cat = new QACategory(parent, name);
        catDao.persist(parent);
        return cat;
    }

    public static QACategory createCat(String name) {
        QACategory cat = new QACategory(name);
        QACategoryDAO catDao = new QACategoryDAO();
        catDao.persist(cat);

    return cat;
    }

    public static QACategory getCategoryTree(Long id) {
        QACategoryDAO catDAO = new QACategoryDAO();
        QACategory cat = catDAO.readById(id);
        return cat;
    }
}