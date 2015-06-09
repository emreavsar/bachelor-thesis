package models.project.nfritem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;
import models.project.QualityProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by corina on 27.04.2015.
 */

@Entity
@Table(name = "qualitypropertystatus")
@Nullable
public class QualityPropertyStatus extends AbstractEntity {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference("qaStatus")
    private Instance qa;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference("qpStatus")
    private QualityProperty qp;

    private boolean status;

    public QualityPropertyStatus() {
    }

    public QualityPropertyStatus(Instance qa, QualityProperty qp) {
        this.qa = qa;
        this.qp = qp;
        this.status = false;
    }

    public QualityPropertyStatus(Instance qa, QualityProperty qp, boolean status) {
        this.qa = qa;
        this.qp = qp;
        this.status = status;
    }

    public Instance getQa() {
        return qa;
    }

    public void setQa(Instance qa) {
        this.qa = qa;
    }

    public QualityProperty getQp() {
        return qp;
    }

    public void setQp(QualityProperty qp) {
        this.qp = qp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public QualityPropertyStatus prepareToRemove() {
        this.getQa().getQualityPropertyStatus().remove(this);
        this.getQp().getQualityPropertyStatus().remove(this);
        this.setQp(null);
        this.setQa(null);
        return this;
    }

    public QualityPropertyStatus copyQualityPropertyStatus() {
        QualityPropertyStatus qualityPropertyStats = new QualityPropertyStatus();
        qualityPropertyStats.setQp(this.qp);
        return qualityPropertyStats;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("qa", qa)
                .append("qp", qp)
                .append("status", status)
                .toString();
    }
}
