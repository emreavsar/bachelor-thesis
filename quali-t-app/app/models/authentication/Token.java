package models.authentication;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.AbstractEntity;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.*;

/**
 * Created by emre on 31/03/15.
 */
@Entity
@Table(name = "token")
@Nullable
public class Token extends AbstractEntity {
    private String token;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime validUntil;

    @ManyToOne
    @JsonManagedReference
    private User user;

    public Token() {
    }

    public Token(String token, DateTime validUntil, User user) {
        this.token = token;
        this.validUntil = validUntil;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(DateTime validUntil) {
        this.validUntil = validUntil;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
