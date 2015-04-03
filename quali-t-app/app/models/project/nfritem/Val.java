package models.project.nfritem;

import models.AbstractEntity;

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
    private Instance instance;

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
}
