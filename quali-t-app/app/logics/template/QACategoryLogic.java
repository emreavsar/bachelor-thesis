package logics.template;

import dao.models.QACategoryDAO;
import exceptions.EntityNotFoundException;
import models.template.QACategory;

import java.util.List;

/**
 * Created by corina on 06.05.2015.
 */


public class QACategoryLogic {
    static QACategoryDAO qaCategoryDAO = new QACategoryDAO();

    public static QACategory getCategoryTree(Long id) throws EntityNotFoundException {
        return qaCategoryDAO.readById(id);
    }

    public static List<QACategory> getAllCats() {
        return qaCategoryDAO.readAllSuperclasses();
    }

    //    public static QACategory createCat(String name, Long parentid, String icon) throws EntityNotFoundException {
//        Logger.info("logic called. name: " + name + " parent: " + parentid + " icon:  " + icon);
//        QACategoryDAO catDao = new QACategoryDAO();
//
//        if (parentid == null) {
//            QACategory cat = new QACategory(name, icon);
//            return catDao.persist(cat);
//        } else  {
//            QACategory parent = catDao.readById(parentid);
//            QACategory cat = new QACategory(parent, name, icon);
//            catDao.persist(parent);
//            return cat;
//        }
//    }
    public static QACategory createCategory(QACategory category) {
        return qaCategoryDAO.persist(category);

    }

    public static void deleteCategory(Long id) throws EntityNotFoundException {
        QACategory category = getCategoryTree(id);
        qaCategoryDAO.remove(category);
    }

    public static QACategory updateCat(Long id, String name, String icon) throws EntityNotFoundException {
        QACategory category = qaCategoryDAO.readById(id);
        category.setName(name);
        category.setIcon(icon);
        return qaCategoryDAO.persist(category);
    }

    public static QACategory createSubCategory(QACategory ent, Long parentId) throws EntityNotFoundException {
        ent.setParent(qaCategoryDAO.readById(parentId));
        return createCategory(ent);

    }
}
