package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import exceptions.EntityNotFoundException;
import models.Interface.JIRAConnection;
import models.authentication.Token;
import models.authentication.User;
import models.project.ProjectInitiator;
import models.project.Project;
import models.project.QualityProperty;
import models.project.nfritem.Instance;
import models.project.nfritem.QualityPropertyStatus;
import models.project.nfritem.Val;
import models.template.Catalog;
import models.template.CatalogQA;
import models.template.QA;
import models.template.QACategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corina on 14.05.2015.
 */
public class JsonConverter {
    @Inject
    private Helper helper;
    @Inject
    private VariableConverter variableConverter;

    public QA getQaFromJson(JsonNode json) {
        QA qa = new QA(json.findValue("description").asText());
        qa.setId(json.findPath("id").asLong());
        return qa;
    }

    public List<Long> getQaCategoriesFromJson(JsonNode json) throws EntityNotFoundException {
        JsonNode categoriesNode = json.findPath("categories");
        List<String> list = categoriesNode.findValuesAsText("id");
        return Lists.transform(list, helper.parseLongFunction());
    }

    public ProjectInitiator getProjectInitiatorFromJson(JsonNode json) {
        ProjectInitiator projectInitiator = new ProjectInitiator(json.findPath("name").asText(), json.findPath("address").asText());
        projectInitiator.setId(json.findPath("id").asLong());
        return projectInitiator;
    }

    public Catalog getCatalogFromJson(JsonNode json) {
        Catalog catalog = new Catalog();
        catalog.setId(json.findPath("id").asLong());
        catalog.setDescription(json.findPath("description").asText());
        catalog.setName(json.findPath("name").asText());
        catalog.setImage(json.findPath("image").asText());
        return catalog;
    }

    public List<CatalogQA> getCatalogQasFromJson(JsonNode json) {
        JsonNode qas = json.findValue("selectedQualityAttributes");
        List<CatalogQA> newCatalogQAs = new ArrayList<>();
        for (JsonNode qaNode : qas) {
            QA qa = new QA();
            qa.setId(qaNode.findValue("id").asLong());
            CatalogQA catalogQA = new CatalogQA();
            catalogQA.setQa(qa);
            catalogQA.addVars(variableConverter.getVarsFromJson(qaNode));
            newCatalogQAs.add(catalogQA);
        }
        return newCatalogQAs;
    }

    public CatalogQA getCatalogQaFromJson(JsonNode json) {
        QA qa = new QA();
        qa.setId(json.findPath("qa").findPath("id").asLong());
        CatalogQA catalogQA = new CatalogQA(qa, getCatalogIdFromJson(json));
        catalogQA.setId(json.findPath("catalogQa").findPath("id").asLong());
        catalogQA.addVars(variableConverter.getVarsFromJson(json));
        return catalogQA;
    }

    public models.project.Project getProjectFromJson(JsonNode json) {
        //set general project information parameters
        models.project.Project project = new models.project.Project();
        project.setName(json.findPath("name").asText());
        project.setJiraKey(json.findPath("jiraKey").asText());
        if (project.getJiraKey().equals("null")) {
            project.setJiraKey(null);
        }
        JIRAConnection jiraConnection = new JIRAConnection();
        jiraConnection.setId(json.findPath("jiraConnection").findPath("id").asLong());
        project.setJiraConnection(jiraConnection);
        ProjectInitiator projectInitiator = new ProjectInitiator();
        projectInitiator.setId(json.findPath("projectInitiator").asLong());
        project.setProjectInitiator(projectInitiator);
        project.setId(json.findPath("id").asLong());
        //set additional qualityInstances
        project.addQualityAttributes(getAdditionalQualityAttributesFromJson(json));
        return project;
    }

    public List<Long> getQualityPropertiesFromJson(JsonNode json) {
        JsonNode qualityPropertyNode = json.findPath("qualityProperties");
        return Lists.transform(qualityPropertyNode.findValuesAsText("id"), helper.parseLongFunction());
    }

    public List<Long> getQualityAttributeIdsFromJson(JsonNode json) {
        JsonNode qualityAttributeNode = json.findPath("qualityAttributes");
        if (qualityAttributeNode.findPath("id").isMissingNode()) {
            List<Long> idList = new ArrayList<>();
            for (JsonNode id : qualityAttributeNode) {
                idList.add(id.asLong());
            }
            return idList;
        } else {
            return Lists.transform(qualityAttributeNode.findValuesAsText("id"), helper.parseLongFunction());
        }
    }

