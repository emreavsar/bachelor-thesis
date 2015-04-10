package logics.template;

import dao.models.CatalogDAO;
import dao.models.QualityAttributeDAO;
import models.template.*;

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
        List<QA> qas = qaDAO.readAll();
        return qas;
    }

    public static List<QA> getQAsByCatalog(long id) {
        QualityAttributeDAO qaDAO = new QualityAttributeDAO();
        CatalogDAO catalogDAO = new CatalogDAO();
        models.template.Catalog cat = catalogDAO.readById(id);
        List<QA> qas = qaDAO.findByCatalog(cat);
        return qas;
    }
}