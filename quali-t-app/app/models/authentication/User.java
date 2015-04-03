package models.authentication;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;
import org.apache.commons.lang3.RandomStringUtils;

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
public class User extends AbstractEntity {
    private String name;

    @JsonIgnore
    private String salt;

    @JsonIgnore
    private String hashedPassword;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "user")
    @JsonManagedReference
    private List<Role> roles = new ArrayList<>();


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonBackReference
    private List<Token> token = new ArrayList<>();

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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
}