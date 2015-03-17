package controllers;

import dao.models.NfrDao;
import models.Nfr;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Controller;



public class Application extends Controller {

    public static Result index() {
        return redirect("/app/index.html");
    }
    @Transactional
    public static Result getNFR(Long id) {
        NfrDao nfrDao = new NfrDao();
        Nfr firstNfr = nfrDao.readById(new Long(id));

        // TODO (emre): avoid checking for null somehow
        if(firstNfr != null) {
            return ok(Json.toJson(firstNfr));
        } else {
            return notFound();
        }
    }

}
