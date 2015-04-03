package models.template;

import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;

/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "templatevarval")
@Nullable
public class TemplateVarVal extends AbstractEntity {
    private String value;

    @ManyToOne
    private ValRange.TemplateVar valInVar;

//    @OneToOne(optional = true)
//    private TemplateVar defaultValIn;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ValRange.TemplateVar getValInVar() {
        return valInVar;
    }

    public void setValInVar(ValRange.TemplateVar usedInVar) {
        this.valInVar = usedInVar;
    }

//    public TemplateVar getDefaultValIn() {
//        return defaultValIn;
//    }
//
//    public void setDefaultValIn(TemplateVar defaultValIn) {
//        this.defaultValIn = defaultValIn;
//    }
}
