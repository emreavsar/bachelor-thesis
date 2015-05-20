package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.AbstractException;
import play.mvc.Http;
import play.mvc.Result;

import static play.mvc.Results.status;


/**
 * Created by emre on 20/05/15.
 */
public interface ExceptionHandlingInterface {

    default Result catchAbstractException(Http.Request request, FunctionThatThrows<JsonNode, Result> f) {
        try {
            JsonNode json = request.body().asJson();
            return f.apply(json);
        } catch (AbstractException e) {
            return status(e.getStatusCode(), e.getMessage());
        }
    }

    default Result catchAbstractException(Long id, FunctionThatThrows<Long, Result> f) {
        try {
            return f.apply(id);
        } catch (AbstractException e) {
            return status(e.getStatusCode(), e.getMessage());
        }
    }

    default Result catchAbstractException(FunctionThatThrowsWithoutParameter<Result> f) {
        try {
            return f.apply();
        } catch (AbstractException e) {
            return status(e.getStatusCode(), e.getMessage());
        }
    }


    @FunctionalInterface
    public interface FunctionThatThrows<T, R> {
        R apply(T t) throws AbstractException;
    }

    @FunctionalInterface
    public interface FunctionThatThrowsWithoutParameter<R> {
        R apply() throws AbstractException;
    }

}
