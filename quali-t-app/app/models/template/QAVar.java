package models.template;

import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.HashSet;
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
    private CatalogQA template;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private QAType type;
    @OneToMany(mappedBy = "valInVar", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<QAVarVal> values = new HashSet<>();

    @OneToOne(mappedBy = "rangeInVar", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private ValRange valRange;

    public int getVarIndex() {
        return varIndex;
    }

    public void setVarIndex(int varIndex) {
        this.varIndex = varIndex;
    }

    public CatalogQA getCatalogTemplate() {
        return template;
    }

    public void setCatalogTemplate(CatalogQA catalogQA) {
        this.template = catalogQA;
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
    }

    //    @OneToOne(mappedBy = "defaultValIn", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    //    private QAVarVal defaultVal;

    //    public QAVarVal getDefaultVal() {
    //        return defaultVal;
    //    }
    //
    //    public void setDefaultVal(QAVarVal defaultVal) {
    //        this.defaultVal = defaultVal;
    //    }
}
