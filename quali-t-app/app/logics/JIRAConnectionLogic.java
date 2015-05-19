package logics;

import dao.interfaces.JIRAConnectionDAO;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.Interface.JIRAConnection;

import java.util.List;

import static controllers.Helper.validate;

/**
 * Created by corina on 13.05.2015.
 */
public class JIRAConnectionLogic {
    static JIRAConnectionDAO jiraConnectionDAO = new JIRAConnectionDAO();

    public static List<JIRAConnection> getAllJIRAConnections() {
        return jiraConnectionDAO.readAll();
    }

    public static void deleteJIRAConnection(Long id) throws EntityNotFoundException {
        jiraConnectionDAO.remove(jiraConnectionDAO.readById(id));
    }

    public static JIRAConnection createJIRAConnection(JIRAConnection jiraConnection) throws MissingParameterException {
        if (jiraConnection != null && validate(jiraConnection.getHostAddress()) && validate(jiraConnection.getPassword()) && validate(jiraConnection.getUsername())) {
            jiraConnection.setId(null);
            return jiraConnectionDAO.persist(jiraConnection);

        }
        throw new MissingParameterException("Please provide all Parameters!");
    }

    public static JIRAConnection updateJIRAConnection(JIRAConnection jiraConnection) throws EntityNotFoundException, MissingParameterException {
        if (jiraConnection != null && jiraConnection.getId() != null && validate(jiraConnection.getHostAddress()) && validate(jiraConnection.getPassword()) && validate(jiraConnection.getUsername())) {
            JIRAConnection persistedJiraConnection = jiraConnectionDAO.readById(jiraConnection.getId());
            persistedJiraConnection.setHostAddress(jiraConnection.getHostAddress());
            persistedJiraConnection.setUsername(jiraConnection.getUsername());
            persistedJiraConnection.setPassword(jiraConnection.getPassword());
            return jiraConnectionDAO.update(persistedJiraConnection);
        }
        throw new MissingParameterException("Please provide all Parameters!");
    }
}
