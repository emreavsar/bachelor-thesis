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
    public String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}