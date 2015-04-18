import play.Application;
import play.GlobalSettings;
import play.Logger;

/**
 * Created by emre on 17/03/15.
 */
public class Global extends GlobalSettings {
    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

    @Override
    public void onStart(Application app) {
        Logger.info("Application started...");
    }

}
