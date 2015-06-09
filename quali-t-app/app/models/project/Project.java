package models.project;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;
import models.Interface.JIRAConnection;
import models.authentication.User;
import models.project.nfritem.Instance;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nullable;
import javax.persistence.*;
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
    @JsonManagedReference(value = "userProjects")
    private ProjectInitiator projectInitiator;
    private String name;
    @ManyToMany(mappedBy = "usedByProject", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference(value = "qualityProperty")
    private Set<QualityProperty> qualityProperties = new HashSet<>();
    @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonManagedReference(value = "qualityAttributes")
    private Set<Instance> qualityAttributes = new HashSet<>();
    private String jiraKey;

    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference(value = "projectsJiraConnection")
    private JIRAConnection jiraConnection;

    @ManyToMany(mappedBy = "favorites", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private Set<User> favoritedBy = new HashSet<>();

    public Project() {
    }

    public Project(List<QualityProperty> qps) {
        this.addQualityProperties(qps);
    }

    public Project(String name, ProjectInitiator projectInitiator, List<QualityProperty> qps) {
        this.name = name;
        this.projectInitiator = projectInitiator;
        this.addQualityProperties(qps);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectInitiator getProjectInitiator() {
        return projectInitiator;
    }

    public void setProjectInitiator(ProjectInitiator projectInitiator) {
        this.projectInitiator = projectInitiator;
    }

    public Set<QualityProperty> getQualityProperties() {
        return qualityProperties;
    }

    public void setQualityProperties(Set<QualityProperty> qualityProperties) {
        this.qualityProperties = qualityProperties;
    }

    public void addQualityProperty(QualityProperty qp) {
        this.qualityProperties.add(qp);
        qp.addUsedByProject(this);
    }

    public void addQualityProperties(List<QualityProperty> qps) {
        for (QualityProperty qp : qps) {
            this.addQualityProperty(qp);
        }
    }

    public Set<Instance> getQualityAttributes() {
        return qualityAttributes;
    }

    public void setQualityAttributes(Set<Instance> qualityAttributes) {
        this.qualityAttributes = qualityAttributes;
    }

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

    public void removeQualityProperties() {
        for (QualityProperty qp : this.qualityProperties) {
            qp.getUsedByProject().remove(this);
        }
        this.qualityProperties.clear();
    }

    public JIRAConnection getJiraConnection() {
        return jiraConnection;
    }

    public void setJiraConnection(JIRAConnection jiraConnection) {
        this.jiraConnection = jiraConnection;
    }

    public Set<User> getFavoritedBy() {
        return favoritedBy;
    }

    public void setFavoritedBy(Set<User> isFavorite) {
        this.favoritedBy = isFavorite;
    }

    public void removeFavoritedBy() {
        for (User user : this.favoritedBy) {
            user.getFavorites().remove(this);
        }
        this.favoritedBy.clear();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("projectInitiator", projectInitiator)
                .append("name", name)
                .append("qualityProperties", qualityProperties)
                .append("qualityAttributes", qualityAttributes)
                .append("jiraKey", jiraKey)
                .append("jiraConnection", jiraConnection)
                .append("favoritedBy", favoritedBy)
                .toString();
    }
}

