package models.template;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JsonBackReference
    private QAVar valInVar;


    @OneToOne(optional = true)
    @JsonBackReference
    private QAVar defaultValIn;

    public QAVar getValInVar() {
        return valInVar;
    }

    public void setValInVar(QAVar usedInVar) {
        this.valInVar = usedInVar;
    }

    public QAVar getDefaultValIn() {
        return defaultValIn;
    }

    public void setDefaultValIn(QAVar defaultValIn) {
        this.defaultValIn = defaultValIn;
    }
}
