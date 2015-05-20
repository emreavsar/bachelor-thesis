package unit.daos.project;

import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import dao.models.CustomerDAO;
import models.project.Customer;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


/**
 * Created by emre on 28/04/15.
 */
public class CustomerDAOTest extends AbstractDatabaseTest {

    private CustomerDAO customerDAO;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        customerDAO = getInjector().getInstance(CustomerDAO.class);
    }

    @Test
    public void findByNameTest() throws Throwable {
        // ARRANGE
        Customer persistedCustomer = AbstractTestDataCreator.createCustomer("IFS", "Rapperswil");
        // ACT
        Customer customer = customerDAO.findByName("IFS");
        // ASSERT
        assertThat(customer.getId()).isEqualTo(persistedCustomer.getId());
        assertThat(customer.getName()).isEqualTo("IFS");
        assertThat(customer.getAddress()).isEqualTo("Rapperswil");
    }
}
