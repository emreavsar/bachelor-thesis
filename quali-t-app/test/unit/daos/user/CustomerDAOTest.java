package unit.daos.user;

import dao.models.CustomerDAO;
import models.project.Customer;
import org.junit.Test;
import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;

import static org.fest.assertions.Assertions.assertThat;


/**
 * Created by emre on 28/04/15.
 */
public class CustomerDAOTest extends AbstractDatabaseTest {

    @Test
    public void findByNameTest() throws Throwable {
        AbstractTestDataCreator.createCustomer("IFS", "Rapperswil");

        Customer customer = new CustomerDAO().findByName("IFS");

        assertThat(customer.getName()).isEqualTo("IFS");
        assertThat(customer.getAddress()).isEqualTo("Rapperswil");

        // TODO: corina -> complete lines below
//        ArrayList<Project> custProjects = (ArrayList<Project>) customer.getProjects();
//        boolean hasCloudProject = false;
//        for(Project p : custProjects) {
//            if(p.getName().equals("Cloud")) {
//                hasCloudProject = true;
//            }
//        }
//
//        assertThat(hasCloudProject).isEqualTo(true);
    }
}
