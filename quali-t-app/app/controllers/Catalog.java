package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import exceptions.EntityNotFoundException;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
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

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
        JsonNode json = request().body().asJson();
        List<String> qas = new ArrayList();

        qas = json.findValuesAsText("id");
        String name = json.findValue("name").asText();
        String image = json.findValue("image").asText();
        List<Long> qas_id = Lists.transform(qas, Helper.parseLongFunction());
        Logger.info(qas_id.toString());
        try {
            models.template.Catalog catalog = logics.template.Catalog.create(name, image, qas_id);
            return ok("Name: " + name + "/br QAS: " + qas_id);
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }

//        if (name.equals("") | qas_id.isEmpty()) {
//            return badRequest("Missing parameter [name] or no selected QAs");
//        } else {
//            return ok("Name: " + name + "/br QAS: " + qas_id);
//        }


    }
}
