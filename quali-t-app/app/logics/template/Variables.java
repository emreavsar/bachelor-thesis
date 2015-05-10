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
        //get all nodes within variables node.
        JsonNode vars = json.findValue("variables");
        List<QAVar> qaVars = new ArrayList<>();

        //create MAP to get all VariableParameters
        Map<String, String> variableParameters = new HashMap<>();
        //create List to save all VariableValues from Parameters
        List<String> variableValues = new ArrayList<>();
        //create iterator for each variable
        for (Iterator<JsonNode> varIterator = vars.elements(); varIterator.hasNext(); ) {
            //clear all used fields
            variableParameters.clear();
            variableValues.clear();
            //get all VariableParameter nodes and push them to variableParameters Map
            Map.Entry<String, JsonNode> variableParametersEntry = null;
            for (Iterator<Map.Entry<String, JsonNode>> parameters = varIterator.next().fields(); parameters.hasNext(); ) {
                variableParametersEntry = parameters.next();
                //if Parameters is value node, put all values to variableValues List, otherwise put them to variableParameters map
                if (variableParametersEntry.getKey().equals("values")) {
                    JsonNode valueNode = variableParametersEntry.getValue();
                    if (valueNode != null) {
                        for (Iterator<JsonNode> textNode = valueNode.elements(); textNode.hasNext(); ) {
                            variableValues.add(textNode.next().asText());
                        }
                    }
                } else {
                    variableParameters.put(variableParametersEntry.getKey(), variableParametersEntry.getValue().asText());
                }
            }
            Logger.info("Value Map:    " + variableParameters.toString());
            //check if QA has any variables and create them
            if (!variableParameters.isEmpty()) {
                qaVars.add(createVariable(variableParameters, variableValues));
            }
        }

        catalogQA.addVars(qaVars);
        return catalogQADAO.update(catalogQA);
    }

    public static QAVar createVariable(Map<String, String> parameters, List<String> variableValues) {
        QAVar var = new QAVar(Integer.parseInt(parameters.get("varIndex")));
        switch (parameters.get("type")) {
            case "FREENUMBER":
                var = createValRange(parameters, var);
                var.setType(QAType.FREENUMBER);
                break;
            case "FREETEXT":
                var.setType(QAType.FREETEXT);
                break;
            case "ENUMNUMBER":
                var = createValRange(parameters, var);
                var = createVariableValues(parameters, ValueType.NUMBER, var, variableValues);
                var.setType(QAType.ENUMNUMBER);
                break;
            case "ENUMTEXT":
                var = createVariableValues(parameters, ValueType.TEXT, var, variableValues);
                var.setType(QAType.ENUMTEXT);
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
