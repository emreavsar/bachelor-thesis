package base;

import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.authentication.Authenticator;
import models.Interface.JIRAConnection;
import models.authentication.Token;
import models.authentication.User;
import models.project.Project;
import models.project.ProjectInitiator;
import models.project.QualityProperty;
import models.project.nfritem.Instance;
import models.project.nfritem.Val;
import models.template.*;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Credits: EEPPI Project
 */
public abstract class AbstractTestDataCreator {
    private static void persistAndFlushInTransaction(Object entity) {
        JPA.withTransaction(() -> persistAndFlush(entity));
    }

    public static void persistAndFlush(Object... entities) {
        EntityManager em = JPA.em();
        for (Object entity : entities) {
            em.persist(entity);
        }
        em.flush();
    }

    public static User createUser(String name, String password) throws EntityAlreadyExistsException, MissingParameterException, EntityNotFoundException {
        return TestDependencyUtil.createInjector().getInstance(Authenticator.class).registerUser(name, password);
    }

    public static User createUserWithToken(String name, String password, String token) throws EntityAlreadyExistsException, MissingParameterException, EntityNotFoundException {
        User user = TestDependencyUtil.createInjector().getInstance(Authenticator.class).registerUser(name, password);
        LocalDateTime date = LocalDateTime.now();
        date = date.plusMonths(6);
        user.addToken(new Token(token, date, user));
        persistAndFlush(user);
        return user;
    }

    public static ProjectInitiator createCustomer(String name, String address) {
        ProjectInitiator c = new ProjectInitiator(name, address);
        persistAndFlush(c);
        return c;
    }

    public static QA createQA(String qaDescription) throws EntityNotFoundException, MissingParameterException {
        QA qa = new QA(qaDescription, 1);
        persistAndFlush(qa);
        return qa;
    }

    public static QA createQA(String description, List<QACategory> qaCategories) throws MissingParameterException, EntityNotFoundException {
        QA qa = createQA(description);
        qa.addCategories(qaCategories);
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

    public static CatalogQA createCatalogQA(QA qa, Catalog catalog) {
        CatalogQA catalogQA = new CatalogQA(qa, catalog);
        catalog.addCatalogQA(catalogQA);
        qa.addCatalogQA(catalogQA);
        persistAndFlush(catalogQA);
        return catalogQA;
    }

    public static CatalogQA createCatalogQA(QA qa, Catalog catalog, List<QAVar> qaVars) {
        CatalogQA catalogQA = createCatalogQA(qa, catalog);
        catalogQA.addVars(qaVars);
        persistAndFlush(catalogQA);
        return catalogQA;
    }

    public static Instance createInstance(String description, Project project, CatalogQA catalogQA) {
        Instance instance = new Instance(description);
        persistAndFlush(instance);
        instance.setProject(project);
        instance.setTemplate(catalogQA);
        persistAndFlush(instance);
        return instance;
    }

    public static Instance createFullInstance(String description, Project project, CatalogQA catalogQA) {
        Instance instance = new Instance(description);
        persistAndFlush(project);
        persistAndFlush(catalogQA);
        persistAndFlush(instance);
//        instance.setProject(project);
        project.addQualityAttribute(instance);
        instance.setTemplate(catalogQA);
        catalogQA.getQaInstances().add(instance);
        persistAndFlush(instance);
        return instance;
    }

    public static Instance createInstanceWithVars(String descirption, List<Val> valueList) {
        Instance instance = new Instance(descirption);
        instance.addValues(valueList);
        persistAndFlush(instance);
        return instance;
    }

    public static Instance addValueToInstance(Instance instance, Val value) {
        instance.addValue(value);
        persistAndFlush(instance);
        return instance;
    }

    public static Project createProject(Project project) {
        persistAndFlush(project);
        return project;
    }

    public static JIRAConnection createJiraConnection(String hostname, String user, String password) {
        JIRAConnection jiraConnection = new JIRAConnection(hostname, user, password);
        persistAndFlush(jiraConnection);
        return jiraConnection;
    }

    public static Instance addQualityPropertyStatusToInstance(Instance instance, QualityProperty qualityProperty, Boolean status) {
        instance.addQualityProperty(qualityProperty, status);
        persistAndFlush(instance);
        return instance;
    }
}
