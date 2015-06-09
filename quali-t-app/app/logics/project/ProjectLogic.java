package logics.project;

import com.google.inject.Inject;
import dao.interfaces.JIRAConnectionDAO;
import dao.models.*;
import exceptions.CouldNotConvertException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.interfaces.projectExport.models.ModelConverter;
import logics.interfaces.projectExport.repositories.PdfRepo;
import logics.interfaces.projectExport.repositories.XmlRepo;
import models.project.Project;
import models.project.QualityProperty;
import models.project.nfritem.Instance;
import models.project.nfritem.QualityPropertyStatus;
import models.project.nfritem.Val;
import models.template.CatalogQA;
import org.apache.fop.apps.FOPException;
import play.Play;
import play.db.jpa.JPA;
import util.Helper;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by corina on 08.04.2015.
 */
public class ProjectLogic {
    @Inject
    private ProjectDAO projectDAO;
    @Inject
    private ProjectInitiatorDAO projectInitiatorDAO;
    @Inject
    private QualityPropertyDAO qualityPropertyDAO;
    @Inject
    private CatalogQADAO catalogQADAO;
    @Inject
    private QAInstanceDAO qaInstanceDAO;
    @Inject
    private JIRAConnectionDAO jiraConnectionDAO;
    @Inject
    private ValDAO valDAO;
    @Inject
    private QualityPropertyStatusDAO qualityPropertyStatusDAO;
    @Inject
    private QAVarDAO qaVarDAO;
    @Inject
    private Helper helper;
    @Inject
    private PdfRepo pdfRepo;
    @Inject
    private XmlRepo xmlRepo;
    @Inject
    private ModelConverter modelConverter;


    public models.project.Project createProject(models.project.Project project, List<Long> qualityAttributeIdList, List<Long> qualityPropertyIdList) throws EntityNotFoundException, MissingParameterException {
        if (project != null && qualityAttributeIdList != null && qualityPropertyIdList != null) {
            project.setId(null);
            //create qa instances
            CatalogQA catalogQA;
            addQualityAttributesToProject(qualityAttributeIdList, project);
            // set project parameters
            setProjectParameters(project);
            setProjectQualityProperties(project, qualityPropertyIdList);
            projectDAO.persist(project);
            return project;
        }
        throw new MissingParameterException("Please provide all required Parameters!");
    }

    public Instance cloneInstance(Long id) throws MissingParameterException, EntityNotFoundException {
        if (id != null) {
            Instance originalInstance = qaInstanceDAO.readById(id);
            Instance newInstance = originalInstance.copyInstance();
            return qaInstanceDAO.persist(newInstance);
        }
        throw new MissingParameterException("Please provide a valid ID!");
    }

    public models.project.Project createInstance(Project project, List<Long> qualityAttributeIdList) throws EntityNotFoundException, MissingParameterException {
        if (project != null && qualityAttributeIdList != null) {
            Project persistedProject = projectDAO.readById(project.getId());
            for (Instance instance : project.getQualityAttributes()) {
                persistedProject.addQualityAttribute(instance);
            }
            addQualityAttributesToProject(qualityAttributeIdList, persistedProject);
            return projectDAO.update(persistedProject);
        }
        throw new MissingParameterException("Please provide all required Parameters!");
    }

    private void addQualityAttributesToProject(List<Long> qualityAttributeIdList, Project persistedProject) throws EntityNotFoundException {
        CatalogQA catalogQA;
        for (Long id : qualityAttributeIdList) {
            catalogQA = catalogQADAO.readById(id);
            persistedProject.addQualityAttribute(new Instance(catalogQA.getQa().getDescription(), catalogQA));
        }
    }

    public List<models.project.Project> getAllProjects() {
        return projectDAO.readAll();
    }

    public models.project.Project getProject(Long id) throws EntityNotFoundException {
        return projectDAO.readById(id);
    }

