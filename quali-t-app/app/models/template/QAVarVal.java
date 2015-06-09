
package models.template;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nullable;
import javax.persistence.*;

/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "qavarval")
@Nullable
public class QAVarVal extends AbstractEntity {
    boolean isDefault;
    @ManyToOne
    @JsonBackReference
    private QAVar valInVar;
    @Enumerated(EnumType.STRING)
    private ValueType type;
    private String value;

    public QAVarVal(String value, ValueType type) {
        super();
        this.value = value;
        this.type = type;
    }

    public QAVarVal() {
        this.isDefault = false;
    }

    public QAVarVal copyQAVarVal() {
        QAVarVal qaVarVal = new QAVarVal(this.value, this.type);
        qaVarVal.setIsDefault(this.isDefault);
        return qaVarVal;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public QAVar getValInVar() {
        return valInVar;
    }

    public void setValInVar(QAVar usedInVar) {
        this.valInVar = usedInVar;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("isDefault", isDefault)
                .append("valInVar", "disabled because of infinite loops (bi-directional)")
                .append("type", type)
                .append("value", value)
                .toString();
    }
}
