package models.template;

import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "catalog")
@Nullable
public class Catalog extends AbstractEntity {
    public Catalog() {
    }

    private String name;

    private String description;

    private String pictureURL;

    @OneToMany(mappedBy = "catalog")
    private Set<CatalogTemplate> templates = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public Set<CatalogTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(Set<CatalogTemplate> templates) {
        this.templates = templates;
    }

    public void addTemplate(QualityAttribute QualityAttribute) {
        CatalogTemplate catalogTemplate = new CatalogTemplate(QualityAttribute, this);
        this.templates.add(catalogTemplate);
    }


}
