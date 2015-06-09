package models.template;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "catalog")
@Nullable
public class Catalog extends AbstractEntity {
    private String name;
    private String description;
    private String image;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "catalog", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    private Set<CatalogQA> templates = new HashSet<>();

    public Catalog() {
    }

    public Catalog(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String pictureURL) {
        this.image = pictureURL;
    }

    public Set<CatalogQA> getTemplates() {
        return templates;
    }

    public void setTemplates(Set<CatalogQA> templates) {
        this.templates = templates;
    }

    public Catalog addTemplate(QA QA) {
        CatalogQA catalogQA = new CatalogQA(QA, this);
        this.templates.add(catalogQA);
        return this;
    }

    public void addTemplates(List<QA> qas) {
        for (QA qa : qas) {
            this.addTemplate(qa);
        }
    }

    public Catalog addCatalogQA(CatalogQA catalogQA) {
        this.templates.add(catalogQA);
        catalogQA.setCatalog(this);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("description", description)
                .append("image", image)
                .append("templates", templates)
                .toString();
    }
}
