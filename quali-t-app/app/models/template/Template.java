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
public class Template extends AbstractEntity {
    public Template() {
    }

    private String description;

    @OneToMany(mappedBy = "templateID")
    private Set<CatalogTemplate> usedInCatalog = new HashSet<>();

    @ManyToMany(mappedBy = "usedInTemplate", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<NFRCategory> categories = new HashSet<>();

    public Set<NFRCategory> getCategories() {
        return categories;
    }

    public void addCategories(NFRCategory category) {
        categories.add(category);
    }

    public void setCategories(Set<NFRCategory> categories) {
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
