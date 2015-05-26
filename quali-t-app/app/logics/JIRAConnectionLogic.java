package logics;

import com.google.inject.Inject;
import dao.interfaces.JIRAConnectionDAO;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.Interface.JIRAConnection;

import java.util.List;

/**
 * Created by corina on 13.05.2015.
 */
public class JIRAConnectionLogic {
    @Inject
    private JIRAConnectionDAO jiraConnectionDAO;
    @Inject
    private Helper helper;

    public List<JIRAConnection> getAllJIRAConnections() {
        return jiraConnectionDAO.readAll();
    }

    public void deleteJIRAConnection(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            jiraConnectionDAO.remove(jiraConnectionDAO.readById(id));
        } else {
            throw new MissingParameterException("Please provide all Parameters!");
        }
    }

    public JIRAConnection createJIRAConnection(JIRAConnection jiraConnection) throws MissingParameterException {
        if (jiraConnection != null && helper.validate(jiraConnection.getHostAddress()) && helper.validate(jiraConnection.getPassword()) && helper.validate(jiraConnection.getUsername())) {
            jiraConnection.setId(null);
            return jiraConnectionDAO.persist(jiraConnection);

        }
        throw new MissingParameterException("Please provide all Parameters!");
    }

    public JIRAConnection updateJIRAConnection(JIRAConnection jiraConnection) throws EntityNotFoundException, MissingParameterException {
        if (jiraConnection != null && jiraConnection.getId() != null && helper.validate(jiraConnection.getHostAddress()) && helper.validate(jiraConnection.getPassword()) && helper.validate(jiraConnection.getUsername())) {
            JIRAConnection persistedJiraConnection = jiraConnectionDAO.readById(jiraConnection.getId());
            persistedJiraConnection.setHostAddress(jiraConnection.getHostAddress());
            persistedJiraConnection.setUsername(jiraConnection.getUsername());
            persistedJiraConnection.setPassword(jiraConnection.getPassword());
            return jiraConnectionDAO.update(persistedJiraConnection);
        }
        throw new MissingParameterException("Please provide all Parameters!");
    }
}
