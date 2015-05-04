package logics.template;

import com.fasterxml.jackson.databind.JsonNode;
import dao.models.CatalogQADAO;
import models.template.*;
import play.Logger;

import java.util.*;

/**
 * Created by corina on 04.05.2015.
 */
public class Variables {
    public static CatalogQA addVarsToQA(CatalogQA catalogQA, JsonNode vars) {
        CatalogQADAO catalogQADAO = new CatalogQADAO();
        List<QAVar> qaVars = new ArrayList<>();
        Iterator<JsonNode> varIterator = vars.elements();
        Map<String, String> values = new HashMap<>();
        while (varIterator.hasNext()) {
            JsonNode var = varIterator.next();
            Iterator<Map.Entry<String, JsonNode>> parameters = var.fields();
            while (parameters.hasNext()) {
                Map.Entry<String, JsonNode> entry = parameters.next();
                values.put(entry.getKey(), entry.getValue().asText());
            }
            Logger.info("Value Map:    " + values.toString());
            qaVars.add(createVariable(Integer.parseInt(values.get("number")), values.get("type"), values));
        }
        catalogQA.addVars(qaVars);
        return catalogQADAO.persist(catalogQA);
    }

    public static QAVar createVariable(int varNumber, String type, Map<String, String> values) {
        QAVar var = new QAVar(varNumber);
        switch (type) {
            case "FREENUMBER":
                var = createValRange(values, var);
                var.setType(QAType.freeNumber);
                break;
            case "FREETEXT":
                var.setType(QAType.freeText);
                break;
            case "enumNumber":
                var = createValRange(values, var);
                var = createVariableValues(values, TYPE.number, var);
                var.setType(QAType.enumNumber);
                break;
            case "enumText":
                var = createVariableValues(values, TYPE.text, var);
                var.setType(QAType.enumText);
                break;
            default:
                break;
        }

        return var;

    }

    private static QAVar createVariableValues(Map<String, String> values, TYPE type, QAVar var) {
        List<QAVarVal> varVals = new ArrayList<>();
        String defaultValue = "";
        if (values.containsKey("default")) {
            defaultValue = values.get("default");
        }
        if (type.equals(TYPE.number)) {
            for (Map.Entry<String, String> val : values.entrySet()) {
                QAVarVal value = new NumberVarVal(Float.parseFloat(val.getValue()));
                if (checkDefaultVal(defaultValue, val.getValue())) {
                    var.setDefaultVal(value);
                }
                varVals.add(value);
            }
        } else {
            for (Map.Entry<String, String> val : values.entrySet()) {
                QAVarVal value = new StringVarVal(val.getValue());
                if (checkDefaultVal(defaultValue, val.getValue())) {
                    var.setDefaultVal(value);
                }
                varVals.add(value);
            }
        }
        var.addValues(varVals);
        Logger.debug("var   " + var.toString());
        return var;
    }

    private static QAVar createValRange(Map<String, String> values, QAVar var) {
        Logger.info(Boolean.toString(values.containsKey("min")) + values.containsKey("max"));
        if (values.containsKey("min") && values.containsKey("max")) {
            var.setValRange(new ValRange(Float.parseFloat(values.get("min")), Float.parseFloat(values.get("max"))));
            values.remove("min");
            values.remove("max");
        } else {
        }
        return var;
    }

    public static boolean checkDefaultVal(String defaultValue, String value) {
        return defaultValue.equals(value);
    }

    private enum TYPE {number, text}
}
