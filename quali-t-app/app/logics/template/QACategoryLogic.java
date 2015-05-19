package logics.template;

import dao.models.QACategoryDAO;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.template.QACategory;

import java.util.List;

import static controllers.Helper.validate;

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

    public static QACategory createCategory(QACategory qaCategory) throws EntityNotFoundException, MissingParameterException {
        if (qaCategory != null && validate(qaCategory.getName())) {
            qaCategory.setId(null);
            if (qaCategory.getParent() != null) {
                QACategory parent = qaCategoryDAO.readById(qaCategory.getParent().getId());
                qaCategory.setParent(parent);
            }
            return qaCategoryDAO.persist(qaCategory);
        }
        throw new MissingParameterException("Please provide all Parameters!");
    }

    public static void deleteCategory(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            QACategory category = getCategoryTree(id);
            qaCategoryDAO.remove(category);
        }
        throw new MissingParameterException("Please provide all Parameters!");
    }

    public static QACategory updateCat(QACategory qaCategory) throws EntityNotFoundException, MissingParameterException {
        if (qaCategory != null && qaCategory.getId() != null && validate(qaCategory.getName())) {
            QACategory persistedQaCategory = qaCategoryDAO.readById(qaCategory.getId());
            persistedQaCategory.setName(qaCategory.getName());
            persistedQaCategory.setIcon(qaCategory.getIcon());
            return qaCategoryDAO.update(persistedQaCategory);
        }
        throw new MissingParameterException("Please provide all Parameters!");
    }
}
