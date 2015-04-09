package logics.qualityattribute;

import dao.models.QualityAttributeDAO;
import models.template.QA;

import java.util.List;

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

    public static List<QA> getAllQAs() {
        QualityAttributeDAO qaDAO = new QualityAttributeDAO();
        List<QA> qa = qaDAO.readAll();
        return qa;
    }
}