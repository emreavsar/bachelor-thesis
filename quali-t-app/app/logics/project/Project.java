package logics.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import controllers.Helper;
import dao.models.*;
import exceptions.EntityNotFoundException;
import models.project.Customer;
import models.project.QualityProperty;
import models.project.nfritem.Instance;

import java.util.List;

import static logics.project.QAInstance.*;

/**
 * Created by corina on 08.04.2015.
 */
public class Project {
    static ProjectDAO projectDAO = new ProjectDAO();
    static CustomerDAO customerDAO = new CustomerDAO();
    static CatalogDAO catalogDAO = new CatalogDAO();
    static QualityPropertyDAO qualityPropertyDAO = new QualityPropertyDAO();
    static CatalogQADAO catalogQADAO = new CatalogQADAO();
    static QADAO qaDAO = new QADAO();
    /**
     * Creates and persists customer.
     *
     * @param json
     * @return Project
     */

    public static models.project.Project createProject(JsonNode json) throws EntityNotFoundException {

        String name = json.findValue("name").asText();
        Long customerId = json.findValue("customer").asLong();

        JsonNode qualityPropertyNode = json.findValue("qualityProperties");
        List<Long> qpIds = Lists.transform(qualityPropertyNode.findValuesAsText("id"), Helper.parseLongFunction());
        List<QualityProperty> qualityProperties = qualityPropertyDAO.readAllById(qpIds);
        Customer customer = customerDAO.readById(customerId);

        models.project.Project project = projectDAO.persist(new models.project.Project(qualityProperties));
        setProjectParameters(project, json);
        JsonNode qaNode = json.findPath("qualityAttributes");
        for (JsonNode qa : qaNode) {
            Instance instance = createQAInstance(qa);
            addQaInstanceToProject(instance, project);
        }
        return project;
    }

    private static models.project.Project setProjectParameters(models.project.Project project, JsonNode json) throws EntityNotFoundException {
        project.setName(json.findPath("name").asText());
        project.setProjectCustomer(customerDAO.readById(json.findPath("customer").asLong()));
        return projectDAO.update(project);
    }

    public static List<models.project.Project> getAllProjects() {
        return projectDAO.readAll();
    }

    public static models.project.Project getProject(Long id) throws EntityNotFoundException {
        return projectDAO.readById(id);
    }

    public static models.project.Project updateProject(JsonNode json) throws EntityNotFoundException {
        //update project parameters
        models.project.Project project = getProject(json.findPath("id").asLong());
        setProjectParameters(project, json);
        //update Variable Values
        for (JsonNode qa : json.findPath("qualityAttributes")) {
            if (qa.findPath("id").asLong() == 0) {
                Instance instance = createQAInstance(qa);
                project.addQualityAttribute(instance);
            } else {
                updateQAInstance(qa);
            }
        }
        return project;
    }

    public static void deleteProject(Long id) throws EntityNotFoundException {
        models.project.Project project = projectDAO.readById(id);
        project.removeQualityProperties();
        projectDAO.remove(projectDAO.update(project));
    }

    public static models.project.Project createInstance(JsonNode json) throws EntityNotFoundException {
        models.project.Project project = getProject(json.findPath("id").asLong());
        for (JsonNode qa : json.findPath("qualityAttributes")) {
            Instance instance = createQAInstance(qa);
            project.addQualityAttribute(instance);
        }
        return projectDAO.update(project);
    }

    public static void deleteInstance(Long id) throws EntityNotFoundException {
        qaInstanceDAO.remove(qaInstanceDAO.readById(id));
    }
}
