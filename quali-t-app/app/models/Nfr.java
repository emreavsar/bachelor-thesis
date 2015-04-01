package models;

import javax.annotation.Nullable;
import javax.persistence.*;

/**
 * Created by emre on 06/03/15.
 */

@Entity
@Table(name = "nfr")
@Nullable
public class Nfr extends AbstractEntity {

    public String description;

    public String something;

    public String getSomething() {
        return something;
    }

    public void setSomething(String something) {
        this.something = something;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}