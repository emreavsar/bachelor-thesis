package logics.project;

import com.google.inject.Inject;
import controllers.Helper;
import dao.models.QualityPropertyDAO;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;

import java.util.List;

/**
 * Created by corina on 03.05.2015.
 */
public class QualityPropertyLogic {
    @Inject
    private QualityPropertyDAO qualityPropertyDAO;
    @Inject
    private Helper helper;

    public List<models.project.QualityProperty> getAllQualityProperties() {
        return qualityPropertyDAO.readAll();
    }

    public models.project.QualityProperty createQualityProperty(models.project.QualityProperty qualityProperty) throws MissingParameterException {
        if (helper.validate(qualityProperty.getName()) && helper.validate(qualityProperty.getDescription())) {
            qualityProperty.setId(null);
            return qualityPropertyDAO.persist(qualityProperty);
        }
        throw new MissingParameterException("Please provide all Parameters!");
    }

    public models.project.QualityProperty updateQualityProperty(models.project.QualityProperty qualityProperty) throws EntityNotFoundException, MissingParameterException {
        if (helper.validate(qualityProperty.getName()) && helper.validate(qualityProperty.getDescription()) && qualityProperty.getId() != null) {
            models.project.QualityProperty persistedQualityProperty = qualityPropertyDAO.readById(qualityProperty.getId());
            persistedQualityProperty.setName(qualityProperty.getName());
            persistedQualityProperty.setDescription(qualityProperty.getDescription());
            return qualityPropertyDAO.update(qualityProperty);
        }
        throw new MissingParameterException("Name and Description need to be filled!");
    }

    public void deleteQualityProperty(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            qualityPropertyDAO.remove(qualityPropertyDAO.readById(id));
        }
        throw new MissingParameterException("Please provide all Parameters!");
    }
}
