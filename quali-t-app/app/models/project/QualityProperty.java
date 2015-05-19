package models.project;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;
import models.project.nfritem.QualityPropertyStatus;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by corina on 30.03.2015.
 */

@Entity
@Table(name = "qualityproperty")
@Nullable
public class QualityProperty extends AbstractEntity {
    private String name;
    private String description;
    @ManyToMany
    @JsonBackReference(value = "qualityProperty")
    private Set<Project> usedByProject = new HashSet<Project>();
    @OneToMany(mappedBy = "qp", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonBackReference("qpStatus")
    private Set<QualityPropertyStatus> qualityPropertyStatus = new HashSet<>();

    public QualityProperty() {
    }

    public QualityProperty(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public QualityProperty(Long id) {
        this.setId(id);
    }

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

    public Set<QualityPropertyStatus> getQualityPropertyStatus() {
        return qualityPropertyStatus;
    }

    public void setQualityPropertyStatus(Set<QualityPropertyStatus> qualityPropertyStatus) {
        this.qualityPropertyStatus = qualityPropertyStatus;
    }
}
