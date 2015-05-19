package unit.logic;


import base.AbstractDatabaseTest;
import controllers.Helper;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class HelperLogicTest extends AbstractDatabaseTest {

    private models.project.Customer customer;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        customer = new models.project.Customer("Name", "Address");
    }

    @Test
    public void testValidateNull() {
        assertThat(Helper.validate(null)).isFalse();
    }

    @Test
    public void testValidateEmtpyString() {
        assertThat(Helper.validate("")).isFalse();
    }

    @Test
    public void testValidateValidString() {
        assertThat(Helper.validate("Customer Name")).isTrue();
    }
}