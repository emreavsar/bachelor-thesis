package logics.template;

import com.fasterxml.jackson.databind.JsonNode;
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

    public static QACategory createCategory(JsonNode json) throws EntityNotFoundException {
        QACategory qaCategory = new QACategory(json.findPath("name").asText(), json.findPath("icon").asText());
        if (json.findPath("parent").asLong() != 0) {
            QACategory parent = qaCategoryDAO.readById(json.findPath("parent").asLong());
            qaCategory.setParent(parent);
        }
        return qaCategoryDAO.persist(qaCategory);
    }

    public static void deleteCategory(Long id) throws EntityNotFoundException {
        QACategory category = getCategoryTree(id);
        qaCategoryDAO.remove(category);
    }

    public static QACategory updateCat(JsonNode json) throws EntityNotFoundException {
        QACategory category = qaCategoryDAO.readById(json.findPath("id").asLong());
        category.setName(json.findPath("name").asText());
        category.setIcon(json.findPath("icon").asText());
        return qaCategoryDAO.update(category);
    }
//
//    public static QACategory createSubCategory(QACategory ent, Long parentId) throws EntityNotFoundException {
//        ent.setParent(qaCategoryDAO.readById(parentId));
//        return null;
////        return createCategory(ent);
//
//    }
}
