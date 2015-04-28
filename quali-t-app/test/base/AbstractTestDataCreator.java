package base;

import exceptions.EntityNotCreatedException;
import logics.authentication.Authenticator;
import models.authentication.User;
import models.project.Customer;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;

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

    public static User createUser(String name, String password) throws EntityNotCreatedException {
        return Authenticator.registerUser(name, password);
    }

    public static Customer createCustomer(String name, String address) throws Throwable {
        Customer c = new Customer(name, address);
        persistAndFlush(c);
        return c;
    }
}
