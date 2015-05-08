package models.template;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "catalog")
@Nullable
public class Catalog extends AbstractEntity {
    private String name;
    private String description;
    private String pictureURL;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "catalog", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "catalogQAs")
    private Set<CatalogQA> templates = new HashSet<>();

    public Catalog() {
    }

    public Catalog(String name, String description, String  pictureURL, List<QA> qas){
        this.name = name;
        this.description = description;
        this.pictureURL = pictureURL;
        this.addTemplates(qas);
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

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
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


}