    private List<Instance> getAdditionalQualityAttributesFromJson(JsonNode json) {
        JsonNode qualityAttributeNode = json.findPath("additionalQualityAttributes");
        List<Instance> qaList = new ArrayList<>();
        for (JsonNode qaNode : qualityAttributeNode) {
            qaList.add(new Instance(qaNode.findPath("description").asText()));
        }
        return qaList;
    }

    private Catalog getCatalogIdFromJson(JsonNode json) {
        Catalog catalog = new Catalog();
        catalog.setId(json.findPath("catalog").asLong());
        return catalog;
    }

    public Instance getInstanceFromJson(JsonNode json) {
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

    public Project getCompleteProjectFromJson(JsonNode json) {
        Project project = getProjectFromJson(json);
        for (JsonNode instanceNode : json.findPath("qualityAttributes")) {
            project.addQualityAttribute(getInstanceFromJson(instanceNode));
        }
        return project;
    }

    public QualityProperty getQualityPropertyFromJson(JsonNode json) {
        QualityProperty qualityProperty = new QualityProperty(json.findPath("name").asText(), json.findPath("description").asText());
        qualityProperty.setId(json.findPath("id").asLong());
        return qualityProperty;
    }

    public QACategory getCategoryFromJson(JsonNode json) {
        QACategory qaCategory = new QACategory(json.findPath("name").asText(), json.findPath("icon").asText());
        qaCategory.setId(json.findPath("id").asLong());
        if (json.findPath("parent").asLong() != 0) {
            QACategory parent = new QACategory();
            parent.setId(json.findPath("parent").asLong());
            qaCategory.setParent(parent);
        }
        return qaCategory;
    }

    public JIRAConnection getJiraConnectionFromJson(JsonNode json) {
        JIRAConnection jiraConnection = new JIRAConnection(json.findPath("hostAddress").asText(), json.findPath("username").asText(), json.findPath("password").asText());
        jiraConnection.setId(json.findPath("id").asLong());
        return jiraConnection;
    }

    public Project getProjectIdFromJson(JsonNode json) {
        Project project = new Project();
        project.setId(json.findPath("projectId").asLong());
        return project;
    }

    public List<Long> getQualityAttributeIdsToExportFromJson(JsonNode json) {
        List<Long> qualityAttributeIds = new ArrayList<>();
        for (JsonNode qualityAtributeId : json.findPath("selectedQas")) {
            qualityAttributeIds.add(qualityAtributeId.longValue());
        }
        return qualityAttributeIds;
    }

    public Boolean getExportAsRawBoolean(JsonNode json) {
        return (new Boolean(json.findPath("exportAsRaw").asBoolean()));
    }

    public Catalog getImportCatalogFromJson(JsonNode json) {
        // get catalog from json
        Catalog catalog = getCatalogFromJson(json.findPath("catalog"));
        catalog.setId(null);
        List<QACategory> qaCategories = new ArrayList<>();
        // get qa's from json
        for (JsonNode qaNode : json.findPath("qualityAttributes")) {
            QA qa = getQaFromJson(qaNode.findPath("qa"));
            qa.setId(null);
            qaCategories.clear();
            for (JsonNode categoryNode : qaNode.findPath("categories")) {
                qaCategories.add(new QACategory(categoryNode.get("name").asText(), categoryNode.get("icon").asText()));
            }
            qa.addCategories(qaCategories);
            CatalogQA catalogQA = new CatalogQA();
            catalogQA.setQa(qa);
            catalogQA.addVars(variableConverter.getVarsFromJson(qaNode));
            catalog.addCatalogQA(catalogQA);
        }
        return catalog;
    }

    public User getUserFromJson(JsonNode json) {
        User user = new User();
        user.setId(json.findPath("id").asLong());
        user.setName(json.findPath("username").asText());
        user.setHashedPassword(json.findPath("password").asText());
        user.addToken(new Token(json.findPath("token").asText()));
        return user;
    }

    public List<Long> getUserRolesFromJson(JsonNode json) {
        List<Long> roleIds = new ArrayList<>();
        for (JsonNode roleId : json.findPath("selectedRoles")) {
            roleIds.add(roleId.asLong());
        }
        return roleIds;
    }
}
