package models.project.nfritem;

import models.AbstractEntity;
import models.project.Project;
import models.template.CatalogTemplate;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by corina on 03.04.2015.
 */

@Entity
@Table(name = "instance")
@Nullable
public class Instance extends AbstractEntity {
    private String description;

    @ManyToOne
    private Project project;

    @ManyToOne(optional = true)
    private CatalogTemplate template;

    @OneToMany(mappedBy = "instance")
    private Set<Val> values = new HashSet<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public CatalogTemplate getTemplate() {
        return template;
    }

    public void setTemplate(CatalogTemplate template) {
        this.template = template;
    }

    public Set<Val> getValues() {
        return values;
    }

    public void setValues(Set<Val> values) {
        this.values = values;
    }
}
