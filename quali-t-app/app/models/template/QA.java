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
@JsonIgnoreProperties({"id2"})
public class QA extends AbstractEntity {
    private String description;
    @OneToMany(mappedBy = "qa", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "QA")
    @JsonIgnore
    private Set<CatalogQA> usedInCatalog = new HashSet<>();
    @ManyToMany(mappedBy = "usedInQA", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JsonManagedReference(value="qaCategories")
    @JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "id2")
    private Set<QACategory> categories = new HashSet<>();
    private boolean deleted;
    private int versionNumber;

    public QA() {
        this.deleted = false;
    }

    public QA(String description, int versionNumber) {
        super();
        this.versionNumber = versionNumber;
        this.description = description;
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
}
