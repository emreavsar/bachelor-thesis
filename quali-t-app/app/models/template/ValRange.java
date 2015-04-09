package models.template;

import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;

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
    private QAVar rangeInVar;

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public QAVar getRangeInVar() {
        return rangeInVar;
    }

    public void setRangeInVar(QAVar rangeInVar) {
        this.rangeInVar = rangeInVar;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }
}
