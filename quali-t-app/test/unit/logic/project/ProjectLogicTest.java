package unit.logic.project;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.ProjectDAO;
import dao.models.QAInstanceDAO;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.project.ProjectLogic;
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
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by corina on 18.05.2015.
 */
public class ProjectLogicTest extends AbstractDatabaseTest {
    private List<Long> qualityAttributeIdList;
    private List<Long> qualityPropertyIdList;
    private Project project;
    private Customer customer;
    private CatalogQA qa;
    private QualityProperty qualityProperty;
    private Project persistedProject;
    private ProjectLogic projectLogic;
    private ProjectDAO projectDAO;
    private Instance instance;
    private QAInstanceDAO instanceDAO;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        qa = AbstractTestDataCreator.createCatalogQA(new QA("description"), new Catalog("name", "image", "description"));
        instance = AbstractTestDataCreator.createInstance("instance", null, null);
        qualityAttributeIdList = new ArrayList<>();
        qualityAttributeIdList.add(qa.getId());
        qualityPropertyIdList = new ArrayList<>();
        qualityProperty = AbstractTestDataCreator.createQualityProperty("D", "Description");
        qualityPropertyIdList.add(qualityProperty.getId());
        customer = AbstractTestDataCreator.createCustomer("name", "address");
        project = new Project();
        project.setProjectCustomer(customer);
        project.setName("project name");
        project.setJiraKey("jira key");
        projectLogic = getInjector().getInstance(ProjectLogic.class);
        projectDAO = getInjector().getInstance(ProjectDAO.class);
        instanceDAO = getInjector().getInstance(QAInstanceDAO.class);
    }

    //createProjectTest
    @Test
    public void createValidProject() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE

        // ACT
        Project newProject = projectLogic.createProject(project, qualityAttributeIdList, qualityPropertyIdList);
        // ASSERT
        assertThat(newProject.getId()).isNotNull();
        assertThat(newProject.getJiraKey()).isEqualTo("jira key");
        assertThat(newProject.getName()).isEqualTo("project name");
        assertThat(newProject.getProjectCustomer()).isEqualTo(customer);
        assertThat(newProject.getQualityProperties().size()).isEqualTo(1);
        assertThat(newProject.getQualityProperties().contains(qualityProperty));
        assertThat(newProject.getQualityAttributes().size()).isEqualTo(1);
        assertThat(newProject.getQualityAttributes().contains(qa));
    }

    @Test
    public void createValidProjectWithNewQAs() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        Instance qa = new Instance("new project qa");
        project.addQualityAttribute(qa);
        // ACT
        Project newProject = projectLogic.createProject(project, qualityAttributeIdList, qualityPropertyIdList);
        // ASSERT
        assertThat(newProject.getQualityAttributes().contains(qa));
    }

    @Test(expected = MissingParameterException.class)
    public void createNullProjectWithQAValues() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        Project newProject = projectLogic.createProject(null, qualityAttributeIdList, qualityPropertyIdList);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void createProjectWithEmptyName() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        project.setName("");
        // ACT
        Project newProject = projectLogic.createProject(project, qualityAttributeIdList, qualityPropertyIdList);
        // ASSERT
    }

    @Test
    public void createProjectWithEmptyQualityAttributeList() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        qualityAttributeIdList = new ArrayList<>();
        // ACT
        Project newProject = projectLogic.createProject(project, qualityAttributeIdList, qualityPropertyIdList);
        // ASSERT
        assertThat(newProject.getId()).isNotNull();
        assertThat(newProject.getJiraKey()).isEqualTo("jira key");
        assertThat(newProject.getName()).isEqualTo("project name");
        assertThat(newProject.getProjectCustomer()).isEqualTo(customer);
        assertThat(newProject.getQualityProperties().size()).isEqualTo(1);
        assertThat(newProject.getQualityProperties().contains(qualityProperty));
        assertThat(newProject.getQualityAttributes().size()).isEqualTo(0);
    }

    @Test(expected = MissingParameterException.class)
    public void createProjectWithNullQualityAttributeList() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        Project newProject = projectLogic.createProject(project, null, qualityPropertyIdList);
        // ASSERT
    }

    @Test
    public void createProjectWithEmptyQualityPropertyList() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        qualityPropertyIdList = new ArrayList<>();
        // ACT
        Project newProject = projectLogic.createProject(project, qualityAttributeIdList, qualityPropertyIdList);
        // ASSERT
        assertThat(newProject.getId()).isNotNull();
        assertThat(newProject.getJiraKey()).isEqualTo("jira key");
        assertThat(newProject.getName()).isEqualTo("project name");
        assertThat(newProject.getProjectCustomer()).isEqualTo(customer);
        assertThat(newProject.getQualityProperties().size()).isEqualTo(0);
        assertThat(newProject.getQualityAttributes().size()).isEqualTo(1);
        assertThat(newProject.getQualityAttributes().contains(qa));
    }

    @Test(expected = MissingParameterException.class)
    public void createProjectWithNullQualityPropertyList() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        Project newProject = projectLogic.createProject(project, qualityAttributeIdList, null);
        // ASSERT
    }

    @Test
    public void cloneValidSimpleInstance() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        persistedProject = AbstractTestDataCreator.createProject(project);
        Instance instance = AbstractTestDataCreator.createInstance("instance to copy", persistedProject, qa);
        // ACT
        Instance newInstance = projectLogic.cloneInstance(instance.getId());
        // ASSERT
        assertThat(newInstance.getId()).isNotEqualTo(instance.getId());
        assertThat(newInstance.getId()).isNotNull();
        assertThat(newInstance.getDescription()).isEqualTo(instance.getDescription());
        assertThat(newInstance.getProject()).isEqualTo(persistedProject);
        assertThat(newInstance.getTemplate()).isEqualTo(qa);
    }

    @Test
    public void cloneValidInstanceWithValues() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        persistedProject = AbstractTestDataCreator.createProject(project);
        Instance instance = AbstractTestDataCreator.createInstance("instance to copy", persistedProject, qa);
        Val value = new Val(1, "value");
        instance = AbstractTestDataCreator.addValueToInstance(instance, value);
        // ACT
        Instance newInstance = projectLogic.cloneInstance(instance.getId());
        // ASSERT
        assertThat(newInstance.getId()).isNotEqualTo(instance.getId());
        assertThat(newInstance.getId()).isNotNull();
        assertThat(newInstance.getDescription()).isEqualTo(instance.getDescription());
        assertThat(newInstance.getProject()).isEqualTo(persistedProject);
        assertThat(newInstance.getTemplate()).isEqualTo(qa);
        assertThat(newInstance.getValues().size()).isEqualTo(instance.getValues().size());
        int valSize = instance.getValues().size();
        for (Val originalVal : instance.getValues()) {
            for (Val newVal : newInstance.getValues()) {
                if (originalVal.getValue().equals(newVal.getValue()) && originalVal.getVarIndex() == newVal.getVarIndex()) {
                    valSize--;
                }
            }
            assertThat(valSize).isEqualTo(0);
        }
    }

    @Test(expected = MissingParameterException.class)
    public void cloneInstanceNullId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        projectLogic.cloneInstance(null);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void cloneInstanceInvalidId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        projectLogic.cloneInstance(new Long(999999));
        // ASSERT
    }

    @Test
    public void updateValidProjectGeneralParameters() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        persistedProject = AbstractTestDataCreator.createProject(project);
        Project projectToUpdate = new Project();
        JIRAConnection jiraConnection = AbstractTestDataCreator.createJiraConnection("new host", "new user", "new password");
        Customer newCustomer = AbstractTestDataCreator.createCustomer("new name", "new address");
        projectToUpdate.setJiraConnection(jiraConnection);
        projectToUpdate.setProjectCustomer(newCustomer);
        projectToUpdate.setName("new name");
        projectToUpdate.setJiraKey("new jira key");
        projectToUpdate.setId(persistedProject.getId());
        // ACT
        Project updatedProject = projectLogic.updateProject(projectToUpdate, qualityPropertyIdList);
        // ASSERT
        assertThat(updatedProject.getId()).isEqualTo(persistedProject.getId());
        assertThat(updatedProject.getJiraKey()).isEqualTo("new jira key");
        assertThat(updatedProject.getName()).isEqualTo("new name");
        assertThat(updatedProject.getProjectCustomer()).isEqualTo(newCustomer);
        assertThat(updatedProject.getQualityProperties().size()).isEqualTo(persistedProject.getQualityProperties().size());
        assertThat(updatedProject.getQualityProperties().contains(qualityProperty));
        assertThat(updatedProject.getQualityAttributes().size()).isEqualTo(persistedProject.getQualityAttributes().size());
        assertThat(updatedProject.getQualityAttributes().contains(qa));
        assertThat(updatedProject.getJiraConnection()).isEqualTo(jiraConnection);
    }

    @Test
    public void updateValidProjectAddQualityProperties() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        project.addQualityAttribute(new Instance("instance"));
        persistedProject = AbstractTestDataCreator.createProject(project);
        Project projectToUpdate = new Project();
        projectToUpdate.setName("name");
        projectToUpdate.setProjectCustomer(customer);
        QualityProperty newQualityProperty = AbstractTestDataCreator.createQualityProperty("S", "Specific");
        qualityPropertyIdList.add(newQualityProperty.getId());
        projectToUpdate.setId(persistedProject.getId());
        // ACT
        Project updatedProject = projectLogic.updateProject(projectToUpdate, qualityPropertyIdList);
        // ASSERT
        assertThat(updatedProject.getId()).isEqualTo(persistedProject.getId());
        assertThat(updatedProject.getQualityProperties().size()).isEqualTo(2);
        assertThat(updatedProject.getQualityProperties().contains(qualityProperty));
        assertThat(updatedProject.getQualityProperties().contains(newQualityProperty));
        int qualityPropertySize = updatedProject.getQualityProperties().size();
        for (Instance instance : updatedProject.getQualityAttributes()) {
            for (QualityPropertyStatus qualityPropertyStatus : instance.getQualityPropertyStatus()) {
                if (qualityPropertyIdList.contains(qualityPropertyStatus.getQp().getId())) {
                    qualityPropertySize--;
                }
            }
            assertThat(qualityPropertySize).isEqualTo(0);
        }
    }

    @Test
    public void updateValidProjectRemoveQualityProperties() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        persistedProject = AbstractTestDataCreator.createProject(project);
        Project projectToUpdate = new Project();
        projectToUpdate.setName("name");
        projectToUpdate.setProjectCustomer(customer);
        qualityPropertyIdList = new ArrayList<>();
        projectToUpdate.setId(persistedProject.getId());
        // ACT
        Project updatedProject = projectLogic.updateProject(projectToUpdate, qualityPropertyIdList);
        // ASSERT
        assertThat(updatedProject.getId()).isEqualTo(persistedProject.getId());
        assertThat(updatedProject.getQualityProperties().size()).isEqualTo(0);
        for (Instance instance : updatedProject.getQualityAttributes()) {
            assertThat(instance.getQualityPropertyStatus().size()).isEqualTo(0);
        }
    }
