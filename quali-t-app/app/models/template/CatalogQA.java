package models.template;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "catalogqa")
@Nullable
@JsonIgnoreProperties({"id2"})
public class CatalogQA extends AbstractEntity {
    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "catalogQAs")
    private Catalog catalog;
    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference(value = "QA")
    private QA qa;
    @OneToMany(mappedBy = "template", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference(value = "variables")
    private Set<QAVar> vars = new HashSet<>();
    private boolean deleted;

    public CatalogQA() {
        this.deleted = false;
    }

    public CatalogQA(QA qa, Catalog catalog) {
        super();
        this.catalog = catalog;
        this.qa = qa;
    }

    public Set<QAVar> getVars() {
        return vars;
    }

    public void setVars(Set<QAVar> vars) {
        this.vars = vars;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalogID) {
        this.catalog = catalogID;
    }

    public QA getQa() {
        return qa;
    }

    public void setQa(QA qaID) {
        this.qa = qaID;
    }

    public void addVars(List<QAVar> qaVars) {
        for (QAVar var : qaVars) {
            this.addVar(var);
        }
    }

    private void addVar(QAVar var) {
        this.vars.add(var);
        var.setTemplate(this);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
