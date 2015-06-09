package models.template;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
    @JsonBackReference
    private QAVar rangeInVar;

    public ValRange() {
    }

    public ValRange(Float min, Float max) {
        this.min = min;
        this.max = max;
    }

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

    public ValRange copyValRange() {
        return new ValRange(this.min, this.max);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("min", min)
                .append("max", max)
                .append("rangeInVar", rangeInVar)
                .toString();
    }
}
