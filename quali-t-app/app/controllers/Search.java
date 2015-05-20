package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.google.inject.Inject;
import models.AbstractEntity;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;


public class Search extends Controller {
    @Inject
    private logics.search.Search searchLogic;

    @SubjectPresent
    @Transactional
    public Result search(String searchArgument) {
        Logger.info("search called");

        HashMap<String, ArrayList<? extends AbstractEntity>> results = searchLogic.search(searchArgument.toLowerCase());

        return ok(Json.toJson(results));
    }
}
