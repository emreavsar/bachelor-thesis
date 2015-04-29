package logics.template;

import dao.models.CatalogDAO;
import dao.models.CatalogQADAO;
import dao.models.QACategoryDAO;
import dao.models.QualityAttributeDAO;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameter;
import models.template.CatalogQA;
import models.template.QA;
import models.template.QACategory;
import play.Logger;

import java.util.List;

/**
 * Created by corina on 09.04.2015.
 */
public class QualityAttribute {
    public static QA createQA(String qaText, List<Long> categories) throws MissingParameter {
        if (qaText.equals("")) {
            throw new MissingParameter("QualityAttribute text can not be emtpy");
        }
        else {
            QualityAttributeDAO qaDAO = new QualityAttributeDAO();
            QA qa = new QA(qaText);
            return qaDAO.persist(qa);
        }
    }

    public static List<QA> getAllQAs() {
        QualityAttributeDAO qaDAO = new QualityAttributeDAO();
        return qaDAO.readAll();
    }

    public static List<CatalogQA> getQAsByCatalog(long id) throws EntityNotFoundException {
        CatalogQADAO catqaDAO = new CatalogQADAO();
        QualityAttributeDAO qadao = new QualityAttributeDAO();
        CatalogDAO catalogDAO = new CatalogDAO();
        models.template.Catalog cat = catalogDAO.readById(id);
        return catqaDAO.findByCatalog(cat);
    }

//    public static QACategory createSubCat(Long id, String name) throws EntityNotFoundException {
//        QACategoryDAO catDao = new QACategoryDAO();
//        QACategory parent = catDao.readById(id);
//        QACategory cat = new QACategory(parent, name);
//        catDao.persist(parent);
//        return cat;
//    }

    public static QACategory createCat(String name) {
        QACategory cat = new QACategory(name);
        QACategoryDAO catDao = new QACategoryDAO();
        return catDao.persist(cat);
    }

    public static QACategory getCategoryTree(Long id) throws EntityNotFoundException {
        QACategoryDAO catDAO = new QACategoryDAO();
        return catDAO.readById(id);
    }

    public static List<QACategory> getAllCats() {
        QACategoryDAO catDAO = new QACategoryDAO();
        return catDAO.readAllSuperclasses();
    }

    public static QACategory createCat(String name, Long parentid) throws EntityNotFoundException {
        Logger.info("logic called. name: " + name + " parent: " + parentid);
        QACategoryDAO catDao = new QACategoryDAO();

        if (parentid == null) {
            QACategory cat = new QACategory(name);
            return catDao.persist(cat);
        } else  {
            QACategory parent = catDao.readById(parentid);
            QACategory cat = new QACategory(parent, name);
            catDao.persist(parent);
            return cat;
        }
    }

    public static void deleteCategory(Long id) throws EntityNotFoundException {
        QACategoryDAO qaCategoryDAO = new QACategoryDAO();
        QACategory category = getCategoryTree(id);
        qaCategoryDAO.remove(category);

    }

    public static QACategory updateCat(Long id, String name, String icon) throws EntityNotFoundException {
        QACategoryDAO qaCategoryDAO = new QACategoryDAO();
        QACategory category = qaCategoryDAO.readById(id);
        category.setName(name);
        category.setIcon(icon);
        return qaCategoryDAO.persist(category);
    }
}