package logics.project;

import dao.models.QualityPropertyDAO;


import java.util.List;

/**
 * Created by corina on 03.05.2015.
 */
public class QualityProperty {
    public static List<models.project.QualityProperty> getAllQualityProperties() {
        QualityPropertyDAO qualityPropertyDAO = new QualityPropertyDAO();
        return qualityPropertyDAO.readAll();
    }

    public static models.project.QualityProperty createQualityProperty(String name, String description) {
        QualityPropertyDAO qualityPropertyDAO = new QualityPropertyDAO();
        models.project.QualityProperty qp = new models.project.QualityProperty(name, description);
        return qualityPropertyDAO.persist(qp);
    }
}
