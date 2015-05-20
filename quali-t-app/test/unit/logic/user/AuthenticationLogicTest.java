package unit.logic.user;

import base.AbstractTest;
import com.google.inject.Inject;
import logics.authentication.Authenticator;
import models.authentication.Token;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by emre on 28/04/15.
 */
public class AuthenticationLogicTest extends AbstractTest {
    @Inject
    Authenticator authenticator;

    @Before
    public void setUp() throws Exception {
        authenticator = getInjector().getInstance(Authenticator.class);
    }

    @Test
    public void isTokenValidWithValidTokenTest() {
        LocalDateTime validUntil = LocalDateTime.now().plusDays(30);
        Token t = new Token("xxx", validUntil, null);
        assertThat(authenticator.isTokenValid(t)).isTrue();
    }

    @Test
    public void isTokenValidWithInvalidTokenTest() {
        LocalDateTime validUntil = LocalDateTime.now().minusDays(1);
        Token t = new Token("xxx", validUntil, null);
        assertThat(authenticator.isTokenValid(t)).isFalse();
    }
}
