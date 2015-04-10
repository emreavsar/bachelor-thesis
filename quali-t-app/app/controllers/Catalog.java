package controllers;

import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by corina on 10.04.2015.
 */
public class Catalog extends Controller {

    @Transactional
    public static Result getAllCatalogs() {
        Logger.info("getAllCatalogs Ctrl called");
        List<models.template.Catalog> catalogs = logics.template.Catalog.getAllCatalogs();
        return ok(Json.toJson(catalogs));
    }
}
