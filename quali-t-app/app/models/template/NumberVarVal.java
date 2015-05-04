package models.template;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by corina on 04.05.2015.
 */
@Entity
@Table(name = "numbervarval")
@Nullable
public class NumberVarVal extends QAVarVal {
    private Float value;

    public NumberVarVal(Float value) {
        super();
        this.value = value;
    }

    public NumberVarVal() {
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
