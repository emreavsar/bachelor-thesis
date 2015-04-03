package models.authentication;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emre on 30/03/15.
 */
@Entity
@Table(name = "role")
@Nullable
public class Role extends AbstractEntity {
    private String name;

    @ManyToMany(mappedBy = "roles")
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
}
