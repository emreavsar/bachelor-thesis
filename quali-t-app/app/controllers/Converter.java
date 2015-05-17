package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import exceptions.EntityNotFoundException;
import models.template.Catalog;
import models.template.CatalogQA;
import models.template.QA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corina on 14.05.2015.
 */
public class Converter {

    public static QA getQaFromJson(JsonNode json) {
        QA qa = new QA(json.findValue("description").asText());
        qa.setId(json.findPath("id").asLong());
        return qa;
    }

    public static List<Long> getQaCategoriesFromJson(JsonNode json) throws EntityNotFoundException {
        JsonNode categoriesNode = json.findValue("categories");
        List<String> list = categoriesNode.findValuesAsText("id");
        return Lists.transform(list, Helper.parseLongFunction());
    }

    public static models.project.Customer getCustomerFromJson(JsonNode json) {
        models.project.Customer customer = new models.project.Customer(json.findPath("name").asText(), json.findPath("address").asText());
        customer.setId(json.findPath("id").asLong());
        return customer;
    }

    public static models.template.Catalog getCatalogFromJson(JsonNode json) throws EntityNotFoundException {
        models.template.Catalog catalog = new Catalog();
        catalog.setId(json.findPath("id").asLong());
        catalog.setDescription(json.findPath("description").asText());
        catalog.setName(json.findPath("name").asText());
        catalog.setPictureURL(json.findPath("image").asText());
        return catalog;
    }

    public static List<CatalogQA> getCatalogQasFromJson(JsonNode json) {
        JsonNode qas = json.findValue("selectedQualityAttributes");
        List<CatalogQA> newCatalogQAs = new ArrayList<>();
        for (JsonNode qaNode : qas) {
            QA qa = new QA();
            qa.setId(qaNode.findValue("id").asLong());
            CatalogQA catalogQA = new CatalogQA();
            catalogQA.setQa(qa);
            catalogQA.addVars(VariableConverter.getVarsFromJson(qaNode));
            newCatalogQAs.add(catalogQA);
        }
        return newCatalogQAs;
    }

    public static CatalogQA getCatalogQaFromJson(JsonNode json) {
        QA qa = new QA();
        JsonNode json2 = json.findPath("qa");
        json2 = json.findPath("id");
        qa.setId(json.findPath("qa").findPath("id").asLong());
        CatalogQA catalogQA = new CatalogQA(qa, getCatalogIdFromJson(json));
        catalogQA.addVars(VariableConverter.getVarsFromJson(json));
        return catalogQA;
    }

    private static Catalog getCatalogIdFromJson(JsonNode json) {
        Catalog catalog = new Catalog();
        catalog.setId(json.findPath("catalog").asLong());
        return catalog;
    }
}
