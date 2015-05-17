package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.EntityCanNotBeDeleted;
import exceptions.EntityCanNotBeUpdated;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.template.CatalogQA;
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
        return ok(Json.toJson(logics.template.Catalog.getAllCatalogs()));
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result createCatalog() {
        JsonNode json = request().body().asJson();
        try {
            models.template.Catalog catalog = Converter.getCatalogFromJson(json);
            List<CatalogQA> newCatalogQAs = Converter.getCatalogQasFromJson(json);
            return ok(Json.toJson(logics.template.Catalog.createCatalog(catalog, newCatalogQAs)));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
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
        try {
            return ok(Json.toJson(logics.template.Catalog.createCatalogQA(Converter.getCatalogQaFromJson(json))));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result deleteCatalogQA(Long id) {
        try {
            logics.template.Catalog.deleteCatalogQA(id);
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
            return ok(Json.toJson(logics.template.Catalog.updateCatalog(Converter.getCatalogFromJson(json))));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (EntityCanNotBeUpdated e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }


    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public static Result updateCatalogQA() {
        JsonNode json = request().body().asJson();
        try {
            return ok(Json.toJson(logics.template.Catalog.updateCatalogQA(Converter.getCatalogQaFromJson(json))));
        } catch (EntityNotFoundException e) {
            return status(400, e.getMessage());
        } catch (MissingParameterException e) {
            return status(400, e.getMessage());
        }
    }
}
