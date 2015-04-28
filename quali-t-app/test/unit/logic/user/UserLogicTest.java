package unit.logic.user;


import com.fasterxml.jackson.databind.JsonNode;
import exceptions.EntityNotCreatedException;
import models.authentication.User;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import base.AbstractDatabaseTest;
import base.AbstractTestDataCreator;

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
    public void testHashAndSaltFieldsNotReturned() throws EntityNotCreatedException {
        final JsonNode json = Json.toJson(user);

        assertThat(json.get("name").asText()).isEqualTo("test-user");

        // hashedPassword and salt should not be exposed in json
        assertThat(json.get("hashedPassword")).isNull();
        assertThat(json.get("salt")).isNull();
    }
}
