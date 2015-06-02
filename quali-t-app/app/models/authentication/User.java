package models.authentication;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Subject;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;
import models.user.Task;
import models.project.Project;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.*;

/**
 * Created by emre on 30/03/15.
 */

@Entity
@Table(name = "\"user\"")
// TODO: fix this, otherwise it is bounded to postgresql because of the ecaping, maybe this helps http://stackoverflow.com/questions/3364835/automatic-reserved-word-escaping-for-hibernate-tables-and-columns
@Nullable
public class User extends AbstractEntity implements Subject {
    private String name;

    @JsonIgnore
    private String salt;

    @JsonIgnore
    private String hashedPassword;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<Role> roles = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonBackReference
    private List<Token> token = new ArrayList<>();

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "favorite_project",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id")})
//    @ManyToMany(mappedBy = "isFavorite", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private Set<Project> favorites = new HashSet<>();

    public User() {
    }

    public void initSalt() {
        salt = RandomStringUtils.randomAlphabetic(20);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public List<Token> getToken() {
        return token;
    }

    public void setToken(List<Token> token) {
        this.token = token;
    }

    public void removeToken(Token userToken) {
        token.remove(userToken);
    }

    @Override
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return getId().toString();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Project> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Project> favorites) {
        this.favorites = favorites;
    }

    public User addToFavorites(Project project) {
        favorites.add(project);
        return this;
    }

    public User removeFromFavorites(Project projectToFavorite) {
        favorites.remove(projectToFavorite);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("roles", roles)
                .append("token", token)
                .toString();
    }

    public void addToken(Token token) {
        this.token.add(token);
        token.setUser(this);
    }

    public void addRole(Role role) {
        if (role != null) {
            this.roles.add(role);
            role.getUser().add(this);
        }
    }

    public void removeRoles() {
        for (Role role : this.getRoles()) {
            role.getUser().remove(this);
        }
        this.roles.clear();
    }
}