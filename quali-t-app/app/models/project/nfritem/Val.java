package models.project.nfritem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by corina on 03.04.2015.
 */

@Entity
@Table(name = "val")
@Nullable
public class Val extends AbstractEntity {
    private int varIndex;

    private String value;

    @ManyToOne(optional = true)
    @JsonBackReference("qaInstanceValues")
    private Instance instance;

    public Val() {
    }

    public Val(int varIndex, String value) {
        this.varIndex = varIndex;
        this.value = value;
    }
    public int getVarIndex() {
        return varIndex;
    }

    public void setVarIndex(int varIndex) {
        this.varIndex = varIndex;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public Val copyVal() {
        Val value = new Val();
        value.setValue(this.value);
        value.setVarIndex(this.varIndex);
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("varIndex", varIndex)
                .append("value", value)
                .append("instance", instance)
                .toString();
    }
}
