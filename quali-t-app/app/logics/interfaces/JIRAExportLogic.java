package logics.interfaces;

/**
 * Created by corina on 12.05.2015.
 */


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import dao.interfaces.JIRAConnectionDAO;
import dao.models.ProjectDAO;
import dao.models.QAInstanceDAO;
import exceptions.EntityNotFoundException;
import models.Interface.JIRAConnection;
import models.project.Project;
import models.project.nfritem.Instance;
import models.project.nfritem.Val;
import org.jsoup.Jsoup;
import play.libs.F;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSRequestHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JIRAExportLogic {
    @Inject
    private JIRAConnectionDAO jiraConnectionDAO;
    @Inject
    private ProjectDAO projectDAO;
    @Inject
    private QAInstanceDAO qaInstanceDAO;

    public JsonNode exportToJira() throws EntityNotFoundException {
        JIRAConnection connectionParameter = getConnectionParameter(Long.parseLong("1500"));
        Project project = projectDAO.readById(Long.parseLong("11000"));
        List<Long> qaIds = new ArrayList<>();
        ObjectNode responses = new ObjectNode(JsonNodeFactory.instance);
//        getDescriptionWithVars(qaInstanceDAO.readById(Long.parseLong("5")));
        qaIds.add(Long.parseLong("5"));

        for (Long id : qaIds) {
            Instance qa = qaInstanceDAO.readById(id);
            String text = getDescriptionWithVars(qa);

            JsonNode jsonString = Json.parse("{\n" +
                    "    \"fields\": {\n" +
                    "       \"project\":\n" +
                    "       {\n" +
                    "          \"key\": \"" + project.getJiraKey() + "\"\n" +
                    "       },\n" +
                    "       \"summary\": \"Project " + project.getName() + " / QA " + qa.getId() + "\",\n" +
                    "       \"description\": \"" + text + "\",\n" +
                    "       \"issuetype\": {\n" +
                    "          \"name\": \"Task\"\n" +
                    "       }\n" +
                    "   }\n" +
                    "}");
            WSRequestHolder request = WS.url(connectionParameter.getHostAddress() + "/rest/api/latest/issue");

            WSRequestHolder complexRequest = request.setAuth(connectionParameter.getUsername(), connectionParameter.getPassword(), WSAuthScheme.BASIC)
                    .setContentType("application/json");
            F.Promise<JsonNode> jsonResponse = complexRequest.post(jsonString).map(response -> {
                return response.asJson();
            });
            JsonNode newNode = jsonResponse.get(10000);

            ((ObjectNode) responses).put(qa.getId().toString(), newNode);
        }
        return responses;

    }

    public JIRAConnection getConnectionParameter(Long id) throws EntityNotFoundException {
        return jiraConnectionDAO.readById(id);
    }

    public String getDescriptionWithVars(Instance qualityAttributeInstance) throws EntityNotFoundException {
        Set<Val> values = qualityAttributeInstance.getValues();

//            String number = qa.getDescription().matches("%(.*?)%");
//            String description = qa.getDescription().replaceFirst("%VARIABLE(.*?)%", "");

//        String description = .replaceFirst("%VARIABLE_.*?([0-9]*)%", "");
//        Logger.info(description);
        return removeHtmlTags(replacePlaceholder(qualityAttributeInstance.getDescription(), values));
    }

    private String removeHtmlTags(String text) {

        return Jsoup.parse(text).text();
    }

    private Val getVarValue(int i, Set<Val> values) throws EntityNotFoundException {
        for (Val value : values) {
            if (value.getVarIndex() == i) {
                return value;
            }
        }
        throw new EntityNotFoundException("Value with this varindex not found!");
    }

    private String replacePlaceholder(String text, Set<Val> values) throws EntityNotFoundException {
        Pattern p = Pattern.compile("%VARIABLE_.*?([0-9]*)%");
        Matcher m = p.matcher(text);

        if (m.find()) {
            Val value = getVarValue(Integer.parseInt(m.group(1)), values);
            text = m.replaceFirst(value.getValue());
            return replacePlaceholder(text, values);
        }
        return text;
    }


}
