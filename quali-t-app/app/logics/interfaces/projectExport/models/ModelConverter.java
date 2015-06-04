package logics.interfaces.projectExport.models;

import com.google.inject.Inject;
import exceptions.EntityNotFoundException;
import util.Helper;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by corina on 25.05.2015.
 */
public class ModelConverter {
    @Inject
    private Helper helper;

    public Project convertProject(models.project.Project project) throws EntityNotFoundException {
        Project convertedProject = new Project();
        convertedProject.setName(project.getName());
        convertedProject.setJiraKey(project.getJiraKey());
        convertedProject.setProjectCustomer(convertCustomer(project.getProjectCustomer()));
        convertedProject.setQualityAttributes(convertInstance(project.getQualityAttributes()));
        return convertedProject;
    }

    private Customer convertCustomer(models.project.Customer customer) {
        Customer convertCustomer = new Customer();
        convertCustomer.setAddress(customer.getAddress());
        convertCustomer.setName(customer.getName());
        return convertCustomer;
    }

    private Set<Instance> convertInstance(Set<models.project.nfritem.Instance> instanceSet) throws EntityNotFoundException {
        Set<Instance> convertedInstances = new HashSet<>();
        for (models.project.nfritem.Instance instance : instanceSet) {
            Instance tempInstance = new Instance();
            tempInstance.setDescription(helper.removeHtmlTags(helper.getDescriptionWithVars(instance)));
            convertedInstances.add(tempInstance);
        }
        return convertedInstances;
    }
}
