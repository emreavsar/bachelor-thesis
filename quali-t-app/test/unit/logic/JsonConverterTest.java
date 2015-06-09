package unit.logic;

import base.AbstractDatabaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import models.Interface.JIRAConnection;
import models.authentication.User;
import models.project.Project;
import models.project.ProjectInitiator;
import models.project.QualityProperty;
import models.project.nfritem.Instance;
import models.project.nfritem.QualityPropertyStatus;
import models.project.nfritem.Val;
import models.template.*;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import util.JsonConverter;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by corina on 08.06.2015.
 */
public class JsonConverterTest extends AbstractDatabaseTest {

    private JsonConverter jsonConverter;
    private String jsonString;
    private JsonNode jsonToConvert;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        jsonConverter = getInjector().getInstance(JsonConverter.class);
    }

    @Test
    public void testGetQaFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"id\": 1,\n" +
                "  \"qa\": {\n" +
                "    \"description\": \"<p>QA Test.</p>\"\n" +
                "  }\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        QA qa = jsonConverter.getQaFromJson(jsonToConvert);
        // ASSERT
        assertThat(qa.getId()).isEqualTo(1);
        assertThat(qa.getDescription()).isEqualTo("<p>QA Test.</p>");
    }

    @Test
    public void testGetProjectInitiatorFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"id\": -3001,\n" +
                "  \"name\": \"HSR\",\n" +
                "  \"address\": \"Rapperswil\"\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        ProjectInitiator projectInitiator = jsonConverter.getProjectInitiatorFromJson(jsonToConvert);
        // ASSERT
        assertThat(projectInitiator.getId()).isEqualTo(-3001);
        assertThat(projectInitiator.getName()).isEqualTo("HSR");
        assertThat(projectInitiator.getAddress()).isEqualTo("Rapperswil");
    }

    @Test
    public void testGetCatalogFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"id\": -6001,\n" +
                "  \"name\": \"Cloud Catalog\",\n" +
                "  \"description\": \"Catalog for Cloud Templates\",\n" +
                "  \"image\": \"test.jpg\"\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        Catalog catalog = jsonConverter.getCatalogFromJson(jsonToConvert);
        // ASSERT
        assertThat(catalog.getId()).isEqualTo(-6001);
        assertThat(catalog.getName()).isEqualTo("Cloud Catalog");
        assertThat(catalog.getDescription()).isEqualTo("Catalog for Cloud Templates");
        assertThat(catalog.getImage()).isEqualTo("test.jpg");
    }

    @Test
    public void testGetCatalogQasFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"selectedQualityAttributes\": [\n" +
                "    {\n" +
                "      \"id\": -2011\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": -2000\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 53\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        List<Long> catalogQaIds = new ArrayList<>();
        List<CatalogQA> catalogQAList = jsonConverter.getCatalogQasFromJson(jsonToConvert);
        // ASSERT
        for (CatalogQA catalogQA : catalogQAList) {
            catalogQaIds.add(catalogQA.getQa().getId());
        }
        assertThat(catalogQaIds).containsExactly(Long.valueOf(-2011), Long.valueOf(-2000), Long.valueOf(53));
    }

    @Test
    public void testGetCatalogQaFromJsonWithVars() {
        List<Integer> intVarList = new ArrayList<>();
        List<Integer> intValList = new ArrayList<>();
        jsonString = "{\n" +
                "  \"qa\": {\n" +
                "    \"id\": -2000\n" +
                "  },\n" +
                "  \"catalogQa\": {\n" +
                "    \"id\": 59,\n" +
                "    \"deleted\": false,\n" +
                "    \"variables\": [\n" +
                "      {\n" +
                "        \"type\": \"FREETEXT\",\n" +
                "        \"varIndex\": 0,\n" +
                "        \"id\": 60,\n" +
                "        \"extendable\": false\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"ENUMNUMBER\",\n" +
                "        \"varIndex\": 1,\n" +
                "        \"defaultValue\": \"100\",\n" +
                "        \"values\": [\n" +
                "          \"99\",\n" +
                "          \"90\",\n" +
                "          \"95\",\n" +
                "          \"100\"\n" +
                "        ],\n" +
                "        \"extendable\": true,\n" +
                "        \"min\": 4,\n" +
                "        \"max\": 55\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        CatalogQA catalogQA = jsonConverter.getCatalogQaFromJson(jsonToConvert);
        // ASSERT
        assertThat(catalogQA.getQa().getId()).isEqualTo(-2000);
        assertThat(catalogQA.getId()).isEqualTo(59);
        assertThat(catalogQA.getVariables().size()).isEqualTo(2);
        for (QAVar qaVar : catalogQA.getVariables()) {
            if (qaVar.getVarIndex() == 0) {
                assertThat(qaVar.getType()).isEqualTo(QAType.FREETEXT);
                intVarList.add(qaVar.getVarIndex());
            }
            if (qaVar.getVarIndex() == 1) {
                assertThat(qaVar.getType()).isEqualTo(QAType.ENUMNUMBER);
                assertThat(qaVar.getValRange().getMin()).isEqualTo(4);
                assertThat(qaVar.getValRange().getMax()).isEqualTo(55);
                assertThat(qaVar.isExtendable()).isTrue();
                assertThat(qaVar.getDefaultValue().getValue()).isEqualTo("100");
                for (QAVarVal qaVarVal : qaVar.getValues()) {
                    intValList.add(Integer.parseInt(qaVarVal.getValue()));
                    assertThat(qaVarVal.getType()).isEqualTo(ValueType.NUMBER);
                }
                intVarList.add(qaVar.getVarIndex());
            }
        }
        assertThat(intValList).containsOnly(90, 95, 99, 100);
        assertThat(intVarList).containsOnly(0, 1);
    }

    @Test
    public void testGetProjectFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"name\": \"My Project\",\n" +
                "  \"id\": 555,\n" +
                "  \"jiraKey\": \"QTP\",\n" +
                "  \"jiraConnection\": {\n" +
                "    \"id\": -1500\n" +
                "  },\n" +
                "  \"projectInitiator\": -3000,\n" +
                "  \"additionalQualityAttributes\": [\n" +
                "    {\n" +
                "      \"description\": \"<p>Simple QA without vars.</p>\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        Project project = jsonConverter.getProjectFromJson(jsonToConvert);
        // ASSERT
        assertThat(project.getId()).isEqualTo(555);
        assertThat(project.getName()).isEqualTo("My Project");
        assertThat(project.getJiraKey()).isEqualTo("QTP");
        assertThat(project.getJiraConnection().getId()).isEqualTo(-1500);
        assertThat(project.getProjectInitiator().getId()).isEqualTo(-3000);
        assertThat(project.getQualityAttributes().size()).isEqualTo(1);
        for (Instance instance : project.getQualityAttributes()) {
            assertThat(instance.getDescription()).isEqualTo("<p>Simple QA without vars.</p>");
        }
    }

    @Test
    public void testGetQualityPropertiesFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"qualityProperties\": [\n" +
                "    {\n" +
                "      \"id\": -8000,\n" +
                "      \"name\": \"S\",\n" +
                "      \"description\": \"Specific\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": -8002,\n" +
                "      \"name\": \"A\",\n" +
                "      \"description\": \"Agreed Upon\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": -8001,\n" +
                "      \"name\": \"M\",\n" +
                "      \"description\": \"Measurable\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        List<Long> qualityPropertyIds = jsonConverter.getQualityPropertiesFromJson(jsonToConvert);
        // ASSERT
        assertThat(qualityPropertyIds).containsExactly(Long.valueOf(-8000), Long.valueOf(-8002), Long.valueOf(-8001));
    }

    @Test
    public void testGetQualityAttributeIdsFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"qualityAttributes\": [\n" +
                "    {\n" +
                "      \"id\": -7000,\n" +
                "      \"description\": \"<p>Das %VARIABLE_FREETEXT_0% ist zu %VARIABLE_ENUMNUMBER_1%% verfuegbar.</p>\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": -7012,\n" +
                "      \"description\": \"<p>Dieses QA hat einen Range von 0 bis 100 %VARIABLE_FREENUMBER_0%</p>\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        List<Long> qualityAttributeIds = jsonConverter.getQualityAttributeIdsFromJson(jsonToConvert);
        // ASSERT
        assertThat(qualityAttributeIds).containsExactly(Long.valueOf(-7000), Long.valueOf(-7012));
    }

    @Test
    public void testGetInstanceFromJson() {
        // ARRANGE
        List<Integer> intVarList = new ArrayList<>();
        jsonString = "{\n" +
                "  \"id\": 58,\n" +
                "  \"description\": \"<p>Das %VARIABLE_FREETEXT_0% ist zu %VARIABLE_ENUMNUMBER_1%% verfuegbar.</p>\",\n" +
                "  \"values\": [\n" +
                "    {\n" +
                "      \"varIndex\": 0,\n" +
                "      \"value\": \"System\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"varIndex\": 1,\n" +
                "      \"value\": \"90\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        Instance instance = jsonConverter.getInstanceFromJson(jsonToConvert);
        // ASSERT
        assertThat(instance.getDescription()).isEqualTo("<p>Das %VARIABLE_FREETEXT_0% ist zu %VARIABLE_ENUMNUMBER_1%% verfuegbar.</p>");
        assertThat(instance.getValues().size()).isEqualTo(2);
        for (Val val : instance.getValues()) {
            if (val.getVarIndex() == 0) {
                assertThat(val.getValue()).isEqualTo("System");
                intVarList.add(val.getVarIndex());
            }
            if (val.getVarIndex() == 1) {
                assertThat(val.getValue()).isEqualTo("90");
                intVarList.add(val.getVarIndex());
            }
        }
        assertThat(intVarList).containsOnly(1, 0);
    }

    @Test
    public void testGetCompleteProjectFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"id\": 57,\n" +
                "  \"name\": \"My Project\",\n" +
                "  \"jiraKey\": \"QTP\",\n" +
                "  \"jiraConnection\": {\n" +
                "    \"id\": -1500\n" +
                "  },\n" +
                "  \"projectInitiator\": -3000,\n" +
                "  \"qualityAttributes\": [\n" +
                "    {\n" +
                "      \"id\": 58,\n" +
                "      \"description\": \"<p>Simple QA without vars.</p>\",\n" +
                "      \"qualityPropertyStatus\": [\n" +
                "        {\n" +
                "          \"id\": 60,\n" +
                "          \"qp\": {\n" +
                "            \"id\": -8001,\n" +
                "            \"name\": \"M\",\n" +
                "            \"description\": \"Measurable\"\n" +
                "          },\n" +
                "          \"status\": true\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        Project project = jsonConverter.getCompleteProjectFromJson(jsonToConvert);
        // ASSERT
        assertThat(project.getId()).isEqualTo(57);
        assertThat(project.getName()).isEqualTo("My Project");
        assertThat(project.getJiraKey()).isEqualTo("QTP");
        assertThat(project.getJiraConnection().getId()).isEqualTo(-1500);
        assertThat(project.getProjectInitiator().getId()).isEqualTo(-3000);
        assertThat(project.getQualityAttributes().size()).isEqualTo(1);
        for (Instance instance : project.getQualityAttributes()) {
            assertThat(instance.getId()).isEqualTo(58);
            for (QualityPropertyStatus qualityPropertyStatus : instance.getQualityPropertyStatus()) {
                assertThat(qualityPropertyStatus.getQa().getId()).isEqualTo(58);
                assertThat(qualityPropertyStatus.getQp().getId()).isEqualTo(-8001);
                assertThat(qualityPropertyStatus.getId()).isEqualTo(60);
                assertThat(qualityPropertyStatus.isStatus()).isTrue();
            }
        }
    }

    @Test
    public void testGetQualityPropertyFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"id\": -8001,\n" +
                "  \"name\": \"M\",\n" +
                "  \"description\": \"Measurable\"\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        QualityProperty qualityProperty = jsonConverter.getQualityPropertyFromJson(jsonToConvert);
        // ASSERT
        assertThat(qualityProperty.getId()).isEqualTo(-8001);
        assertThat(qualityProperty.getDescription()).isEqualTo("Measurable");
        assertThat(qualityProperty.getName()).isEqualTo("M");
    }

    @Test
    public void testGetCategoryFromJson() {
        // ARRANGE
        jsonString = "[\n" +
                "  {\n" +
                "    \"id\": -4005,\n" +
                "    \"name\": \"Availability\",\n" +
                "    \"icon\": \"fa fa-cog\",\n" +
                "    \"parent\": -4000\n" +
                "  }\n" +
                "]";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        QACategory qaCategory = jsonConverter.getCategoryFromJson(jsonToConvert);
        // ASSERT
        assertThat(qaCategory.getId()).isEqualTo(-4005);
        assertThat(qaCategory.getName()).isEqualTo("Availability");
        assertThat(qaCategory.getIcon()).isEqualTo("fa fa-cog");
        assertThat(qaCategory.getParent().getId()).isEqualTo(-4000);
    }

    @Test
    public void testJiraConnectionFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"id\": -1500,\n" +
                "  \"hostAddress\": \"http://sinv-56055.edu.hsr.ch:8080\",\n" +
                "  \"username\": \"user\",\n" +
                "  \"password\": \"password\"\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        JIRAConnection jiraConnection = jsonConverter.getJiraConnectionFromJson(jsonToConvert);
        // ASSERT
        assertThat(jiraConnection.getId()).isEqualTo(-1500);
        assertThat(jiraConnection.getHostAddress()).isEqualTo("http://sinv-56055.edu.hsr.ch:8080");
        assertThat(jiraConnection.getUsername()).isEqualTo("user");
        assertThat(jiraConnection.getPassword()).isEqualTo("password");
    }

    @Test
    public void testGetJiraProjectFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"projectId\": 52\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        Project project = jsonConverter.getJiraProjectFromJson(jsonToConvert);
        // ASSERT
        assertThat(project.getId()).isEqualTo(52);
    }

    @Test
    public void testGetQualityAttributeIdsToExportFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"selectedQas\": [\n" +
                "    53,\n" +
                "    54\n" +
                "  ]\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        List<Long> qaIds = jsonConverter.getQualityAttributeIdsToExportFromJson(jsonToConvert);
        // ASSERT
        assertThat(qaIds).containsExactly(Long.valueOf(53), Long.valueOf(54));
    }

    @Test
    public void testGetExportAsRawBoolean() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"exportAsRaw\": true\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        Boolean exportAsRawBoolean = jsonConverter.getExportAsRawBoolean(jsonToConvert);
        // ASSERT
        assertThat(exportAsRawBoolean).isTrue();
    }

    @Test
    public void testGetUserFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"id\": -1003,\n" +
                "  \"username\": \"user\",\n" +
                "  \"password\": \"password\",\n" +
                "  \"token\": \"token\"\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        User user = jsonConverter.getUserFromJson(jsonToConvert);
        // ASSERT
        assertThat(user.getId()).isEqualTo(-1003);
        assertThat(user.getName()).isEqualTo("user");
        assertThat(user.getHashedPassword()).isEqualTo("password");
        assertThat(user.getToken().get(0).getToken()).isEqualTo("token");
    }

    @Test
    public void testUserRolesFromJson() {
        // ARRANGE
        jsonString = "{\n" +
                "  \"selectedRoles\": [\n" +
                "    -20000,\n" +
                "    -20001\n" +
                "  ]\n" +
                "}";
        jsonToConvert = Json.parse(jsonString);
        // ACT
        List<Long> roleIds = jsonConverter.getUserRolesFromJson(jsonToConvert);
        // ASSERT
        assertThat(roleIds).containsExactly(Long.valueOf(-20000), Long.valueOf(-20001));
    }


}
