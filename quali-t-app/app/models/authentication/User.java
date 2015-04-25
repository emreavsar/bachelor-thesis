package models.authentication;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Subject;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;
import models.project.Project;
import models.task.Task;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_user",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @JsonManagedReference
    private List<Role> roles = new ArrayList<>();


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonBackReference
    private List<Token> token = new ArrayList<>();

    @OneToMany(mappedBy = "assignee")
    @JsonManagedReference
    private List<Task> tasks = new ArrayList<>();

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

    @Override
    public List<? extends Permission> getPermissions() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return getId().toString();
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("roles", roles)
                .append("token", token)
                .toString();
    }

}