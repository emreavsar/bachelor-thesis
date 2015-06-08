package unit.logic;


import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import com.google.common.collect.Lists;
import exceptions.EntityNotFoundException;
import models.project.nfritem.Instance;
import models.project.nfritem.Val;
import org.junit.Before;
import org.junit.Test;
import util.Helper;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class HelperLogicTest extends AbstractDatabaseTest {
    private Helper helper;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        helper = getInjector().getInstance(Helper.class);
    }

    @Test
    public void testValidateNull() {
        assertThat(helper.validate(null)).isFalse();
    }

    @Test
    public void testValidateEmtpyString() {
        assertThat(helper.validate("")).isFalse();
    }

    @Test
    public void testValidateValidString() {
        assertThat(helper.validate("Customer Name")).isTrue();
    }

    @Test
    public void testRemoveHTMLTags() {
        // ARRANGE
        String textWithHTMLTags = "<p>Das %VARIABLE_FREETEXT_0% ist zu %VARIABLE_ENUMNUMBER_1%% verfügbar.</p>";
        // ACT
        String textWithoutHTMLTags = helper.removeHtmlTags(textWithHTMLTags);
        // ASSERT
        assertThat(textWithHTMLTags).doesNotContain("<p>");
        assertThat(textWithHTMLTags).doesNotContain("</p>");
        assertThat(textWithHTMLTags).isEqualTo("Das %VARIABLE_FREETEXT_0% ist zu %VARIABLE_ENUMNUMBER_1%% verfügbar.");
    }

    @Test
    public void testParseLongFunction() {
        // ARRANGE
        List<String> stringList = new ArrayList<>();
        stringList.add("1");
        stringList.add("2");
        // ACT
        List<Long> longList = Lists.transform(stringList, helper.parseLongFunction());
        // ASSERT
        assertThat(longList).containsExactly(Long.parseLong("1"), Long.parseLong("2"));
    }

    @Test
    public void testGetValidDescirptionWithVars() throws EntityNotFoundException {
        // ARRAGE
        String textWihtoutVars = "Das %VARIABLE_FREETEXT_0% ist zu %VARIABLE_ENUMNUMBER_1%% verfügbar.";
        List<Val> valueList = new ArrayList<>();
        valueList.add(new Val(0, "System"));
        valueList.add(new Val(1, "99"));
        Instance instance = AbstractTestDataCreator.createInstanceWithVars(textWihtoutVars, valueList);
        // ACT
        String textWithVars = helper.getDescriptionWithVars(instance);
        // ASSERT
        textWithVars.equals("Das System ist zu 99% verfügbar.");
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetDescirptionWithMissingVar() throws EntityNotFoundException {
        // ARRAGE
        String textWihtoutVars = "Das %VARIABLE_FREETEXT_0% ist zu %VARIABLE_ENUMNUMBER_1%% verfügbar.";
        List<Val> valueList = new ArrayList<>();
        valueList.add(new Val(1, "99"));
        Instance instance = AbstractTestDataCreator.createInstanceWithVars(textWihtoutVars, valueList);
        // ACT
        String textWithVars = helper.getDescriptionWithVars(instance);
        // ASSERT
    }
}