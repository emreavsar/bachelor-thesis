package models.authentication;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emre on 30/03/15.
 */
@Entity
@Table(name = "role")
@Nullable
public class Role extends AbstractEntity implements be.objectify.deadbolt.core.models.Role {
    private String name;

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    private List<User> user = new ArrayList<>();

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("user", "disabled because of infinite loops (bi-directional)")
                .toString();
    }
}
