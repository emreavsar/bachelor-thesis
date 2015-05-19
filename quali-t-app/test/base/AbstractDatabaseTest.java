package base;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import play.db.jpa.JPA;
import play.test.FakeApplication;
import play.test.Helpers;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.HashMap;

import static play.test.Helpers.inMemoryDatabase;

/**
 * Credits: EEPPI Project
 */

public abstract class AbstractDatabaseTest extends AbstractTest {

    private static FakeApplication app;
    private EntityManager em;
    private EntityTransaction tx;
    private boolean committed = false;

    @BeforeClass
    public static void startApp() {
        HashMap<String, String> settings = new HashMap<>();
        settings.put("DATABASE_TO_UPPER", "false");
        settings.put("MODE", "PostgreSQL");

        app = Helpers.fakeApplication(inMemoryDatabase("default", settings));
        Helpers.start(app);
    }

    @AfterClass
    public static void stopApp() {
        Helpers.stop(app);
    }

    @Before
    public void setUp() throws Exception {
        em = JPA.em("default");
        JPA.bindForCurrentThread(em);
        tx = em.getTransaction();
        tx.begin();
    }

    @After
    public void tearDown() {
        try {
            tx.rollback();
        } finally {
            JPA.bindForCurrentThread(null);
            if (em != null) {
                em.close();
            }
        }
        if (committed) {
            stopApp();
            startApp();
            committed = false;
        }
    }

    protected void flush() {
        JPA.em().flush();
    }

    protected void persistAndFlush(Object o) {
        JPA.em().persist(o);
        flush();
    }

    protected void persist(Object o) {
        JPA.em().persist(o);
    }

    protected void commit() {
        tx.commit();
        tx.begin();
        committed = true;
    }
}
