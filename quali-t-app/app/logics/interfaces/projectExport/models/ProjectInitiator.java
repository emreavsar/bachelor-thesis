package logics.interfaces.projectExport.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "projectInitiator")
@XmlType(propOrder = {"name", "address"})
public class ProjectInitiator {

    private String name;
    private String address;

    public ProjectInitiator() {
    }

    public ProjectInitiator(String name, String address) {
        this.name = name;
        this.address = address;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ProjectInitiator{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
