package unit.logic;


import base.AbstractDatabaseTest;
import util.Helper;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class HelperLogicTest extends AbstractDatabaseTest {

    private models.project.Customer customer;
    private Helper helper;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        customer = new models.project.Customer("Name", "Address");
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
}