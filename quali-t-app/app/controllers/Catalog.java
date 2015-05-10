package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.EntityCanNotBeDeleted;
import exceptions.EntityNotFoundException;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by corina on 10.04.2015.
 */
public class Catalog extends Controller {

    @SubjectPresent
    @Transactional
    public static Result getAllCatalogs() {
        Logger.info("getAllCatalogs Ctrl called");
        List<models.template.Catalog> catalogs = logics.template.Catalog.getAllCatalogs();
        return ok(Json.toJson(catalogs));
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result createCatalog() {
        JsonNode json = request().body().asJson();
        try {
            models.template.Catalog catalog = logics.template.Catalog.create(json);
            return ok(Json.toJson(catalog));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result deleteCatalog(long id) {
        try {
            logics.template.Catalog.deleteCatalog(id);
            return status(202);
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (EntityCanNotBeDeleted e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result createCatalogQA() {
        JsonNode json = request().body().asJson();
        JsonNode qa = json.findValue("qa");
        JsonNode catalogQANode = json.findValue("catalogQa");
        try {
            models.template.CatalogQA catalogQA = logics.template.Catalog.createCatalogQA(qa, catalogQANode);
            return ok(Json.toJson(catalogQA));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result deleteCatalogQA(Long id) {
        try {
            logics.template.Catalog.removeQaFromCatalog(id);
            return status(202);
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result updateCatalog() {
        JsonNode json = request().body().asJson();
        try {
            models.template.Catalog catalog = logics.template.Catalog.update(json);
            return ok(Json.toJson(catalog));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }


    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result updateCatalogQA() {
        JsonNode json = request().body().asJson();
        JsonNode catalogQANode = json.findValue("catalogQa");
        try {
            models.template.CatalogQA catalogQA = logics.template.Catalog.updateCatalogQA(catalogQANode);
            return ok(Json.toJson(catalogQA));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        }
    }
}
