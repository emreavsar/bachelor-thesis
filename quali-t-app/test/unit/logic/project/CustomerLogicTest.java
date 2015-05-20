package unit.logic.project;


import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.project.CustomerLogic;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CustomerLogicTest extends AbstractDatabaseTest {

    private models.project.Customer customer;
    private CustomerLogic customerLogic;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        customer = new models.project.Customer("Name", "Address");
        customerLogic = getInjector().getInstance(CustomerLogic.class);
    }

    //createCustomer Tests
    @Test
    public void testCreateValidCustomer() throws MissingParameterException, EntityAlreadyExistsException, InterruptedException {
        // ACT
        models.project.Customer newCustomer = customerLogic.createCustomer(customer);
        // ASSERT
        assertThat(newCustomer.getId()).isNotNull();
        assertThat(newCustomer.getName()).isEqualTo("Name");
        assertThat(newCustomer.getAddress()).isEqualTo("Address");
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCustomerEmptyName() throws EntityAlreadyExistsException, MissingParameterException {
        // ARRANGE
        models.project.Customer newCustomer = new models.project.Customer("", "Address");
        // ACT
        customerLogic.createCustomer(newCustomer);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCustomerNullName() throws EntityAlreadyExistsException, MissingParameterException {
        // ARRANGE
        models.project.Customer newCustomer = new models.project.Customer(null, "Address");
        // ACT
        customerLogic.createCustomer(newCustomer);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateNullCustomer() throws EntityAlreadyExistsException, MissingParameterException {
        // ARRANGE
        models.project.Customer newCustomer = null;
        // ACT
        customerLogic.createCustomer(newCustomer);
        // ASSERT
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testCreateCustomerAlreadyExists() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        customerLogic.createCustomer(customer);
        // ACT
        customerLogic.createCustomer(customer);
        // ASSERT
    }

    //updateCustomer Tests

    @Test
    public void testUpdateValidCustomer() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ACT
        models.project.Customer originalCustomer = AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        customer.setId(originalCustomer.getId());
        models.project.Customer updatedCustomer = customerLogic.updateCustomer(customer);
        // ASSERT
        assertThat(updatedCustomer.getId()).isEqualTo(originalCustomer.getId());
        assertThat(updatedCustomer.getName()).isEqualTo("Name");
        assertThat(updatedCustomer.getAddress()).isEqualTo("Address");
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateCustomerEmptyName() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        models.project.Customer originalCustomer = AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        customer.setId(originalCustomer.getId());
        customer.setName("");
        // ACT
        customerLogic.updateCustomer(customer);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateCustomerNullName() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        models.project.Customer originalCustomer = AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        customer.setId(originalCustomer.getId());
        customer.setName(null);
        // ACT
        customerLogic.updateCustomer(customer);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateNullCustomer() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        models.project.Customer customer = null;
        // ACT
        customerLogic.updateCustomer(customer);
        // ASSERT
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testUpdateCustomerAlreadyExists() throws MissingParameterException, EntityNotFoundException, EntityAlreadyExistsException {
        // ARRANGE
        AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        models.project.Customer customer = AbstractTestDataCreator.createCustomer("Name", "Adress");
        customer.setName("Name original");
        // ACT
        customerLogic.updateCustomer(customer);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateCustomerWithNullId() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        customerLogic.updateCustomer(customer);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateNonExistingCustomer() throws EntityAlreadyExistsException, MissingParameterException, EntityNotFoundException {
        // ARRANGE
        customer.setId(Long.parseLong("999999"));
        // ACT
        customerLogic.updateCustomer(customer);
        // ASSERT
    }

    @Test
    public void testDeletedValidcustomer() throws EntityNotFoundException, MissingParameterException {
        models.project.Customer customerToDelet = AbstractTestDataCreator.createCustomer("Name", "Adress");
        customerLogic.deleteCustomer(customerToDelet.getId());
        assertThat(customerLogic.getAllCustomers().contains(customerToDelet)).isFalse();
    }

    @Test(expected = MissingParameterException.class)
    public void testDeletedNullCustomer() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        customerLogic.deleteCustomer(null);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeletedNonExistingCustomer() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        customerLogic.deleteCustomer(Long.parseLong("999999"));
        // ASSERT
    }

}
