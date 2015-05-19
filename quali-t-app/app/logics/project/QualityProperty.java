package logics.project;

import dao.models.QualityPropertyDAO;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;

import java.util.List;

import static controllers.Helper.validate;

/**
 * Created by corina on 03.05.2015.
 */
public class QualityProperty {
    static QualityPropertyDAO qualityPropertyDAO = new QualityPropertyDAO();

    public static List<models.project.QualityProperty> getAllQualityProperties() {
        return qualityPropertyDAO.readAll();
    }

    public static models.project.QualityProperty createQualityProperty(models.project.QualityProperty qualityProperty) throws MissingParameterException {
        if (validate(qualityProperty.getName()) && validate(qualityProperty.getDescription())) {
            qualityProperty.setId(null);
            return qualityPropertyDAO.persist(qualityProperty);
        }
        throw new MissingParameterException("Please provide all Parameters!");
    }

    public static models.project.QualityProperty updateQualityProperty(models.project.QualityProperty qualityProperty) throws EntityNotFoundException, MissingParameterException {
        if (validate(qualityProperty.getName()) && validate(qualityProperty.getDescription()) && qualityProperty.getId() != null) {
            models.project.QualityProperty persistedQualityProperty = qualityPropertyDAO.readById(qualityProperty.getId());
            persistedQualityProperty.setName(qualityProperty.getName());
            persistedQualityProperty.setDescription(qualityProperty.getDescription());
            return qualityPropertyDAO.update(qualityProperty);
        }
        throw new MissingParameterException("Name and Description need to be filled!");
    }

    public static void deleteQualityProperty(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            qualityPropertyDAO.remove(qualityPropertyDAO.readById(id));
        }
        throw new MissingParameterException("Please provide all Parameters!");
    }
}