//
//    @Test
//    public void updateValidProjectChangeQualityPropertyStatus() throws MissingParameterException, EntityNotFoundException {
//        // ARRANGE
//        Instance persistedInstance = AbstractTestDataCreator.createInstance("instance", project,qa);
//        persistedInstance = AbstractTestDataCreator.addQualityPropertyStatusToInstance(persistedInstance, qualityProperty, true);
//        project.addQualityAttribute(persistedInstance);
//        persistedProject = AbstractTestDataCreator.createProject(project);
//        Project projectToUpdate = new Project();
//        projectToUpdate.setProjectCustomer(customer);
//        Instance instanceToUpdate = new Instance();
//        instanceToUpdate.setId(persistedInstance.getId());
//        instanceToUpdate.addQualityProperty(qualityProperty);
//        for (QualityPropertyStatus qualityPropertyStatusToUpdate : instanceToUpdate.getQualityPropertyStatus()){
//            Logger.debug(" status find");
//            qualityPropertyStatusToUpdate.setStatus(true);
//            //        for (Instance persistedInstance : persistedProject.getQualityAttributes()) {
//            for (QualityPropertyStatus persistedQualityPropertyStatus : persistedInstance.getQualityPropertyStatus()) {
//                Logger.info("id persistiert" + persistedQualityPropertyStatus.getId());
//                qualityPropertyStatusToUpdate.setId(persistedQualityPropertyStatus.getId());
//            }
//        }
//
//        projectToUpdate.addQualityAttribute(instanceToUpdate);
//        // ACT
//        Project updatedProject = projectLogic.updateProject(projectToUpdate, qualityPropertyIdList);
//        // ASSERT
//        for (Instance updatedInstance : updatedProject.getQualityAttributes()) {
//            for (QualityPropertyStatus updatedQualityPropertyStatus : updatedInstance.getQualityPropertyStatus()) {
//                assertThat(updatedQualityPropertyStatus.isStatus()).isTrue();
//            }
//        }
//    }

    @Test(expected = MissingParameterException.class)
    public void updateNullProject() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        projectLogic.updateProject(null, qualityPropertyIdList);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void updateProjectEmptyName() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        persistedProject = AbstractTestDataCreator.createProject(project);
        Project projectToUpdate = new Project();
        projectToUpdate.setProjectCustomer(customer);
        projectToUpdate.setId(persistedProject.getId());
        projectToUpdate.setName("");
        // ACT
        projectLogic.updateProject(projectToUpdate, qualityPropertyIdList);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void updateProjectNullCustomer() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        persistedProject = AbstractTestDataCreator.createProject(project);
        Project projectToUpdate = new Project();
        projectToUpdate.setProjectCustomer(customer);
        projectToUpdate.setId(persistedProject.getId());
        projectToUpdate.setProjectCustomer(null);
        // ACT
        projectLogic.updateProject(projectToUpdate, qualityPropertyIdList);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateProjectInvalidId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        persistedProject = AbstractTestDataCreator.createProject(project);
        Project projectToUpdate = new Project();
        projectToUpdate.setName("name");
        projectToUpdate.setProjectCustomer(customer);
        projectToUpdate.setId(new Long(99999));
        // ACT
        projectLogic.updateProject(projectToUpdate, qualityPropertyIdList);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void updateProjectNullId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        persistedProject = AbstractTestDataCreator.createProject(project);
        Project projectToUpdate = new Project();
        projectToUpdate.setName("name");
        projectToUpdate.setProjectCustomer(customer);
        projectToUpdate.setId(null);
        projectToUpdate.setProjectCustomer(null);
        // ACT
        projectLogic.updateProject(projectToUpdate, qualityPropertyIdList);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void updateProjectInvalidCustomerId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        persistedProject = AbstractTestDataCreator.createProject(project);
        Project projectToUpdate = new Project();
        projectToUpdate.setName("name");
        projectToUpdate.setProjectCustomer(customer);
        projectToUpdate.setId(null);
        projectToUpdate.setProjectCustomer(null);
        // ACT
        projectLogic.updateProject(projectToUpdate, qualityPropertyIdList);
        // ASSERT
    }

    @Test
    public void updateValidInstance() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        Instance instanceToUpdate = new Instance("new description");
        instanceToUpdate.setId(instance.getId());
        Val newValue = new Val();
        instance.addValue(new Val(1, "value1"));
        AbstractTestDataCreator.persistAndFlush(instance);
        for (Val value : instance.getValues()) {
            newValue.setValue("new value");
            newValue.setVarIndex(value.getVarIndex());
            newValue.setId(value.getId());
        }
        instanceToUpdate.addValue(newValue);
        // ACT
        Instance updatedInstance = projectLogic.updateInstance(instanceToUpdate);
        // ASSERT
        assertThat(updatedInstance.getDescription()).isEqualTo("new description");
        assertThat(updatedInstance.getId()).isEqualTo(instance.getId());
        for (Val value : updatedInstance.getValues()) {
            assertThat(value.getValue()).isEqualTo("new value");
            assertThat(value.getVarIndex()).isEqualTo(1);
        }
        assertThat(updatedInstance.getValues().size()).isEqualTo(1);
    }

    @Test(expected = MissingParameterException.class)
    public void updateInstanceNullId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        instance.setId(null);
        // ACT
        projectLogic.updateInstance(instance);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void updateNullInstance() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        projectLogic.updateInstance(null);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateInstanceInvalidId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        instance.setId(new Long(99999));
        // ACT
        projectLogic.updateInstance(instance);
        // ASSERT
    }

    @Test
    public void deleteValidProject() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        persistedProject = AbstractTestDataCreator.createProject(project);
        // ACT
        projectLogic.deleteProject(persistedProject.getId());
        // ASSERT
        assertThat(projectDAO.readAll().contains(persistedProject)).isFalse();
    }

    @Test(expected = MissingParameterException.class)
    public void deleteProjectNullId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        projectLogic.deleteProject(null);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteProjectInvalidId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        projectLogic.deleteProject(new Long(99999));
        // ASSERT
    }

    @Test
    public void deleteValidInstance() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        Instance persistedInstance = AbstractTestDataCreator.createInstance("description", persistedProject, qa);
        // ACT
        projectLogic.deleteInstance(persistedInstance.getId());
        // ASSERT
        assertThat(instanceDAO.readAll().contains(persistedInstance)).isFalse();
    }

    @Test(expected = MissingParameterException.class)
    public void deleteInstanceNullId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        projectLogic.deleteInstance(null);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteInstanceInvalidId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        projectLogic.deleteInstance(new Long(99999));
        // ASSERT
    }




}
