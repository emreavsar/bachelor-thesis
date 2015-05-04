package models.template;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by corina on 09.04.2015.
 */

@Entity
@Table(name = "qavar")
@Nullable
public class QAVar extends AbstractEntity {
    private int varIndex;
    @ManyToOne
    @JsonBackReference
    private CatalogQA template;
    @Enumerated(EnumType.STRING)
    private QAType type;
    @OneToMany(mappedBy = "valInVar", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private Set<QAVarVal> values = new HashSet<>();
    @OneToOne(mappedBy = "rangeInVar", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private ValRange valRange;
    @OneToOne(mappedBy = "defaultValIn", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private QAVarVal defaultVal;

    public QAVar(int varNumber) {
        this.varIndex = varNumber;
    }

    public QAVar() {
    }

    public int getVarIndex() {
        return varIndex;
    }

    public void setVarIndex(int varIndex) {
        this.varIndex = varIndex;
    }

    public CatalogQA getTemplate() {
        return template;
    }

    public void setTemplate(CatalogQA template) {
        this.template = template;
    }

    public QAType getType() {
        return type;
    }

    public void setType(QAType type) {
        this.type = type;
    }

    public Set<QAVarVal> getValues() {
        return values;
    }

    public void setValues(Set<QAVarVal> values) {
        this.values = values;
    }

    public ValRange getValRange() {
        return valRange;
    }

    public void setValRange(ValRange valRange) {
        this.valRange = valRange;
        valRange.setRangeInVar(this);
    }

    public void addValue(QAVarVal varValue) {
        this.values.add(varValue);
        varValue.setValInVar(this);
    }

    public void addValues(List<QAVarVal> varValues) {
        for (QAVarVal varVal : varValues) {
            addValue(varVal);
        }
    }

    public QAVarVal getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(QAVarVal defaultVal) {
        this.defaultVal = defaultVal;
    }
}
