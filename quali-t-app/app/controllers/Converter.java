package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import exceptions.EntityNotFoundException;
import models.template.QA;

import java.util.List;

/**
 * Created by corina on 14.05.2015.
 */
public class Converter {
    static QA getQaFromJson(JsonNode json) {
        QA qa = new QA(json.findValue("description").asText());
        qa.setId(json.findPath("id").asLong());
        return qa;
    }

    public static List<Long> getQaCategoriesFromJson(JsonNode json) throws EntityNotFoundException {
        JsonNode categoriesNode = json.findValue("categories");
        List<String> list = categoriesNode.findValuesAsText("id");
        return Lists.transform(list, Helper.parseLongFunction());
    }

    static models.project.Customer getCustomerFromJson(JsonNode json) {
        models.project.Customer customer = new models.project.Customer(json.findPath("name").asText(), json.findPath("address").asText());
        customer.setId(json.findPath("id").asLong());
        return customer;
    }
}
