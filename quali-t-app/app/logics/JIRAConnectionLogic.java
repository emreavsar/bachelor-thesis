package logics;

import com.fasterxml.jackson.databind.JsonNode;
import dao.interfaces.JIRAConnectionDAO;
import exceptions.EntityNotFoundException;
import models.Interface.JIRAConnection;

import java.util.List;

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

    public static JIRAConnection createJIRAConnection(JsonNode json) {
        return jiraConnectionDAO.persist(new JIRAConnection(json.findPath("hostAddress").asText(), json.findPath("username").asText(), json.findPath("password").asText()));
    }

    public static JIRAConnection updateJIRAConnection(JsonNode json) throws EntityNotFoundException {
        JIRAConnection jiraConnection = jiraConnectionDAO.readById(json.findPath("id").asLong());
        jiraConnection.setHostAddress(json.findPath("hostAddress").asText());
        jiraConnection.setUsername(json.findPath("username").asText());
        jiraConnection.setPassword(json.findPath("password").asText());
        return jiraConnectionDAO.persist(jiraConnection);
    }
}
