package models;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by emre on 06/03/15.
 */

@Entity
@Table(name = "`NFR`")
@Nullable
public class Nfr extends AbstractEntity {
    public String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}