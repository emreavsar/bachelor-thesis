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

    public static Customer createCustomer(String name, String address) throws Throwable {
        Customer c = new Customer(name, address);
        persistAndFlush(c);
        return c;
    }

    public static QA createQA(String qa) throws EntityNotFoundException, MissingParameterException {
        List<Long> categories = new ArrayList();
//        return logics.template.QualityAttribute.createQA(qa, categories);$
        return null;
    }

    public static Catalog createCatalog(String name, String icon, List<Long> qaIds) throws EntityNotFoundException {
        return logics.template.Catalog.create(name, icon, qaIds);

    }

    public static QACategory createCategory(String name, Long parent, String icon) throws EntityNotFoundException {
//        return logics.template.QualityAttribute.createCat(name, parent, icon);
        return null;
    }

    public static QualityProperty createQualityProperty(String name) {
//        return logics.project.QualityProperty.createQualityProperty(name, "");
        return null;
    }
}
