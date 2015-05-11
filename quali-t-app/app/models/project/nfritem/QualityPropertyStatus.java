package models.project.nfritem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;
import models.project.QualityProperty;

import javax.annotation.Nullable;
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
    @ManyToOne
    @JsonBackReference("qaStatus")
    private Instance qa;

    @ManyToOne
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
}
