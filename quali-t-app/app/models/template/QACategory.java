package models.template;

import models.AbstractEntity;

import javax.persistence.CascadeType;
import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "nfrcategory")
@Nullable
public class QACategory extends AbstractEntity {
    public QACategory() {
    }

    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<QualityAttribute> usedInQA = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<QualityAttribute> getUsedInQA() {
        return usedInQA;
    }

    public void setUsedInQA(Set<QualityAttribute> usedInQualityAttribute) {
        this.usedInQA = usedInQualityAttribute;
    }

    public void addUsedInTemplate(QualityAttribute QualityAttribute) {
        this.usedInQA.add(QualityAttribute);
    }
}
