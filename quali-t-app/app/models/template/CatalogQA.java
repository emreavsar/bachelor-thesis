package models.template;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;
import models.project.nfritem.Instance;
import play.Logger;

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
public class CatalogQA extends AbstractEntity {
    @OneToMany
    @JsonManagedReference("qaTemplate")
    private Set<Instance> qaInstances = new HashSet<>();
    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "catalogQAs")
    private Catalog catalog;
    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference(value = "QA")
    private QA qa;
    @OneToMany(mappedBy = "template", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference(value = "variables")
    private Set<QAVar> variables = new HashSet<>();
    private boolean deleted;

    public CatalogQA() {
        this.deleted = false;
    }

    public CatalogQA(QA qa, Catalog catalog) {
        super();
        this.catalog = catalog;
        this.qa = qa;
    }

    public CatalogQA copyCatalogQA() {
        CatalogQA catalogQA = new CatalogQA();
        catalogQA.setCatalog(this.catalog);
        Logger.info(" blubb   ");
        if (!this.getVariables().isEmpty()) {
            for (QAVar qaVar : this.getVariables()) {
                catalogQA.addVar(qaVar.copyQAVar());
            }
        }
        return catalogQA;
    }

    public Set<QAVar> getVariables() {
        return variables;
    }

    public void setVariables(Set<QAVar> vars) {
        this.variables = vars;
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
        if (qaVars != null) {
            for (QAVar var : qaVars) {
                this.addVar(var);
            }
        }
    }

    public CatalogQA addVar(QAVar var) {
        var.setTemplate(this);
        this.variables.add(var);
        return this;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Instance> getQaInstances() {
        return qaInstances;
    }

    public void setQaInstances(Set<Instance> qaInstances) {
        this.qaInstances = qaInstances;
    }
}
