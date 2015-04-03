package models.template;

import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by corina on 01.04.2015.
 */

@Entity
@Table(name = "valrange")
@Nullable
public class ValRange extends AbstractEntity {
    private float min;

    private float max;

    @OneToOne(optional = true)
    private TemplateVar rangeInVar;

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public TemplateVar getRangeInVar() {
        return rangeInVar;
    }

    public void setRangeInVar(TemplateVar rangeInVar) {
        this.rangeInVar = rangeInVar;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    /**
     * Created by corina on 31.03.2015.
     */

    @Entity
    @Table(name = "templatevar")
    @Nullable
    public static class TemplateVar extends AbstractEntity {
        private int varIndex;

        @ManyToOne
        private CatalogTemplate template;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        private NFRType type;

    //    @OneToOne(mappedBy = "defaultValIn", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    //    private TemplateVarVal defaultVal;

        @OneToMany(mappedBy = "valInVar", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        private Set<TemplateVarVal> values = new HashSet<>();

        @OneToOne(mappedBy = "rangeInVar", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        private ValRange valRange;

        public int getVarIndex() {
            return varIndex;
        }

        public void setVarIndex(int varIndex) {
            this.varIndex = varIndex;
        }

        public CatalogTemplate getCatalogTemplate() {
            return template;
        }

        public void setCatalogTemplate(CatalogTemplate catalogTemplate) {
            this.template = catalogTemplate;
        }

        public NFRType getType() {
            return type;
        }

        public void setType(NFRType type) {
            this.type = type;
        }

    //    public TemplateVarVal getDefaultVal() {
    //        return defaultVal;
    //    }
    //
    //    public void setDefaultVal(TemplateVarVal defaultVal) {
    //        this.defaultVal = defaultVal;
    //    }

        public Set<TemplateVarVal> getValues() {
            return values;
        }

        public void setValues(Set<TemplateVarVal> values) {
            this.values = values;
        }

        public ValRange getValRange() {
            return valRange;
        }

        public void setValRange(ValRange valRange) {
            this.valRange = valRange;
        }
    }
}
