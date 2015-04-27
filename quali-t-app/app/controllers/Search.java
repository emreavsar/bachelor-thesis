package controllers;

import models.AbstractEntity;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;


public class Search extends Controller {

    @Transactional
    public static Result search(String searchArgument) {
        Logger.info("search called");

        HashMap<String, ArrayList<? extends AbstractEntity>> results = logics.search.Search.search(searchArgument);

        return ok(Json.toJson(results));
    }
}
