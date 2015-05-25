package logics.interfaces.projectExport.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by corina on 03.04.2015.
 */
@XmlRootElement(name = "instance")
@XmlType(propOrder = {"description"})
public class Instance {

    private String description;
    private Project project;

    public Instance() {
    }

    @XmlAttribute
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
