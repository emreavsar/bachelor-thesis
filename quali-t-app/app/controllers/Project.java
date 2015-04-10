package controllers;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;


/**
 * Created by corina on 10.04.2015.
 */
public class Project extends Controller {

    @Transactional
    public static Result createProject() {
        return null;
    }
}
