package models.template;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    public QA() {
    }

    public QA(String description, List<QACategory> categories) {
        this.description = description;
        this.addCategories(categories);
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
}
