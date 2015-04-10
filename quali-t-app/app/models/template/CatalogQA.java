package models.template;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "catalogqa")
@Nullable
public class CatalogQA extends AbstractEntity {
    public CatalogQA() {
    }

    public CatalogQA(QA QA, Catalog catalog) {
        this.catalog = catalog;
        this.qa = QA;
    }

    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private Catalog catalog;

    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private QA qa;

    @OneToMany(mappedBy = "template", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<QAVar> vars = new HashSet<>();

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

    public void setQa(QA QAID) {
        this.qa = QAID;
    }
}
