package dao.models;

import dao.AbstractDAO;
import models.template.CatalogQA;
import models.template.QAType;
import models.template.QAVar;
import play.db.jpa.JPA;

/**
 * Created by emre on 30.05.2015.
 */
public class QAVarDAO extends AbstractDAO<QAVar> {

    /**
     * Does the update of statistics in QAVar (look for mostUsedValue and averageValue)
     *
     * @param catalog
     */
    public void updateStatistic(CatalogQA catalog) {
        // update each variable of the catalogqa with statistics
        for (QAVar var : catalog.getVariables()) {
            // average suits only for number values
            if (var.getType().equals(QAType.FREENUMBER) || var.getType().equals(QAType.ENUMNUMBER)) {
                JPA.em().createNativeQuery(
                        "update qavar" +
                                "  set" +
                                "    averagevalue=(" +
                                "       select sum(CAST(value as DECIMAL(9,2)))/count(value) from val " +
                                "           inner join instance " +
                                "               on val.instance_id=instance.id" +
                                "           where varindex=:varindex and template_id=:templateId" +
                                "       )" +
                                "       where id=:qavarid")
                        .setParameter("qavarid", var.getId())
                        .setParameter("templateId", var.getTemplate().getId())
                        .setParameter("varindex", var.getVarIndex())
                        .executeUpdate();
            }

            // most used value suits for all variable types
            // TODO emre: atm this can be improved when there is the same value used twice the first one is used -> no value should be used then
            JPA.em().createNativeQuery(
                    "update qavar" +
                            "  set" +
                            "    mosteusedvalue=(" +
                            "       select value from val " +
                            "           inner join instance " +
                            "               on val.instance_id=instance.id" +
                            "           where varindex=:varindex and template_id=:templateId" +
                            "           group by value" +
                            "           ORDER BY count(value) DESC" +
                            "           limit 1" +
                            "       )" +
                            "       where id=:qavarid")
                    .setParameter("qavarid", var.getId())
                    .setParameter("templateId", var.getTemplate().getId())
                    .setParameter("varindex", var.getVarIndex())
                    .executeUpdate();
        }
    }
}