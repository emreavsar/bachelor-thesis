package unit.logic.project;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.project.Customer;
import models.project.Project;
import models.project.QualityProperty;
import models.project.nfritem.Instance;
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

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        qa = AbstractTestDataCreator.createCatalogQA(new QA("description"), new Catalog("name", "image", "description"));
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
    }

    //createProjectTest
    @Test
    public void createValidProject() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE

        // ACT
        Project newProject = logics.project.Project.createProject(project, qualityAttributeIdList, qualityPropertyIdList);
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
        Project newProject = logics.project.Project.createProject(project, qualityAttributeIdList, qualityPropertyIdList);
        // ASSERT
        assertThat(newProject.getQualityAttributes().contains(qa));
    }

    @Test(expected = MissingParameterException.class)
    public void createNullProjectWithQAValues() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        Project newProject = logics.project.Project.createProject(null, qualityAttributeIdList, qualityPropertyIdList);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void createProjectWithEmptyName() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        project.setName("");
        // ACT
        Project newProject = logics.project.Project.createProject(project, qualityAttributeIdList, qualityPropertyIdList);
        // ASSERT
    }

    @Test
    public void createProjectWithEmptyQualityAttributeList() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        qualityAttributeIdList = new ArrayList<>();
        // ACT
        Project newProject = logics.project.Project.createProject(project, qualityAttributeIdList, qualityPropertyIdList);
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
        Project newProject = logics.project.Project.createProject(project, null, qualityPropertyIdList);
        // ASSERT
    }

    @Test
    public void createProjectWithEmptyQualityPropertyList() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        qualityPropertyIdList = new ArrayList<>();
        // ACT
        Project newProject = logics.project.Project.createProject(project, qualityAttributeIdList, qualityPropertyIdList);
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
        Project newProject = logics.project.Project.createProject(project, qualityAttributeIdList, null);
        // ASSERT
    }

    @Test
    public void cloneValidSimpleInstance() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        Project persistedProject = AbstractTestDataCreator.createProject(project);
        Instance instance = AbstractTestDataCreator.createInstance("instance to copy", persistedProject, qa);
        // ACT
        Instance newInstance = logics.project.Project.cloneInstance(instance.getId());
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
        Project persistedProject = AbstractTestDataCreator.createProject(project);
        Instance instance = AbstractTestDataCreator.createInstance("instance to copy", persistedProject, qa);
        Val value = new Val(1, "value");
        instance = AbstractTestDataCreator.addValueToInstance(instance, value);
        // ACT
        Instance newInstance = logics.project.Project.cloneInstance(instance.getId());
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
        logics.project.Project.cloneInstance(null);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void cloneInstanceInvalidId() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        logics.project.Project.cloneInstance(new Long(999999));
        // ASSERT
    }

}
