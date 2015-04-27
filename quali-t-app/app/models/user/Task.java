package models.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;
import models.authentication.User;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by emre on 18/04/15.
 */

@Entity
@Table(name = "task")
@Nullable
public class Task extends AbstractEntity {
    @ManyToOne(optional = true)
    @JsonBackReference
    private User assignee;

    private boolean done;

    private String description;

//    private AbstractEntity resource;

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public AbstractEntity getResource() {
//        return resource;
//    }
//
//    public void setResource(AbstractEntity resource) {
//        this.resource = resource;
//    }

    public Task() {
    }

    public void toggleState() {
        this.done = !this.done;
    }
}
