package logics.project;

import com.google.inject.Inject;
import util.Helper;
import dao.models.QualityPropertyDAO;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import models.project.QualityProperty;

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

    public models.project.QualityProperty createQualityProperty(models.project.QualityProperty qualityProperty) throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        if (qualityProperty != null && helper.validate(qualityProperty.getName()) && helper.validate(qualityProperty.getDescription())) {
            if (qualityPropertyDAO.findByName(qualityProperty.getName()) == null) {
                qualityProperty.setId(null);
                return qualityPropertyDAO.persist(qualityProperty);
            }
            throw new EntityAlreadyExistsException("QualityProperty with this name already exists!");
        }
        throw new MissingParameterException("Please provide all Parameters!");
    }

    public models.project.QualityProperty updateQualityProperty(models.project.QualityProperty qualityProperty) throws EntityNotFoundException, MissingParameterException, EntityAlreadyExistsException {
        if (qualityProperty != null && qualityProperty.getId() != null && helper.validate(qualityProperty.getName()) && helper.validate(qualityProperty.getDescription())) {
            models.project.QualityProperty persistedQualityProperty = qualityPropertyDAO.readById(qualityProperty.getId());
            QualityProperty tempQualityProperty = qualityPropertyDAO.findByName(qualityProperty.getName());
            if (tempQualityProperty == null || tempQualityProperty.getId() == persistedQualityProperty.getId()) {
                persistedQualityProperty.setName(qualityProperty.getName());
                persistedQualityProperty.setDescription(qualityProperty.getDescription());
                return qualityPropertyDAO.update(qualityProperty);
            }
            throw new EntityAlreadyExistsException("QualityProperty with this name already exists!");
        }
        throw new MissingParameterException("Name and Description need to be filled!");
    }

    public void deleteQualityProperty(Long id) throws EntityNotFoundException, MissingParameterException {
        if (id != null) {
            qualityPropertyDAO.remove(qualityPropertyDAO.readById(id));
        } else {
            throw new MissingParameterException("Please provide all Parameters!");
        }
    }
}
