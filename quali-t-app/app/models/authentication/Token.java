package models.authentication;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by emre on 31/03/15.
 */
@Entity
@Table(name = "token")
@Nullable
public class Token extends AbstractEntity {
    private String token;
    private LocalDateTime validUntil;

    @ManyToOne
    @JsonManagedReference
    private User user;

    public Token() {
    }

    public Token(String token, LocalDateTime validUntil, User user) {
        this.token = token;
        this.validUntil = validUntil;
        this.user = user;
    }

    public Token(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("token", token)
                .append("validUntil", validUntil)
                .append("user", user)
                .toString();
    }
}
