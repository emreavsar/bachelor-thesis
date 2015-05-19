package models.Interface;

import com.fasterxml.jackson.annotation.JsonBackReference;
import models.AbstractEntity;
import models.project.Project;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by corina on 12.05.2015.
 */

@Entity
@Table(name = "jiraconnection")
@Nullable
public class JIRAConnection extends AbstractEntity {
    private String hostAddress;
    private String username;
    private String password;

    @OneToMany(mappedBy = "jiraConnection")
    @JsonBackReference(value = "projectsJiraConnection")
    protected Set<Project> usedForProject = new HashSet<>();

    public JIRAConnection(String hostAddress, String username, String password) {
        this.hostAddress = hostAddress;
        this.username = username;
        this.password = password;
    }

    public JIRAConnection() {
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Project> getUsedForProject() {
        return usedForProject;
    }

    public void setUsedForProject(Set<Project> usedForProject) {
        this.usedForProject = usedForProject;
    }
}