    public models.project.Project updateProject(models.project.Project project, List<Long> qualityPropertyList) throws EntityNotFoundException, MissingParameterException {
        if (project != null && project.getId() != null && qualityPropertyList != null) {
            projectDAO.update(setProjectParameters(project));
            setInstanceQualityAttributeStatus(project, qualityPropertyList);
            setProjectQualityProperties(project, qualityPropertyList);
            return projectDAO.readById(project.getId());
        }
        throw new MissingParameterException("Please provide all required Parameters!");
    }

    public Instance updateInstance(Instance updatedInstance) throws EntityNotFoundException, MissingParameterException {
        if (updatedInstance != null && updatedInstance.getId() != null && helper.validate(updatedInstance.getDescription())) {
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
            // update the instance to database
            Instance toReturn = qaInstanceDAO.persist(persistedInstance);
            // TODO emre: discuss with corina wether to check statistics before or after saving the new values
            JPA.em().flush();
            // and do after that some statistics
            qaVarDAO.updateStatistic(persistedInstance.getTemplate());
            return toReturn;
        }
        throw new MissingParameterException("Please provide all required Parameters!");

    }

    public void deleteProject(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            models.project.Project project = projectDAO.readById(id);
            project.removeQualityProperties();
            project.removeFavoritedBy();
            projectDAO.remove(projectDAO.update(project));
        } else {
            throw new MissingParameterException("Please provide all required Parameters!");
        }
    }

    public void deleteInstance(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            qaInstanceDAO.remove(qaInstanceDAO.readById(id));
        } else {
            throw new MissingParameterException("Please provide all required Parameters!");
        }
    }

    private models.project.Project setProjectParameters(models.project.Project updatedProject) throws EntityNotFoundException, MissingParameterException {
        if (helper.validate(updatedProject.getName()) && updatedProject.getProjectInitiator() != null) {
            boolean jiraConnectionChanged = false;
            models.project.Project persistedProject;
            if (updatedProject.getId() != null) {
                persistedProject = projectDAO.readById(updatedProject.getId());
            } else {
                persistedProject = updatedProject;
            }
            persistedProject.setName(updatedProject.getName());
            if (persistedProject.getJiraKey() == null && updatedProject.getJiraKey() == null) {
            } else if (persistedProject.getJiraKey() == null || updatedProject.getJiraKey() == null) {
                jiraConnectionChanged = true;
            } else if (!persistedProject.getJiraKey().equals(updatedProject.getJiraKey())) {
                jiraConnectionChanged = true;
            }
            persistedProject.setProjectInitiator(projectInitiatorDAO.readById(updatedProject.getProjectInitiator().getId()));
            if (updatedProject.getJiraConnection() != null && updatedProject.getJiraConnection().getId() != null && updatedProject.getJiraConnection().getId() != 0) {
                updatedProject.setJiraConnection(jiraConnectionDAO.readById(updatedProject.getJiraConnection().getId()));
                if (updatedProject.getJiraConnection() != persistedProject.getJiraConnection()) {
                    jiraConnectionChanged = true;
                }
                persistedProject.setJiraConnection(updatedProject.getJiraConnection());
            } else {
                if (persistedProject.getJiraConnection() != null) {
                    jiraConnectionChanged = true;
                }
                persistedProject.setJiraConnection(null);
            }
            if (jiraConnectionChanged) {
                resetQaInstanceJiraParameter(persistedProject);
                persistedProject.setJiraKey(updatedProject.getJiraKey());
            }

            return projectDAO.persist(persistedProject);
        }
        throw new MissingParameterException("Please provide all required Parameters!");
    }

    private void resetQaInstanceJiraParameter(Project persistedProject) {
        for (Instance instance : persistedProject.getQualityAttributes()) {
            instance.setJiraKey(null);
            instance.setJiraDirectURL(null);
        }
    }

    private models.project.Project setProjectQualityProperties(models.project.Project project, List<Long> qualityPropertyIdList) throws EntityNotFoundException {
        List<models.project.QualityProperty> qualityPropertiesToRemove = new ArrayList<>();
        List<QualityPropertyStatus> qualityPropertyStatusesToRemove = new ArrayList<>();
        List<models.project.QualityProperty> qualityPropertyList = qualityPropertyDAO.readAllById(qualityPropertyIdList);
        models.project.Project persistedProject = projectDAO.readById(project.getId());
        //find qualityProperties and qualityPropertyStatuses to remove
        for (models.project.QualityProperty persistedQualityProperty : persistedProject.getQualityProperties()) {
            if (persistedQualityProperty != null && !qualityPropertyList.contains(persistedQualityProperty)) {
                qualityPropertiesToRemove.add(persistedQualityProperty);
                qualityPropertyStatusesToRemove.addAll(findQualityPropertyStatusesToRemove(persistedProject, persistedQualityProperty));
            }
        }
        //add qp to Project
        for (models.project.QualityProperty qualityProperty : qualityPropertyList) {
            if (!persistedProject.getQualityProperties().contains(qualityProperty)) {
                persistedProject.addQualityProperty(qualityProperty);
                projectDAO.update(persistedProject);
                addQualityPropertyToInstances(persistedProject, qualityProperty);
            }
        }
        //remove marked qualityProperties from project
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

    private void addQualityPropertyToInstances(models.project.Project project, QualityProperty qualityProperty) throws EntityNotFoundException {
        Instance persistedInstance;
        for (Instance instance : project.getQualityAttributes()) {
            persistedInstance = qaInstanceDAO.readById(instance.getId());
            persistedInstance.addQualityProperty(qualityProperty);
            qaInstanceDAO.persist(persistedInstance);
        }
    }

    private void removeQualityPropertyFromProject(models.project.Project persistedProject, QualityProperty qualityProperty) throws EntityNotFoundException {
        QualityProperty persistedQualityProperty = qualityPropertyDAO.readById(qualityProperty.getId());
        persistedQualityProperty.getUsedByProject().remove(persistedProject);
        persistedProject.getQualityProperties().remove(persistedQualityProperty);
        projectDAO.persist(persistedProject);
    }

    private List<QualityPropertyStatus> findQualityPropertyStatusesToRemove(models.project.Project project, QualityProperty qualityProperty) throws EntityNotFoundException {
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


    private models.project.Project setInstanceQualityAttributeStatus(models.project.Project project, List<Long> qualityPropertyIdList) throws EntityNotFoundException {
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

    public ByteArrayOutputStream exportToPdf(Long id) throws EntityNotFoundException, MissingParameterException, CouldNotConvertException {
        if (id != null) {
            Project project = projectDAO.readById(id);
            try {
                ByteArrayOutputStream xml = xmlRepo.projectToXML(modelConverter.convertProject(project));

                InputStream inputStream = Play.application().resourceAsStream("project_export.xsl");
                return pdfRepo.createPdf(new ByteArrayInputStream(xml.toByteArray()), inputStream);
            } catch (JAXBException | FOPException | TransformerException e) {
                throw new CouldNotConvertException("Could not Convert due to internal server error");
            }
        }
        throw new MissingParameterException("Please provide a valid ID!");
    }


    public ByteArrayOutputStream exportToXML(Long id) throws EntityNotFoundException, CouldNotConvertException, MissingParameterException {
        if (id != null) {
            Project project = projectDAO.readById(id);
            try {
                return xmlRepo.projectToXML(modelConverter.convertProject(project));
            } catch (JAXBException e) {
                throw new CouldNotConvertException("Could not Convert due to internal server error");
            }
        }
        throw new MissingParameterException("Please provide a valid ID!");
    }

    public List<Instance> getQualityAttributes(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            Project project = projectDAO.readById(id);
            return new ArrayList<>(project.getQualityAttributes());
        }
        throw new MissingParameterException("Please provide a valid ID!");
    }
}