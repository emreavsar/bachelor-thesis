package models.project;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by corina on 30.03.2015.
 */

@Entity
@Table(name = "qualityattribute")
@Nullable
public class QualityProperty extends AbstractEntity {
    public QualityProperty() {
    }

    public QualityProperty(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private String name;

    private String description;

    @ManyToMany
    @JsonBackReference
    private Set<Project> usedByProject = new HashSet<Project>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Project> getUsedByProject() {
        return usedByProject;
    }

    public void setUsedByProject(Set<Project> usedByProject) {
        this.usedByProject = usedByProject;
    }

    public void addUsedByProject(Project project) {
        usedByProject.add(project);
    }
}
