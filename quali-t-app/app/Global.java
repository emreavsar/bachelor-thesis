import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dao.authentication.RoleDao;
import dao.models.CatalogDAO;
import dao.models.CustomerDAO;
import dao.models.ProjectDAO;
import dao.models.QualityAttributeDAO;
import dao.user.TaskDao;
import logics.authentication.Authenticator;
import logics.search.Search;
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
                bind(Search.class);
                bind(CustomerDAO.class);
                bind(CatalogDAO.class);
                bind(ProjectDAO.class);
                bind(QualityAttributeDAO.class);
            }
        });
    }

    @Override
    public <A> A getControllerInstance(Class<A> aClass) throws Exception {
        return injector.getInstance(aClass);
    }
}
