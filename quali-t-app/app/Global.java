import dao.authentication.RoleDao;
import dao.authentication.TokenDao;
import dao.models.NfrDao;
import dao.models.UserDao;
import models.authentication.Role;
import models.authentication.User;
import models.template.Nfr;
import play.*;
import play.db.jpa.JPA;
import play.libs.Yaml;

import java.util.List;
import java.util.Map;

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
        try {
            // TODO emre: this is getting called *every* time when the app starts
            // -> causes lose of data on productive systems
            // -> OK on dev/heroku
            // -> NOK on prod
            JPA.withTransaction("default", false, () -> {
                // put some sample data from initial-data.yml
                Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml.load("initial-data.yml");

                Logger.info("Start resetting the database.\n");

                Logger.info("\tResetting Roles start");
                resetRoles(all.get("roles"));
                Logger.info("\tResetting Roles done\n");

                resetTokens();

                Logger.info("\tResetting Users start");
                resetUser(all.get("users"));
                Logger.info("\tResetting Users done\n");

                Logger.info("\tResetting Nfrs start");
                resetNfr(all.get("nfrs"));
                Logger.info("\tResetting Nfrs done\n");

                Logger.info("Resetting the database done.");
                return null;
            });
        } catch (Throwable throwable) {
            play.Logger.error("Error at initializing database with conf/initial-data.yml file");
            // TODO write to log but nowhere else!
            throwable.printStackTrace();
        }
    }

    private void resetNfr(List<Object> nfrs) {
        NfrDao nfrDao = new NfrDao();
        nfrDao.removeAll();

        for (Object nfr : nfrs) {
            Nfr casted = (Nfr) nfr;
            Nfr merged = JPA.em().merge(casted);
        }
    }

    private void resetTokens() {
        TokenDao tokenDao = new TokenDao();
        tokenDao.removeAll();
    }

    private void resetUser(List<Object> users) {
        UserDao userDao = new UserDao();
        RoleDao roleDao = new RoleDao();
        userDao.removeAll();

        for (Object u : users) {
            User casted = (User) u;
            User merged = JPA.em().merge(casted);
        }

        // set correct roles for the admin
        User admin = userDao.findByUsername("admin");
        admin.getRoles().addAll(roleDao.findAdminRoles());
        userDao.persist(admin);

        // set correct roles for the rest
        List<Role> defaultRoles = roleDao.findDefaultRoles();

        User rest = userDao.findByUsername("emre.avsar92@gmail.com");
        rest.getRoles().addAll(defaultRoles);
        userDao.persist(rest);

        rest = userDao.findByUsername("corina");
        rest.getRoles().addAll(roleDao.findDefaultRoles());
        userDao.persist(rest);

        rest = userDao.findByUsername("ozimmermann");
        rest.getRoles().addAll(roleDao.findDefaultRoles());
        userDao.persist(rest);
    }

    private void resetRoles(List<Object> roles) {
        RoleDao roleDao = new RoleDao();
        roleDao.removeAll();

        for (Object u : roles) {
            Role casted = (Role) u;
            Role merged = JPA.em().merge(casted);
        }
    }

}
