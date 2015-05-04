package models.template;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by corina on 04.05.2015.
 */

@Entity
@Table(name = "stringvarval")
@Nullable
public class StringVarVal extends QAVarVal {
    private String value;

    public StringVarVal(String value) {
        super();
        this.value = value;
    }

    public StringVarVal() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
