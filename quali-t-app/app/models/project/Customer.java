package models.project;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    @OneToMany(mappedBy = "projectCustomer", cascade = CascadeType.REMOVE)
    @JsonBackReference(value = "userProjects")
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
