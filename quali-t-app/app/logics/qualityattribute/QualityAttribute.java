package logics.qualityattribute;

import dao.models.QualityAttributeDAO;
import models.template.QA;

/**
 * Created by corina on 09.04.2015.
 */
public class QualityAttribute {
    public static QA createQA(String qaText) {
        if (qaText.equals("")) {
            return null; }
        else {
            QualityAttributeDAO qaDAO = new QualityAttributeDAO();
            QA qa = new QA(qaText);
            qaDAO.persist(qa);
            return qa;
        }
    }
}