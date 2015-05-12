package logics.project;

import com.fasterxml.jackson.databind.JsonNode;
import dao.models.QualityPropertyDAO;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;

import java.util.List;

/**
 * Created by corina on 03.05.2015.
 */
public class QualityProperty {
    static QualityPropertyDAO qualityPropertyDAO = new QualityPropertyDAO();

    public static List<models.project.QualityProperty> getAllQualityProperties() {
        return qualityPropertyDAO.readAll();
    }

    public static models.project.QualityProperty createQualityProperty(JsonNode json) throws MissingParameterException {
        if (validate(json)) {
            return qualityPropertyDAO.persist(new models.project.QualityProperty(json.findPath("name").asText(), json.findPath("description").asText()));
        } else {
            throw new MissingParameterException("Name and Description need to be filled!");
        }
    }

    private static boolean validate(JsonNode json) {
        return (json.findPath("name").asText() != "" && json.findPath("description").asText() != "");
    }

    public static models.project.QualityProperty updateQualityProperty(JsonNode json) throws EntityNotFoundException, MissingParameterException {
        if (validate(json)) {
            models.project.QualityProperty qualityProperty = qualityPropertyDAO.readById(json.findPath("id").asLong());
            qualityProperty.setName(json.findPath("name").asText());
            qualityProperty.setDescription(json.findPath("description").asText());
            return qualityPropertyDAO.update(qualityProperty);
        } else {
            throw new MissingParameterException("Name and Description need to be filled!");
        }
    }

    public static void deleteQualityProperty(long id) throws EntityNotFoundException {
        qualityPropertyDAO.remove(qualityPropertyDAO.readById(id));
    }
}
