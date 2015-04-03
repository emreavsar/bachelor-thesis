package models.template;

import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "nfrtype")
@Nullable
public class NFRType extends AbstractEntity {
    private String name;

    @OneToMany(mappedBy = "type")
    private Set<ValRange.TemplateVar> vars = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ValRange.TemplateVar> getVars() {
        return vars;
    }

    public void setVars(Set<ValRange.TemplateVar> usedInTemplate) {
        this.vars = usedInTemplate;
    }
}
