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
@Table(name = "template")
@Nullable
public class QualityAttribute extends AbstractEntity {
    public QualityAttribute() {
    }

    private String description;

    @OneToMany(mappedBy = "qa")
    private Set<CatalogTemplate> usedInCatalog = new HashSet<>();

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

    public Set<CatalogTemplate> getUsedInCatalog() {
        return usedInCatalog;
    }

    public void setUsedInCatalog(Set<CatalogTemplate> usedInCatalog) {
        this.usedInCatalog = usedInCatalog;
    }

    public void addUsedInCatalog(Catalog catalog) {
        CatalogTemplate nfrtemplatecatalog = new CatalogTemplate(this, catalog);
        this.usedInCatalog.add(nfrtemplatecatalog);
    }
}
