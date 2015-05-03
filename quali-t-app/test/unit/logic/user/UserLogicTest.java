package unit.logic.user;


import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.EntityAlreadyExistsException;
import models.authentication.User;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;

import static org.fest.assertions.Assertions.assertThat;

public class UserLogicTest extends AbstractDatabaseTest {
    User user;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        user = AbstractTestDataCreator.createUser("test-user", "1234");
    }

    @Test
    public void testHashAndSaltFieldsNotReturned() throws EntityAlreadyExistsException {
        final JsonNode json = Json.toJson(user);

        assertThat(json.get("name").asText()).isEqualTo("test-user");

        // hashedPassword and salt should not be exposed in json
        assertThat(json.get("hashedPassword")).isNull();
        assertThat(json.get("salt")).isNull();
    }
}
