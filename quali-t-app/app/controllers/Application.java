package controllers;


import models.NFR;
import play.libs.Json;
import play.*;
import play.mvc.*;
import views.html.*;
import play.mvc.Controller;


public class Application extends Controller {

    public static Result index() {
        return redirect("/app/index.html");
    }

    // TODO will this be in another Controller Java Class?
    // check EEPPI
    public static Result getNFR(Long id) {
        NFR nfr = new NFR(1, "Die Backend-Applikation von QUALI-T muss im Jahr 99.99% erreichbar sein.");
        return ok(Json.toJson(nfr));
    }

}
