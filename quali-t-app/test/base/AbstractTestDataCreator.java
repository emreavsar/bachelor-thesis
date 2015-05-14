package base;

import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.authentication.Authenticator;
import models.authentication.User;
import models.project.Customer;
import models.project.QualityProperty;
import models.template.Catalog;
import models.template.QA;
import models.template.QACategory;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Credits: EEPPI Project
 */
public abstract class AbstractTestDataCreator {

    private static void persistAndFlushInTransaction(Object entity) {
        JPA.withTransaction(() -> persistAndFlush(entity));
    }

    private static void persistAndFlush(Object... entities) {
        EntityManager em = JPA.em();
        for (Object entity : entities) {
            em.persist(entity);
        }
        em.flush();
    }

    public static User createUserWithTransaction(final String name, final String password) throws Throwable {
        return JPA.withTransaction(() -> createUser(name, password));
    }

    public static User createUser(String name, String password) throws EntityAlreadyExistsException {
        return Authenticator.registerUser(name, password);
    }

    public static Customer createCustomer(String name, String address) {
        Customer c = new Customer(name, address);
        persistAndFlush(c);
        return c;
    }

    public static QA createQA(String qaDescirption) throws EntityNotFoundException, MissingParameterException {
        List<Long> categories = new ArrayList();
        QA qa = new QA(qaDescirption, 1);
        persistAndFlush(qa);
        return qa;
    }

    public static Catalog createCatalog(String name, String image, String description, List<QA> qas) throws EntityNotFoundException {
        Catalog catalog = new Catalog(name, description, image);
        catalog.addTemplates(qas);
        persistAndFlush(catalog);
        return catalog;
    }

    public static QACategory createCategory(String name, QACategory parent, String icon) throws EntityNotFoundException {
        QACategory category;
        if (parent == null) {
            category = new QACategory(name, icon);
        } else {
            category = new QACategory(parent, name, icon);
        }
        persistAndFlush(category);
        return category;
    }

    public static QualityProperty createQualityProperty(String name, String description) {
        QualityProperty qualityProperty = new QualityProperty(name, description);
        persistAndFlush(qualityProperty);
        return qualityProperty;
    }
}
