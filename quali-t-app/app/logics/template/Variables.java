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
    static CatalogQADAO catalogQADAO = new CatalogQADAO();

    public static CatalogQA addVarsToQA(CatalogQA catalogQA, JsonNode json) {
        JsonNode vars = json.findValue("variables");
        List<QAVar> qaVars = new ArrayList<>();

//        for (JsonNode node : vars) {
        Iterator<JsonNode> varIterator = vars.elements();
        Map<String, String> variableParameters = new HashMap<>();
        List<String> variableValues = new ArrayList<>();
        JsonNode valueNode;
        while (varIterator.hasNext()) {
            JsonNode var = varIterator.next();
            valueNode = var.findValue("values");
//                variableValues = valueNode.findValuesAsText("");
            if (valueNode != null) {
                for (Iterator<JsonNode> textNode = valueNode.elements(); textNode.hasNext(); ) {
                    variableValues.add(textNode.next().asText());
                }
            }

            Iterator<Map.Entry<String, JsonNode>> parameters = var.fields();
            while (parameters.hasNext()) {
                Map.Entry<String, JsonNode> entry = parameters.next();
                if (entry.getKey().equals("variables")) {

                } else {
                    variableParameters.put(entry.getKey(), entry.getValue().asText());
                }
            }
            Logger.info("Value Map:    " + variableParameters.toString());
            if (!variableParameters.isEmpty()) {
                qaVars.add(createVariable(variableParameters, variableValues));
            }
            }

        catalogQA.addVars(qaVars);
        return catalogQADAO.update(catalogQA);
    }

    public static QAVar createVariable(Map<String, String> parameters, List<String> variableValues) {
        QAVar var = new QAVar(Integer.parseInt(parameters.get("number")));
        switch (parameters.get("type")) {
            case "FREENUMBER":
                var = createValRange(parameters, var);
                var.setType(QAType.freeNumber);
                break;
            case "FREETEXT":
                var.setType(QAType.freeText);
                break;
            case "ENUMNUMBER":
                var = createValRange(parameters, var);
                var = createVariableValues(parameters, ValueType.number, var, variableValues);
                var.setType(QAType.enumNumber);
                break;
            case "ENUMTEXT":
                var = createVariableValues(parameters, ValueType.text, var, variableValues);
                var.setType(QAType.enumText);
                break;
            default:
                break;
        }

        return var;

    }

    private static QAVar createVariableValues(Map<String, String> values, ValueType type, QAVar var, List<String> variableValues) {
        List<QAVarVal> varVals = new ArrayList<>();
        String defaultValue = "";
        for (String val : variableValues) {
            QAVarVal value = new QAVarVal(val, type);
            varVals.add(value);
        }

//            for (Map.Entry<String, String> val : values.entrySet()) {
//                QAVarVal value = new QAVarVal(val.getValue(), type);
//                if (checkDefaultVal(defaultValue, val.getValue())) {
//                    var.setDefaultValue(value);
//                }
//                varVals.add(value);
//            }

        var.addValues(varVals);
        Logger.debug("var   " + var.toString());
        if (values.containsKey("defaultValue")) {
            defaultValue = values.get("defaultValue");
            Logger.info("Default value:     " + defaultValue);
            var.setDefaultValue(defaultValue);
        }
        return var;
    }


    private static QAVar createValRange(Map<String, String> values, QAVar var) {
        Logger.info(Boolean.toString(values.containsKey("min")) + values.containsKey("max"));
        if (values.containsKey("min") && values.containsKey("max")) {
            var.setValRange(new ValRange(Float.parseFloat(values.get("min")), Float.parseFloat(values.get("max"))));
            values.remove("min");
            values.remove("max");
        }
        return var;
    }

    public static boolean checkDefaultVal(String defaultValue, String value) {
        return defaultValue.equals(value);
    }

}
