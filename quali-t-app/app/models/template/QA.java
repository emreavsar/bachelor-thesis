package models.template;

import com.fasterxml.jackson.annotation.*;
import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "qa")
@Nullable
public class QA extends AbstractEntity {
    private String description;
    @OneToMany(mappedBy = "qa", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    private Set<CatalogQA> usedInCatalog = new HashSet<>();
    @ManyToMany(mappedBy = "usedInQA", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private Set<QACategory> categories = new HashSet<>();
    @OneToOne
    private QA previousVersion;

    private boolean deleted;
    private int versionNumber;

    public QA() {
        this.deleted = false;
    }

    public QA(String description) {
        super();
        this.versionNumber = 1;
        this.description = description;
    }

    public QA(String description, int versionNumber) {
        super();
        this.versionNumber = versionNumber;
        this.description = description;
    }

    public QA(Long id) {
        super();
        this.setId(id);
    }

    public QA copyQA() {
        QA newQA = new QA(this.getDescription(), 1);
        return newQA;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CatalogQA> getUsedInCatalog() {
        return usedInCatalog;
    }

    public void setUsedInCatalog(Set<CatalogQA> usedInCatalog) {
        this.usedInCatalog = usedInCatalog;
    }

    public Set<QACategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<QACategory> categories) {
        this.categories = categories;
    }

    private void addCategory(QACategory category) {
        this.categories.add(category);
        category.addUsedInTemplate(this);
    }

    public void addCategories(List<QACategory> categories) {
        for (QACategory category : categories){
            this.addCategory(category);
        }
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public QA addCatalogQA(CatalogQA catalogQA) {
        this.usedInCatalog.add(catalogQA);
        catalogQA.setQa(this);
        return this;
    }

    public QA getPreviousVersion() {
        return previousVersion;
    }

    public void setPreviousVersion(QA previousVersion) {
        this.previousVersion = previousVersion;
    }


}
