import com.google.inject.Guice;
import com.google.inject.Injector;
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
        injector = Guice.createInjector();
    }

    @Override
    public <A> A getControllerInstance(Class<A> aClass) throws Exception {
        return injector.getInstance(aClass);
    }
}
