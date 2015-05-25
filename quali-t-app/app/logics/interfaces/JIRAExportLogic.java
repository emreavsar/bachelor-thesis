package logics.interfaces;

/**
 * Created by corina on 12.05.2015.
 */


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import dao.models.ProjectDAO;
import dao.models.QAInstanceDAO;
import exceptions.EntityNotFoundException;
import exceptions.InvalidConnectionParameter;
import exceptions.MissingParameterException;
import logics.Helper;
import models.Interface.JIRAConnection;
import models.project.Project;
import models.project.nfritem.Instance;
import play.libs.F;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSRequestHolder;

import java.util.List;


public class JIRAExportLogic {
    @Inject
    private ProjectDAO projectDAO;
    @Inject
    private QAInstanceDAO qaInstanceDAO;
    @Inject
    private Helper helper;
    private String jiraAPIAddress;

    public JsonNode exportToJira(Project project, List<Long> qualityAttributesToExport, Boolean exportAsRaw) throws EntityNotFoundException, InvalidConnectionParameter, MissingParameterException {
        if (project != null && qualityAttributesToExport != null && exportAsRaw != null) {
            //get required parameters
            project = projectDAO.readById(project.getId());
            JIRAConnection connectionParameter = project.getJiraConnection();
            ObjectNode responses = new ObjectNode(JsonNodeFactory.instance);

            //create each QA as JIRA issue
            for (Long id : qualityAttributesToExport) {
                Instance qa = qaInstanceDAO.readById(id);
                JsonNode exportJson = parseJson(project, qa, exportAsRaw);
                //connect to JIRA connection and POST JSON
                try {
                    WSRequestHolder request = WS.url(connectionParameter.getHostAddress() + jiraAPIAddress);

                    WSRequestHolder complexRequest = request.setAuth(connectionParameter.getUsername(), connectionParameter.getPassword(), WSAuthScheme.BASIC)
                            .setContentType("application/json");
                    F.Promise<JsonNode> jsonResponse = complexRequest.post(exportJson).map(response -> response.asJson());
                    JsonNode createIssueResponse = jsonResponse.get(10000);

                    //check if there was an error during creating of issue
                    if (createIssueResponse.has("errors")) {
                        throw new InvalidConnectionParameter("Please check your JIRA Key Parameter");
                    }

                    //persist JIRA Issue Key and Direct URL to database if it was new created
                    if (qa.getJiraKey() == null || qa.getJiraKey().isEmpty()) {
                        qa.setJiraKey(createIssueResponse.findPath("key").asText());
                        qa.setJiraDirectURL(createIssueResponse.findPath("self").asText());
                        qaInstanceDAO.update(qa);
                    }

                    //add jira Response to Json for final response with all QAs
                    ((ObjectNode) responses).put(qa.getId().toString(), createIssueResponse);
                } catch (InvalidConnectionParameter e) {
                    throw e;
                } catch (Exception e) {
                    throw new InvalidConnectionParameter("Could not connect to the selected JIRA-Connection!");
                }
            }
            return responses;
        }
        throw new MissingParameterException("Please provide all required Parameters!");

    }

    private JsonNode parseJson(Project project, Instance qa, Boolean exportAsRaw) throws EntityNotFoundException {
        //replace vars with values
        String text = helper.getDescriptionWithVars(qa);
        //check if export should be raw or html
        if (!exportAsRaw) {
            text = helper.removeHtmlTags(text);
        }
        // generate JSON
        if (qa.getJiraKey() == null || qa.getJiraKey().isEmpty()) {
            return parseNewJiraIssue(project, qa, text);
        }
        return parseJiraUpdateIssue(qa, text);
    }

    private JsonNode parseJiraUpdateIssue(Instance qa, String text) {
        jiraAPIAddress = "/rest/api/2/issue/" + qa.getJiraKey() + "/comment";
        //parse JSON for JIRA POST Update Comment
        return Json.parse("{\n" +
                "    \"body\": \"" + text + "\"\n" +
                "}");
    }

    private JsonNode parseNewJiraIssue(Project project, Instance qa, String text) {
        jiraAPIAddress = "/rest/api/latest/issue";
        //parse JSON for JIRA POST
        return Json.parse("{\n" +
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
    }
//
//    private String getDescriptionWithVars(Instance qualityAttributeInstance) throws EntityNotFoundException {
//        Set<Val> values = qualityAttributeInstance.getValues();
//        return replacePlaceholder(qualityAttributeInstance.getDescription(), values);
//    }
//
//    private String removeHtmlTags(String text) {
//
//        return Jsoup.parse(text).text();
//    }
//
//    private Val getVarValue(int i, Set<Val> values) throws EntityNotFoundException {
//        for (Val value : values) {
//            if (value.getVarIndex() == i) {
//                return value;
//            }
//        }
//        throw new EntityNotFoundException("Value with this varindex not found!");
//    }
//
//    private String replacePlaceholder(String text, Set<Val> values) throws EntityNotFoundException {
//        Pattern p = Pattern.compile("%VARIABLE_.*?([0-9]*)%");
//        Matcher m = p.matcher(text);
//
//        if (m.find()) {
//            Val value = getVarValue(Integer.parseInt(m.group(1)), values);
//            text = m.replaceFirst(value.getValue());
//            return replacePlaceholder(text, values);
//        }
//        return text;
//    }


}
