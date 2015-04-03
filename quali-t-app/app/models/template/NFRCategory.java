package models.template;

import models.AbstractEntity;

import javax.persistence.CascadeType;
import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "nfrcategory")
@Nullable
public class NFRCategory extends AbstractEntity {
    public NFRCategory() {
    }

    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Template> usedInTemplate = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Template> getUsedInTemplate() {
        return usedInTemplate;
    }

    public void setUsedInTemplate(Set<Template> usedInTemplate) {
        this.usedInTemplate = usedInTemplate;
    }

    public void addUsedInTemplate(Template template) {
        this.usedInTemplate.add(template);
    }
}
