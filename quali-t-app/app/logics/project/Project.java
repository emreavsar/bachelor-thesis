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

import static logics.project.QAInstance.addQaInstanceToProject;
import static logics.project.QAInstance.createQAInstance;

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

        models.project.Project project = projectDAO.persist(new models.project.Project(name, customer, qualityProperties));
        JsonNode qaNode = json.findPath("qualityAttributes");
        for (JsonNode qa : qaNode) {
            Instance instance = createQAInstance(qa);
            addQaInstanceToProject(instance, project);
        }
        return project;
    }

    public static List<models.project.Project> getAllProjects() {
        return projectDAO.readAll();
    }

    public static models.project.Project getProject(Long id) throws EntityNotFoundException {
        return projectDAO.readById(id);
    }
}
