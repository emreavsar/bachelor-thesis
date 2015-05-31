package models.template;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.HashSet;
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
    @JsonBackReference(value = "variables")
    private CatalogQA template;
    @Enumerated(EnumType.STRING)
    private QAType type;
    private boolean extendable;
    @OneToMany(mappedBy = "valInVar", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference(value = "variableValue")
    private Set<QAVarVal> values = new HashSet<>();
    @OneToOne(mappedBy = "rangeInVar", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference(value = "variableRange")
    private ValRange valRange;
    private String averageValue;
    private String mosteUsedValue;

    public QAVar(int varIndex) {
        super();
        this.varIndex = varIndex;
    }

    public QAVar() {
        this.extendable = false;
    }

    public QAVar copyQAVar() {
        QAVar qaVar = new QAVar(this.varIndex);
        qaVar.type = this.type;
        qaVar.extendable = this.extendable;
        if (this.getValRange() != null) {
            qaVar.valRange = this.getValRange().copyValRange();
        }
        if (!this.getValues().isEmpty()) {
            for (QAVarVal qaVarVal : this.getValues()) {
                qaVar.addValue(qaVarVal.copyQAVarVal());
            }
        }
        return qaVar;
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

    public QAVarVal getDefaultValue() {
        for (QAVarVal val : this.values) {
            if (val.isDefault) {
                return val;
            }
        }
        return null;
    }

    public void setDefaultValue(String defaultValue) {
        for (QAVarVal val : this.values) {
            if (val.getValue().equals(defaultValue)) {
                val.setIsDefault(true);
            } else {
                val.setIsDefault(false);
            }
        }
    }

    public boolean isExtendable() {
        return extendable;
    }

    public void setExtendable(boolean extendable) {
        this.extendable = extendable;
    }

    public String getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(String averageValue) {
        this.averageValue = averageValue;
    }

    public String getMosteUsedValue() {
        return mosteUsedValue;
    }

    public void setMosteUsedValue(String mosteUsedValue) {
        this.mosteUsedValue = mosteUsedValue;
    }
}
