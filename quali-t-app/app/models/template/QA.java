package models.template;

import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "qa")
@Nullable
public class QA extends AbstractEntity {
    public QA() {
    }

    public QA(String description) {
        this.description = description;
    }

    private String description;

    @OneToMany(mappedBy = "qa")
    private Set<CatalogQA> usedInCatalog = new HashSet<>();

    @ManyToMany(mappedBy = "usedInQA", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<QACategory> categories = new HashSet<>();

    public Set<QACategory> getCategories() {
        return categories;
    }

    public void addCategories(QACategory category) {
        categories.add(category);
    }

    public void setCategories(Set<QACategory> categories) {
        this.categories = categories;
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

    public void addUsedInCatalog(Catalog catalog) {
        CatalogQA nfrtemplatecatalog = new CatalogQA(this, catalog);
        this.usedInCatalog.add(nfrtemplatecatalog);
    }
}
