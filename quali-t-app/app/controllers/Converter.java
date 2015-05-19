package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import exceptions.EntityNotFoundException;
import models.Interface.JIRAConnection;
import models.project.Customer;
import models.project.Project;
import models.project.QualityProperty;
import models.project.nfritem.Instance;
import models.project.nfritem.QualityPropertyStatus;
import models.project.nfritem.Val;
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
        qa.setId(json.findPath("qa").findPath("id").asLong());
        CatalogQA catalogQA = new CatalogQA(qa, getCatalogIdFromJson(json));
        catalogQA.setId(json.findPath("catalogQa").findPath("id").asLong());
        catalogQA.addVars(VariableConverter.getVarsFromJson(json));
        return catalogQA;
    }

    public static models.project.Project getProjectFromJson(JsonNode json) {
        //set general project information parameters
        models.project.Project project = new models.project.Project();
        project.setName(json.findValue("name").asText());
        project.setJiraKey(json.findValue("jiraKey").asText());
        JIRAConnection jiraConnection = new JIRAConnection();
        jiraConnection.setId(json.findPath("jiraConnection").findPath("id").asLong());
        project.setJiraConnection(jiraConnection);
        Customer projectCustomer = new Customer();
        projectCustomer.setId(json.findValue("customer").asLong());
        project.setProjectCustomer(projectCustomer);
        project.setId(json.findPath("id").asLong());
        //set additional qualityInstances
        project.addQualityAttributes(getAdditionalQualityAttributesFromJson(json));
        return project;
    }

    public static List<Long> getQualityPropertiesFromJson(JsonNode json) {
        JsonNode qualityPropertyNode = json.findPath("qualityProperties");
        return Lists.transform(qualityPropertyNode.findValuesAsText("id"), Helper.parseLongFunction());
    }

    public static List<Long> getQualityAttributeIdsFromJson(JsonNode json) {
        JsonNode qualityAttributeNode = json.findPath("qualityAttributes");
        return Lists.transform(qualityAttributeNode.findValuesAsText("id"), Helper.parseLongFunction());
    }

    private static List<Instance> getAdditionalQualityAttributesFromJson(JsonNode json) {
        JsonNode qualityAttributeNode = json.findPath("additionalQualityAttributes");
        List<Instance> qaList = new ArrayList<>();
        for (JsonNode qaNode : qualityAttributeNode) {
            qaList.add(new Instance(qaNode.findPath("description").asText()));
        }
        return qaList;
    }

    private static Catalog getCatalogIdFromJson(JsonNode json) {
        Catalog catalog = new Catalog();
        catalog.setId(json.findPath("catalog").asLong());
        return catalog;
    }

    public static Instance getInstanceFromJson(JsonNode json) {
        Instance instance = new Instance(json.findPath("description").asText());
        instance.setId(json.findPath("id").asLong());
        Val val;
        for (JsonNode var : json.findPath("values")) {
            val = new Val(var.findPath("varIndex").asInt(), var.findPath("value").asText());
            val.setId(var.findPath("id").asLong());
            instance.addValue(val);
        }
        for (JsonNode qualityPropertyNode : json.findPath("qualityPropertyStatus")) {
            QualityProperty qualityProperty = new QualityProperty();
            qualityProperty.setId(qualityPropertyNode.findPath("qp").findPath("id").asLong());
            QualityPropertyStatus qualityPropertyStatus = new QualityPropertyStatus();
            qualityPropertyStatus.setQa(instance);
            qualityPropertyStatus.setQp(qualityProperty);
            qualityPropertyStatus.setStatus(qualityPropertyNode.findPath("status").asBoolean());
            qualityPropertyStatus.setId(qualityPropertyNode.findPath("id").asLong());
            instance.addQualityPropertyStatus(qualityPropertyStatus);
        }
        return instance;
    }

    public static Project getCompleteProjectFromJson(JsonNode json) {
        Project project = getProjectFromJson(json);
        for (JsonNode instanceNode : json.findPath("qualityAttributes")) {
            project.addQualityAttribute(getInstanceFromJson(instanceNode));
        }
        return project;
    }
}
