package models.template;

import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;

/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "qavarval")
@Nullable
public class QAVarVal extends AbstractEntity {
    private String value;

    @ManyToOne
    private QAVar valInVar;

//    @OneToOne(optional = true)
//    private QAVar defaultValIn;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public QAVar getValInVar() {
        return valInVar;
    }

    public void setValInVar(QAVar usedInVar) {
        this.valInVar = usedInVar;
    }

//    public QAVar getDefaultValIn() {
//        return defaultValIn;
//    }
//
//    public void setDefaultValIn(QAVar defaultValIn) {
//        this.defaultValIn = defaultValIn;
//    }
}
