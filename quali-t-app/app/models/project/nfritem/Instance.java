package models.project.nfritem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;
import models.project.Project;
import models.project.QualityProperty;
import models.template.CatalogQA;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by corina on 03.04.2015.
 */

@Entity
@Table(name = "instance")
@Nullable
public class Instance extends AbstractEntity {

    private String description;
    @ManyToOne
    @JsonBackReference
    private Project project;
    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private CatalogQA template;
    @OneToMany(mappedBy = "instance", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonManagedReference
    private Set<Val> values = new HashSet<>();
    @OneToMany(mappedBy = "qa", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonManagedReference
    private Set<QualityPropertyStatus> qualityPropertyStatus = new HashSet<>();
    private String jiraKey;
    private String jiraDirectURL;

    public Instance() {
    }

    public Instance(String description) {
        this.description = description;
    }

    public Instance(String description, CatalogQA qa) {
        this.description = description;
        this.template = qa;
    }

    public Instance(String description, CatalogQA qa, List<QualityProperty> qps) {
        this.addQualityProperty(qps);
        this.description = description;
        this.template = qa;
    }

    public void addQualityProperty(Collection<QualityProperty> qps) {
        for (QualityProperty qp : qps) {
            addQualityProperty(qp);
        }
    }

    public void addQualityProperty(QualityProperty qp) {
        qualityPropertyStatus.add(new QualityPropertyStatus(this, qp));
    }

    public void addQualityProperty(QualityProperty qp, boolean status) {
        qualityPropertyStatus.add(new QualityPropertyStatus(this, qp, status));
    }

    public void removeQualityProperty(QualityProperty qp) {
        for (QualityPropertyStatus qps : this.getQualityPropertyStatus()) {
            if (qps.getQp().getId() == qp.getId()) ;
            this.getQualityPropertyStatus().remove(qps);
        }
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public CatalogQA getTemplate() {
        return template;
    }

    public void setTemplate(CatalogQA template) {
        this.template = template;
    }

    public Set<Val> getValues() {
        return values;
    }

    public void setValues(Set<Val> values) {
        this.values = values;
    }

    public Set<QualityPropertyStatus> getQualityPropertyStatus() {
        return qualityPropertyStatus;
    }

    public void setQualityPropertyStatus(Set<QualityPropertyStatus> qualityPropertyStatus) {
        this.qualityPropertyStatus = qualityPropertyStatus;
    }

    public void addValue(Val val) {
        this.values.add(val);
        val.setInstance(this);
    }

    public void addValues(List<Val> valueList) {
        for (Val value : valueList) {
            this.addValue(value);
        }
    }

    public void addQualityPropertyStatus(QualityPropertyStatus qualityPropertyStatus) {
        this.qualityPropertyStatus.add(qualityPropertyStatus);
        qualityPropertyStatus.setQa(this);
    }

    public Instance copyInstance() {
        Instance instance = new Instance();
        instance.setProject(this.project);
        instance.setDescription(this.description);
        instance.setTemplate(this.template);
        for (Val value : this.values) {
            instance.addValue(value.copyVal());
        }
        for (QualityPropertyStatus qualityPropertyStatus : this.qualityPropertyStatus) {
            instance.addQualityPropertyStatus(qualityPropertyStatus.copyQualityPropertyStatus());
        }
        return instance;
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public String getJiraDirectURL() {
        return jiraDirectURL;
    }

    public void setJiraDirectURL(String jiraDirectURL) {
        this.jiraDirectURL = jiraDirectURL;
    }
}
