package controllers;

import play.mvc.Result;

import static play.mvc.Results.redirect;

public class Application {
    public static Result index() {
        return redirect("/app/index.html");
    }
}