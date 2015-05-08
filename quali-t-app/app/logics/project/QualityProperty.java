package logics.project;

import dao.models.QualityPropertyDAO;
import exceptions.EntityNotFoundException;


import java.util.List;

/**
 * Created by corina on 03.05.2015.
 */
public class QualityProperty {
    static QualityPropertyDAO qualityPropertyDAO = new QualityPropertyDAO();

    public static List<models.project.QualityProperty> getAllQualityProperties() {
        return qualityPropertyDAO.readAll();
    }

    public static models.project.QualityProperty createQualityProperty(models.project.QualityProperty qualityProperty) {
        return qualityPropertyDAO.persist(qualityProperty);
    }

    public static models.project.QualityProperty updateQualityProperty(models.project.QualityProperty qualityProperty) {
        return qualityPropertyDAO.update(qualityProperty);
    }

    public static void deleteQualityProperty(long id) throws EntityNotFoundException {
        qualityPropertyDAO.remove(qualityPropertyDAO.readById(id));
    }
}
