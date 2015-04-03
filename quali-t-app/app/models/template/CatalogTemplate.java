package models.template;


import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "catalogtemplate")
@Nullable
public class CatalogTemplate extends AbstractEntity {
    public CatalogTemplate() {
    }

    public CatalogTemplate(Template template, Catalog catalog) {
        this.catalogID = catalog;
        this.templateID = template;
    }

    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Catalog catalogID;

    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Template templateID;

    @OneToMany(mappedBy = "template", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<ValRange.TemplateVar> vars = new HashSet<>();

    public Set<ValRange.TemplateVar> getVars() {
        return vars;
    }

    public void setVars(Set<ValRange.TemplateVar> vars) {
        this.vars = vars;
    }

    public Catalog getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(Catalog catalogID) {
        this.catalogID = catalogID;
    }

    public Template getTemplateID() {
        return templateID;
    }

    public void setTemplateID(Template templateID) {
        this.templateID = templateID;
    }
}
