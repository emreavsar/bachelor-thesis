package models.template;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by corina on 31.03.2015.
 */

@Entity
@Table(name = "qacategory")
@Nullable
public class QACategory extends AbstractEntity {
    public QACategory() {
    }

    public QACategory (String name) {
        this.name = name;
    }

    public QACategory (QACategory parent, String name) {
        if(parent==null) throw new IllegalArgumentException("parent required");

        this.parent = parent;
        this.name = name;
        registerInSuperCategory();
    }

    private String name;

    private String icon;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private QACategory parent;
    @OneToMany (mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<QACategory> categories = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    private Set<QA> usedInQA = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<QA> getUsedInQA() {
        return usedInQA;
    }

    public void setUsedInQA(Set<QA> usedInQA) {
        this.usedInQA = usedInQA;
    }

    public void addUsedInTemplate(QA QA) {
        this.usedInQA.add(QA);
    }

    private void registerInSuperCategory() {
        this.parent.categories.add(this);
    }

    public Set<QACategory> getCategories() {
        return Collections.unmodifiableSet(this.categories);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
