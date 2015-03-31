package models;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.annotation.Nullable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by corina on 30.03.2015.
 */


@Entity
@Table(name = "project")
@Nullable
public class Project extends AbstractEntity {

    public Project() {
    }
    public Project(String name) {
        this.name = name;
    }
    public Project(String name, Customer projectCustomer, Set<QualityAttribute> q_set){
        this.name = name;
        this.projectCustomer = projectCustomer;
        this.qualityAttributes = q_set;
    }

    @ManyToOne(optional=true)
    @JsonBackReference
    private Customer projectCustomer;
    private String name;
    @ManyToMany(mappedBy="usedByProject")
    @JsonManagedReference
    private Set<QualityAttribute> qualityAttributes = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer getProjectCustomer() {
        return projectCustomer;
    }

    public void setProjectCustomer(Customer projectCustomer) {
        this.projectCustomer = projectCustomer;
    }


    public Set<QualityAttribute> getQualityAttributes() {
        return qualityAttributes;
    }

    public void setQualityAttributes(Set<QualityAttribute> qualityAttributes) {
        this.qualityAttributes = qualityAttributes;
    }

    public void addQualityAttribute(QualityAttribute q) {
        this.qualityAttributes.add(q);
    }
}

