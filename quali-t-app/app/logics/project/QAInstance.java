package logics.project;

import com.fasterxml.jackson.databind.JsonNode;
import dao.models.CatalogQADAO;
import dao.models.ProjectDAO;
import exceptions.EntityNotFoundException;
import models.project.nfritem.Instance;
import models.project.nfritem.Val;
import models.template.CatalogQA;

/**
 * Created by corina on 11.05.2015.
 */
public class QAInstance {
    static CatalogQADAO catalogQADAO = new CatalogQADAO();
    static ProjectDAO projectDAO = new ProjectDAO();

    public static Instance createQAInstance(JsonNode qaNode) throws EntityNotFoundException {
        CatalogQA catalogQA = catalogQADAO.readById(qaNode.findPath("id").asLong());
//        CatalogQA catalogQA2 = catalogQADAO.readById(Long.parseLong("3012"));
        Instance qa = new Instance(qaNode.findPath("description").asText(), catalogQA);
        for (JsonNode var : qaNode.findPath("values")) {
            qa.addValue(new Val(var.findPath("varIndex").asInt(), var.findPath("value").asText()));
        }
        return qa;
    }

    public static models.project.Project addQaInstanceToProject(Instance instance, models.project.Project project) throws EntityNotFoundException {
        models.project.Project updatedProject = projectDAO.readById(project.getId());
        instance.addQualityProperty(updatedProject.getQualityProperties());
        updatedProject.addQualityAttribute(instance);
        return projectDAO.update(updatedProject);
    }
}
