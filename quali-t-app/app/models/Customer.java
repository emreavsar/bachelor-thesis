package models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Customer extends AbstractEntity{
    public Customer(){
    }
    public Customer(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy="projectCustomer")
    @JsonManagedReference
    private String name;
    private Collection<Project> projects = new ArrayList<Project>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Project> getProjects() {
        return projects;
    }

    public void addProject(Project project) {
        this.projects.add(project);
    }







}
