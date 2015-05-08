package models.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;
import org.hibernate.annotations.CascadeType;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by corina on 30.03.2015.
 */

@Entity
@Table(name = "customer")
@Nullable
public class Customer extends AbstractEntity {
    private String name;
    private String address;
    @OneToMany(mappedBy = "projectCustomer", cascade = javax.persistence.CascadeType.REMOVE)
//    @JsonManagedReference(value = "userProjects")
    @JsonIgnore
    private List<Project> projects = new ArrayList<>();

    public Customer() {
    }

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
    }

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

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", projects=" + projects +
                ", id'" + getId() +
                '}';
    }
}
