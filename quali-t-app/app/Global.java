import controllers.Application;
import play.GlobalSettings;
import play.Logger;

/**
 * Created by emre on 17/03/15.
 */
public class Global extends GlobalSettings {
    public void onStart(Application app) {
        Logger.info("Application has started");
    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }
}
