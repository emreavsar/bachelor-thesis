package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import logics.template.CatalogLogic;
import models.template.Catalog;
import models.template.CatalogQA;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.JsonConverter;

import java.util.List;

/**
 * Created by corina on 10.04.2015.
 */
public class CatalogController extends Controller implements ExceptionHandlingInterface {
    @Inject
    private CatalogLogic catalogLogic;
    @Inject
    private JsonConverter jsonConverter;

    @SubjectPresent
    @Transactional
    public Result getAllEditableCatalogs() {
        Logger.info("getAllEditableCatalogs called");
        return ok(Json.toJson(catalogLogic.getAllEditableCatalogs()));
    }

    @SubjectPresent
    @Transactional
    public Result getAllCatalogs() {
        Logger.info("getAllCatalogs called");
        return ok(Json.toJson(catalogLogic.getAllCatalogs()));
    }

    @SubjectPresent
    @Transactional
    public Result getCatalog(Long id) {
        Logger.info("getCatalog called");
        return catchAbstractException(id, catalogId -> {
            return ok(Json.toJson(catalogLogic.getCatalog(catalogId)));
        });
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result createCatalog() {
        Logger.info("createCatalog called");
        return catchAbstractException(request(), json -> {
            Catalog catalog = jsonConverter.getCatalogFromJson(json);
            List<CatalogQA> newCatalogQAs = jsonConverter.getCatalogQasFromJson(json);
            return ok(Json.toJson(catalogLogic.createCatalog(catalog, newCatalogQAs)));
        });
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result deleteCatalog(long id) {
        Logger.info("deleteCatalog called");
        return catchAbstractException(id, catalogId -> {
            catalogLogic.deleteCatalog(catalogId);
            return status(202);
        });
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result createCatalogQA() {
        Logger.info("createCatalogQA called");
        return catchAbstractException(request(), json -> ok(Json.toJson(catalogLogic.createCatalogQA(jsonConverter.getCatalogQaFromJson(json)))));
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result deleteCatalogQA(Long id) {
        Logger.info("deleteCatalogQA called");
        return catchAbstractException(id, catalogId -> {
            catalogLogic.deleteCatalogQA(catalogId);
            return status(202);
        });
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result updateCatalog() {
        Logger.info("updateCatalog called");
        return catchAbstractException(request(), json -> ok(Json.toJson(catalogLogic.updateCatalog(jsonConverter.getCatalogFromJson(json)))));
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result updateCatalogQA() {
        Logger.info("updateCatalogQA called");
        return catchAbstractException(request(), json -> ok(Json.toJson(catalogLogic.updateCatalogQA(jsonConverter.getCatalogQaFromJson(json)))));
    }

    @SubjectPresent
    @Transactional
    public Result getCatalogQA(Long id) {
        Logger.info("getCatalogQA called");
        return catchAbstractException(id, catalogId -> ok(Json.toJson(catalogLogic.getCatalogQA(id))));
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result importCatalog() {
        Logger.info("importCatalog called");
        return catchAbstractException(request(), json -> {
            Catalog catalog = jsonConverter.getImportCatalogFromJson(json);
            return ok(Json.toJson(catalogLogic.importCatalog(catalog)));
        });
    }

    @Restrict({@Group("curator"), @Group("admin")})
    @Transactional
    public Result exportCatalog(Long id) {
        Logger.info("exportCatalog called");
        return catchAbstractException(id, catalogId -> {
            return ok(catalogLogic.getCatalogToExport(catalogId));
        });
    }
}
