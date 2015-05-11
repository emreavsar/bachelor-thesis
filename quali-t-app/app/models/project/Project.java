package models.project;


import javax.persistence.*;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import models.AbstractEntity;
import models.authentication.User;
import models.project.nfritem.Instance;
import models.template.Catalog;
import models.template.CatalogQA;
import models.template.QA;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by corina on 30.03.2015.
 */


@Entity
@Table(name = "project")
@Nullable
public class Project extends AbstractEntity {

    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "userProjects")
    private Customer projectCustomer;
    private String name;
    @ManyToMany(mappedBy = "usedByProject", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JsonManagedReference(value = "qualityProperty")
    @JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@qaProperties")
    private Set<QualityProperty> qualityProperties = new HashSet<>();
    @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonManagedReference(value = "qualityAttributes")
    private Set<Instance> qualityAttributes = new HashSet<>();

    public Project() {
    }

    public Project(String name, Customer projectCustomer, List<QualityProperty> qps) {
        this.name = name;
        this.projectCustomer = projectCustomer;
        this.addQualityProperties(qps);
    }

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

    public Set<QualityProperty> getQualityProperties() {
        return qualityProperties;
    }

    public void setQualityProperties(Set<QualityProperty> qualityProperties) {
        this.qualityProperties = qualityProperties;
    }

    public void addQualityProperty(QualityProperty qp) {
        this.qualityProperties.add(qp);
    }

    public void addQualityProperties(List<QualityProperty> qps) {
        for (QualityProperty qp : qps) {
            this.addQualityProperty(qp);
            qp.addUsedByProject(this);
        }
    }

    public Set<Instance> getQualityAttributes() {
        return qualityAttributes;
    }

    public void setQualityAttributes(Set<Instance> qualityAttributes) {
        this.qualityAttributes = qualityAttributes;
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

