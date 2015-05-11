package logics.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.models.*;
import exceptions.EntityNotFoundException;
import models.project.nfritem.Instance;
import models.project.nfritem.QualityPropertyStatus;
import models.project.nfritem.Val;
import models.template.CatalogQA;

/**
 * Created by corina on 11.05.2015.
 */
public class QAInstance {
    static CatalogQADAO catalogQADAO = new CatalogQADAO();
    static ProjectDAO projectDAO = new ProjectDAO();
    static ValDAO valDAO = new ValDAO();
    static QAInstanceDAO qaInstanceDAO = new QAInstanceDAO();
    static QualityPropertyStatusDAO qualityPropertyStatusDAO = new QualityPropertyStatusDAO();

    public static Instance createQAInstance(JsonNode qaNode) throws EntityNotFoundException {
        CatalogQA catalogQA = catalogQADAO.readById(qaNode.findPath("id").asLong());
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

    public static Instance updateQAInstance(JsonNode qaNode) throws EntityNotFoundException {
        //check if necesary!!
        ((ObjectNode) qaNode).remove("template");
        //update values
        for (JsonNode var : qaNode.findPath("values")) {
            Val value = valDAO.readById(var.findPath("id").asLong());
            value.setValue(var.findPath("value").asText());
            valDAO.persist(value);
        }
        //update quality properties status
        updateQualityPropertyStatus(qaNode);
        //update description
        Instance qaInstance = qaInstanceDAO.readById(qaNode.findPath("id").asLong());
        qaInstance.setDescription(qaNode.findPath("description").asText());
        return qaInstanceDAO.update(qaInstance);
    }

    public static void updateQualityPropertyStatus(JsonNode qaNode) throws EntityNotFoundException {
        QualityPropertyStatus qpStatus;
        for (JsonNode qp : qaNode.findPath("qualityPropertyStatus")) {
            qpStatus = qualityPropertyStatusDAO.readById(qp.findPath("id").asLong());
            if (qp.findPath("status").asText() == "true") {
                qpStatus.setStatus(true);
            } else {
                qpStatus.setStatus(false);
                qualityPropertyStatusDAO.update(qpStatus);
            }
        }
    }

}
