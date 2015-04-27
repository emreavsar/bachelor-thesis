package models.template;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.ArrayList;
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
    public QA() {
    }

    public QA(String description) {
        this.description = description;
    }

    private String description;

    @OneToMany(mappedBy = "qa", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    private Set<CatalogQA> usedInCatalog = new HashSet<>();

    @ManyToMany(mappedBy = "usedInQA", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<QACategory> categories = new HashSet<>();

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
}
