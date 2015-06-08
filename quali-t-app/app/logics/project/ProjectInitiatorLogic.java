package logics.project;

import com.google.inject.Inject;
import models.project.ProjectInitiator;
import util.Helper;
import dao.models.ProjectInitiatorDAO;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;

import java.util.List;

/**
 * Created by corina on 06.05.2015.
 */
public class ProjectInitiatorLogic {
    @Inject
    private ProjectInitiatorDAO projectInitiatorDAO;
    @Inject
    private ProjectLogic projectLogic;
    @Inject
    private Helper helper;

    public ProjectInitiator createProjectInitiator(ProjectInitiator projectInitiator) throws EntityAlreadyExistsException, MissingParameterException, EntityNotFoundException {
        if (projectInitiator != null && helper.validate(projectInitiator.getName())) {
            ProjectInitiator c = projectInitiatorDAO.findByName(projectInitiator.getName());
            if (c == null) {
                projectInitiator.setId(null);
                return projectInitiatorDAO.persist(projectInitiator);
            } else {
                throw new EntityAlreadyExistsException("ProjectInitiator name already exists");
            }
        } else {
            throw new MissingParameterException("Missing required paramter");
        }
    }

    public ProjectInitiator updateProjectInitiator(ProjectInitiator updatedProjectInitiator) throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        if (updatedProjectInitiator != null && updatedProjectInitiator.getId() != null && helper.validate(updatedProjectInitiator.getName())) {
            ProjectInitiator c = projectInitiatorDAO.findByName(updatedProjectInitiator.getName());
            if (c == null || c.getId().equals(updatedProjectInitiator.getId())) {
                ProjectInitiator projectInitiator = projectInitiatorDAO.readById(updatedProjectInitiator.getId());
                projectInitiator.setAddress(updatedProjectInitiator.getAddress());
                projectInitiator.setName(updatedProjectInitiator.getName());
                return projectInitiatorDAO.update(projectInitiator);
            } else {
                throw new EntityAlreadyExistsException("ProjectInitiator name already exists");
            }
        } else {
            throw new MissingParameterException("Missing required paramter");
        }
    }

    public List<ProjectInitiator> getAllProjectInitiators() {
        return projectInitiatorDAO.readAll();
    }

    public void deleteProjectInitiator(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            ProjectInitiator projectInitiator = projectInitiatorDAO.readById(id);
            for (models.project.Project project : projectInitiator.getProjects()) {
                projectLogic.deleteProject(project.getId());
            }
            projectInitiatorDAO.remove(projectInitiator);
        } else {
            throw new MissingParameterException("ID can't be null!");
        }
    }
}
