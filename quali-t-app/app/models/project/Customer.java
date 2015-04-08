package models.project;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;
import java.util.ArrayList;


/**
 * Created by corina on 30.03.2015.
 */

@Entity
@Table(name = "customer")
@Nullable
public class Customer extends AbstractEntity {
    public Customer() {
    }

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
    }


    private String name;

    private String address;

    @OneToMany(mappedBy = "projectCustomer")
    @JsonManagedReference
    private Collection<Project> projects = new ArrayList<Project>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Collection<Project> getProjects() {
        return projects;
    }

    public void addProject(Project project) {
        this.projects.add(project);
    }


}
