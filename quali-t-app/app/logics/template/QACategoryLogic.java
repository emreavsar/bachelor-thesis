package logics.template;

import com.google.inject.Inject;
import logics.Helper;
import dao.models.QACategoryDAO;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.template.QACategory;

import java.util.List;

/**
 * Created by corina on 06.05.2015.
 */


public class QACategoryLogic {
    @Inject
    private QACategoryDAO qaCategoryDAO;
    @Inject
    private Helper helper;

    public QACategory getCategoryTree(Long id) throws EntityNotFoundException {
        return qaCategoryDAO.readById(id);
    }

    public List<QACategory> getAllCats() {
        return qaCategoryDAO.readAllSuperclasses();
    }

    public QACategory createCategory(QACategory qaCategory) throws EntityNotFoundException, MissingParameterException {
        if (qaCategory != null && helper.validate(qaCategory.getName())) {
            qaCategory.setId(null);
            if (qaCategory.getParent() != null) {
                if (qaCategory.getParent().getId() != null) {
                    QACategory parent = qaCategoryDAO.readById(qaCategory.getParent().getId());
                    qaCategory.setParent(parent);
                } else {
                    throw new MissingParameterException("Please provide a valid Parent ID!");
                }
            }
            return qaCategoryDAO.persist(qaCategory);
        }
        throw new MissingParameterException("Please provide all Parameters!");
    }

    public void deleteCategory(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            QACategory category = getCategoryTree(id);
            qaCategoryDAO.remove(category);
        } else {
            throw new MissingParameterException("Please provide all Parameters!");
        }
    }

    public QACategory updateCategory(QACategory qaCategory) throws EntityNotFoundException, MissingParameterException {
        if (qaCategory != null && qaCategory.getId() != null && helper.validate(qaCategory.getName())) {
            QACategory persistedQaCategory = qaCategoryDAO.readById(qaCategory.getId());
            persistedQaCategory.setName(qaCategory.getName());
            persistedQaCategory.setIcon(qaCategory.getIcon());
            return qaCategoryDAO.update(persistedQaCategory);
        }
        throw new MissingParameterException("Please provide all Parameters!");
    }
}
