package logics.interfaces.projectExport.models;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by corina on 22.05.2015.
 */
@XmlRootElement(name = "project")
@XmlType(propOrder = {"name", "jiraKey", "projectCustomer", "qualityAttributes"})
public class Project {

    private Customer projectCustomer;
    private String name;
    private Set<Instance> qualityAttributes = new HashSet<>();
    private String jiraKey;

    public Project() {
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public Customer getProjectCustomer() {
        return projectCustomer;
    }

    public void setProjectCustomer(Customer projectCustomer) {
        this.projectCustomer = projectCustomer;
    }

    @XmlElementWrapper(name = "qualityAttributes")
    @XmlElement(name = "qualityAttribute")
    public Set<Instance> getQualityAttributes() {
        return qualityAttributes;
    }

    public void setQualityAttributes(Set<Instance> qualityAttributes) {
        this.qualityAttributes = qualityAttributes;
    }

    @XmlAttribute
    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public void addQualityAttribute(Instance qa) {
        this.qualityAttributes.add(qa);
        qa.setProject(this);
    }

    public void addQualityAttributes(List<Instance> qas) {
        for (Instance qa : qas) {
            this.addQualityAttribute(qa);
        }
    }
}
