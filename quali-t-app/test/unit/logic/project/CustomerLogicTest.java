package unit.logic.project;


import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.project.Customer;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CustomerLogicTest extends AbstractDatabaseTest {

    private models.project.Customer customer;

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

    @Test
    public void testCreateCustomerEmptyName() throws EntityAlreadyExistsException {
        // ARRANGE
        models.project.Customer newCustomer = new models.project.Customer("", "Address");
        boolean thrown = false;
        // ACT
        try {
            Customer.createCustomer(newCustomer);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown);
    }

    @Test
    public void testCreateCustomerNullName() throws EntityAlreadyExistsException {
        // ARRANGE
        models.project.Customer newCustomer = new models.project.Customer(null, "Address");
        boolean thrown = false;
        // ACT
        try {
            Customer.createCustomer(newCustomer);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown);
    }

    @Test
    public void testCreateNullCustomer() throws EntityAlreadyExistsException {
        // ARRANGE
        models.project.Customer newCustomer = null;
        boolean thrown = false;
        // ACT
        try {
            Customer.createCustomer(newCustomer);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown);
    }

    @Test
    public void testCreateCustomerAlreadyExists() throws MissingParameterException, EntityAlreadyExistsException {
        // ARRANGE
        boolean thrown = false;
        Customer.createCustomer(customer);
        // ACT
        try {
            Customer.createCustomer(customer);
        } catch (EntityAlreadyExistsException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown);
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

    @Test
    public void testUpdateCustomerEmptyName() throws EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        models.project.Customer originalCustomer = AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        customer.setId(originalCustomer.getId());
        customer.setName("");
        boolean thrown = false;
        // ACT
        try {
            Customer.updateCustomer(customer);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown);
    }

    @Test
    public void testUpdateCustomerNullName() throws EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        models.project.Customer originalCustomer = AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        customer.setId(originalCustomer.getId());
        customer.setName(null);
        boolean thrown = false;
        // ACT
        try {
            Customer.updateCustomer(customer);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown);
    }

    @Test
    public void testUpdateNullCustomer() throws EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        models.project.Customer customer = null;
        boolean thrown = false;
        // ACT
        try {
            Customer.updateCustomer(customer);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown);
    }

    @Test
    public void testUpdateCustomerAlreadyExists() throws MissingParameterException, EntityNotFoundException {
        // ARRANGE
        AbstractTestDataCreator.createCustomer("Name original", "Adress original");
        models.project.Customer customer = AbstractTestDataCreator.createCustomer("Name", "Adress");
        customer.setName("Name original");
        boolean thrown = false;
        // ACT
        try {
            Customer.updateCustomer(customer);
        } catch (EntityAlreadyExistsException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown);
    }

    @Test
    public void testUpdateCustomerWithNullId() throws EntityAlreadyExistsException, EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            Customer.updateCustomer(customer);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown);
    }

    @Test
    public void testUpdateNonExistingCustomer() throws EntityAlreadyExistsException, MissingParameterException {
        // ARRANGE
        boolean thrown = false;
        customer.setId(Long.parseLong("999999"));
        // ACT
        try {
            Customer.updateCustomer(customer);
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown);
    }

    @Test
    public void testDeletedValidcustomer() throws EntityNotFoundException, MissingParameterException {
        models.project.Customer customerToDelet = AbstractTestDataCreator.createCustomer("Name", "Adress");
        Customer.deleteCustomer(customerToDelet.getId());
        assertThat(Customer.getAllCustomers().contains(customerToDelet)).isFalse();
    }

    @Test
    public void testDeletedNullCustomer() throws EntityNotFoundException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            Customer.deleteCustomer(null);
        } catch (MissingParameterException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown);
    }

    @Test
    public void testDeletedNonExistingCustomer() throws MissingParameterException {
        // ARRANGE
        boolean thrown = false;
        // ACT
        try {
            Customer.deleteCustomer(Long.parseLong("999999"));
        } catch (EntityNotFoundException e) {
            thrown = true;
        }
        // ASSERT
        assertThat(thrown);
    }

}
