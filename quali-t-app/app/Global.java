import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dao.authentication.RoleDao;
import dao.models.CustomerDAO;
import dao.user.TaskDao;
import logics.authentication.Authenticator;
import play.Application;
import play.GlobalSettings;
import play.Logger;

/**
 * Created by emre on 17/03/15.
 */
public class Global extends GlobalSettings {
    private Injector injector;

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

    @Override
    public void onStart(Application app) {
        Logger.info("Application started...");
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(TaskDao.class);
                bind(RoleDao.class);
                bind(Authenticator.class);
                bind(CustomerDAO.class);
            }
        });
    }

    @Override
    public <A> A getControllerInstance(Class<A> aClass) throws Exception {
        return injector.getInstance(aClass);
    }
}
