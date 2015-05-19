package unit.logic.project;


import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.project.Customer;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CustomerLogicTest extends AbstractDatabaseTest {

    private models.project.Customer customer;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        customer = new models.project.Customer("Name", "Address");
    }

    //createCustomer Tests
    @Test
    public void testCreateValidCustomer() throws MissingParameterException, EntityAlreadyExistsException, InterruptedException {
        // ACT
        models.project.Customer newCustomer = Customer.createCustomer(customer);
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
        Customer.createCustomer(newCustomer);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCustomerNullName() throws EntityAlreadyExistsException, MissingParameterException {
        // ARRANGE
        models.project.Customer newCustomer = new models.project.Customer(null, "Address");
        // ACT
        Customer.createCustomer(newCustomer);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateNullCustomer() throws EntityAlreadyExistsException, MissingParameterException {
        // ARRANGE
        models.project.Customer newCustomer = null;
        // ACT
        Customer.createCustomer(newCustomer);
        // ASSERT
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testCreateCustomerAlreadyExists() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        Customer.createCustomer(customer);
        // ACT
        Customer.createCustomer(customer);
        // ASSERT
    }

    //updateCustomer Tests

    @Test
    public void testUpdateValidCustomer() throws MissingParameterException, EntityAlreadyExistsException, EntityNotFoundException {
        // ACT
        models.project.Customer originalCustomer = AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        customer.setId(originalCustomer.getId());
        models.project.Customer updatedCustomer = Customer.updateCustomer(customer);
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
        Customer.updateCustomer(customer);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateCustomerNullName() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        models.project.Customer originalCustomer = AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        customer.setId(originalCustomer.getId());
        customer.setName(null);
        // ACT
        Customer.updateCustomer(customer);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateNullCustomer() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        models.project.Customer customer = null;
        // ACT
        Customer.updateCustomer(customer);
        // ASSERT
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testUpdateCustomerAlreadyExists() throws MissingParameterException, EntityNotFoundException, EntityAlreadyExistsException {
        // ARRANGE
        AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        models.project.Customer customer = AbstractTestDataCreator.createCustomer("Name", "Adress");
        customer.setName("Name original");
        // ACT
        Customer.updateCustomer(customer);
        // ASSERT
    }

    @Test(expected = MissingParameterException.class)
    public void testUpdateCustomerWithNullId() throws EntityAlreadyExistsException, EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        Customer.updateCustomer(customer);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateNonExistingCustomer() throws EntityAlreadyExistsException, MissingParameterException, EntityNotFoundException {
        // ARRANGE
        customer.setId(Long.parseLong("999999"));
        // ACT
        Customer.updateCustomer(customer);
        // ASSERT
    }

    @Test
    public void testDeletedValidcustomer() throws EntityNotFoundException, MissingParameterException {
        models.project.Customer customerToDelet = AbstractTestDataCreator.createCustomer("Name", "Adress");
        Customer.deleteCustomer(customerToDelet.getId());
        assertThat(Customer.getAllCustomers().contains(customerToDelet)).isFalse();
    }

    @Test(expected = MissingParameterException.class)
    public void testDeletedNullCustomer() throws EntityNotFoundException, MissingParameterException {
        // ARRANGE
        // ACT
        Customer.deleteCustomer(null);
        // ASSERT
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeletedNonExistingCustomer() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        // ACT
        Customer.deleteCustomer(Long.parseLong("999999"));
        // ASSERT
    }

}
