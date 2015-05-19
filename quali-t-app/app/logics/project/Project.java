package logics.project;

import dao.models.*;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.project.QualityProperty;
import models.project.nfritem.Instance;
import models.project.nfritem.QualityPropertyStatus;
import models.project.nfritem.Val;
import models.template.CatalogQA;

import java.util.ArrayList;
import java.util.List;

import static controllers.Helper.validate;
import static logics.project.QAInstance.qualityPropertyStatusDAO;
import static logics.project.QAInstance.valDAO;

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
    static QAInstanceDAO qaInstanceDAO = new QAInstanceDAO();

    /**
     * Creates and persists customer.
     *
     * @param
     * @return Project
     */

    public static models.project.Project createProject(models.project.Project project, List<Long> qualityAttrributeIdList, List<Long> qualityPropertyIdList) throws EntityNotFoundException {
        project.setId(null);
        //create qa instances
        CatalogQA catalogQA;
        for (Long id : qualityAttrributeIdList) {
            catalogQA = catalogQADAO.readById(id);
            project.addQualityAttribute(new Instance(catalogQA.getQa().getDescription(), catalogQA));
        }
        // set project parameters
        setProjectParameters(project);
        setProjectQualityProperties(project, qualityPropertyIdList);
        projectDAO.persist(project);
        return project;
    }

    private static models.project.Project setInstanceQualityAttributeStatus(models.project.Project project, List<Long> qualityPropertyIdList) throws EntityNotFoundException {
        QualityPropertyStatus persistedQualityPropertyStatus;
        List<models.project.QualityProperty> qualityProperties = qualityPropertyDAO.readAllById(qualityPropertyIdList);
        for (Instance instance : project.getQualityAttributes()) {
            for (QualityPropertyStatus qualityPropertyStatus : instance.getQualityPropertyStatus()) {
                if (qualityProperties.contains(qualityPropertyDAO.readById(qualityPropertyStatus.getQp().getId()))) {
                    persistedQualityPropertyStatus = qualityPropertyStatusDAO.readById(qualityPropertyStatus.getId());
                    persistedQualityPropertyStatus.setStatus(qualityPropertyStatus.isStatus());
                    qualityPropertyStatusDAO.update(persistedQualityPropertyStatus);
                }
            }
        }
        return projectDAO.readById(project.getId());
    }

    private static models.project.Project setProjectQualityProperties(models.project.Project project, List<Long> qualityPropertyIdList) throws EntityNotFoundException {
        List<models.project.QualityProperty> qualityPropertiesToRemove = new ArrayList<>();
        List<QualityPropertyStatus> qualityPropertyStatusesToRemove = new ArrayList<>();
        List<models.project.QualityProperty> qualityPropertyList = qualityPropertyDAO.readAllById(qualityPropertyIdList);
        models.project.Project persistedProject = projectDAO.readById(project.getId());
        //find qualityproperties and qualitypropertystatuses to remove
        for (models.project.QualityProperty persistedQualityProperty : persistedProject.getQualityProperties()) {
            if (persistedQualityProperty != null && !qualityPropertyList.contains(persistedQualityProperty)) {
                qualityPropertiesToRemove.add(persistedQualityProperty);
                qualityPropertyStatusesToRemove.addAll(findQualityPropertyStatusesToRemove(persistedProject, persistedQualityProperty));
            }
        }
        persistedProject = projectDAO.readById(project.getId());
        //add qp to Project
        for (models.project.QualityProperty qualityProperty : qualityPropertyList) {
            if (!persistedProject.getQualityProperties().contains(qualityProperty)) {
                persistedProject.addQualityProperty(qualityProperty);
                projectDAO.update(persistedProject);
                addQualityPropertyToInstances(persistedProject, qualityProperty);
            }
        }

        //remove marked qualityproperties from project
        persistedProject = projectDAO.readById(project.getId());
        for (models.project.QualityProperty qualityProperty : qualityPropertiesToRemove) {
            removeQualityPropertyFromProject(persistedProject, qualityProperty);
        }
        //remove marked qualityPropertyStatuses from instances
        QualityPropertyStatus persistedQualityPropertyStatus;
        for (QualityPropertyStatus qualityPropertyStatus : qualityPropertyStatusesToRemove) {
            persistedQualityPropertyStatus = qualityPropertyStatus.prepareToRemove();
            qualityPropertyStatusDAO.persist(persistedQualityPropertyStatus);
            qualityPropertyStatusDAO.remove(persistedQualityPropertyStatus);
        }
        return projectDAO.readById(project.getId());
    }

    private static void addQualityPropertyToInstances(models.project.Project project, QualityProperty qualityProperty) throws EntityNotFoundException {
        Instance persistedInstance;
        for (Instance instance : project.getQualityAttributes()) {
            persistedInstance = qaInstanceDAO.readById(instance.getId());
            persistedInstance.addQualityProperty(qualityProperty);
            qaInstanceDAO.persist(persistedInstance);
        }
    }

    private static void removeQualityPropertyFromProject(models.project.Project persistedProject, QualityProperty qualityProperty) throws EntityNotFoundException {
        QualityProperty persistedQualityProperty = qualityPropertyDAO.readById(qualityProperty.getId());
        persistedQualityProperty.getUsedByProject().remove(persistedProject);
        persistedProject.getQualityProperties().remove(persistedQualityProperty);
        projectDAO.persist(persistedProject);
    }

    private static List<QualityPropertyStatus> findQualityPropertyStatusesToRemove(models.project.Project project, QualityProperty qualityProperty) throws EntityNotFoundException {
        List<QualityPropertyStatus> qualityPropertyStatusToRemove = new ArrayList<>();
        models.project.Project persistedProject = projectDAO.readById(project.getId());
        for (Instance instance : persistedProject.getQualityAttributes()) {
            for (QualityPropertyStatus qualityPropertyStatus : instance.getQualityPropertyStatus()) {
                if (qualityPropertyStatus.getQp() != null && qualityProperty != null && qualityPropertyStatus.getQp().getId() == (qualityProperty.getId())) {
                    QualityPropertyStatus persistedQualityPropertyStatus = qualityPropertyStatusDAO.readById(qualityPropertyStatus.getId());
                    qualityPropertyStatusToRemove.add(persistedQualityPropertyStatus);
                }
            }
        }
        return qualityPropertyStatusToRemove;
    }

    public static models.project.Project createInstance(Instance updatedInstance) throws EntityNotFoundException {
//        models.project.Project project = getProject(json.findPath("id").asLong());
//        for (JsonNode qa : json.findPath("qualityAttributes")) {
//            Instance instance = createQAInstance(qa);
//            project.addQualityAttribute(instance);
//        }
//        return projectDAO.update(project);
        return null;
    }

    public static List<models.project.Project> getAllProjects() {
        return projectDAO.readAll();
    }

    public static models.project.Project getProject(Long id) throws EntityNotFoundException {
        return projectDAO.readById(id);
    }

    public static models.project.Project updateProject(models.project.Project project, List<Long> qualityPropertyList) throws EntityNotFoundException {
        models.project.Project persistedProject = projectDAO.readById(project.getId());
        setProjectParameters(persistedProject);

        setInstanceQualityAttributeStatus(project, qualityPropertyList);
        setProjectQualityProperties(persistedProject, qualityPropertyList);
        return projectDAO.readById(persistedProject.getId());
    }

    public static Instance updateInstance(Instance updatedInstance) throws EntityNotFoundException, MissingParameterException {
        if (updatedInstance != null && updatedInstance.getId() != null && validate(updatedInstance.getDescription())) {
            Instance persistedInstance = qaInstanceDAO.readById(updatedInstance.getId());
            persistedInstance.setDescription(updatedInstance.getDescription());
            List<Val> valueList = new ArrayList<>();
            Val persistedValue;
            for (Val val : updatedInstance.getValues()) {
                if (val.getId() != 0) {
                    persistedValue = valDAO.readById(val.getId());
                    persistedValue.setValue(val.getValue());
                    persistedValue.setVarIndex(val.getVarIndex());
                    valueList.add(persistedValue);
                } else {
                    val.setId(null);
                    valueList.add(val);
                }
            }
            persistedInstance.getValues().clear();
            persistedInstance.addValues(valueList);
            return qaInstanceDAO.persist(persistedInstance);
        } else {
            throw new MissingParameterException("Please provide all required Parameters!");
        }
    }

    public static void deleteProject(Long id) throws EntityNotFoundException {
        models.project.Project project = projectDAO.readById(id);
        project.removeQualityProperties();
        projectDAO.remove(projectDAO.update(project));
    }

    public static void deleteInstance(Long id) throws EntityNotFoundException {
        qaInstanceDAO.remove(qaInstanceDAO.readById(id));
    }

    private static models.project.Project setProjectParameters(models.project.Project updatedProject) throws EntityNotFoundException {
        models.project.Project persistedProject;
        if (updatedProject.getId() != null) {
            persistedProject = projectDAO.readById(updatedProject.getId());
        } else {
            persistedProject = updatedProject;
        }
        persistedProject.setName(updatedProject.getName());
        persistedProject.setJiraKey(updatedProject.getJiraKey());
        persistedProject.setProjectCustomer(customerDAO.readById(updatedProject.getProjectCustomer().getId()));
        return projectDAO.persist(persistedProject);
    }
}
