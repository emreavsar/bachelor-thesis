package integration;

import org.junit.Ignore;
import org.junit.Test;
import play.libs.F;
import play.test.Helpers;
import play.test.TestBrowser;

import java.util.concurrent.TimeUnit;

import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

/**
 * Created by emre on 02/04/15.
 */
public class AuthIntTest {

    @Test
    @Ignore
    // TODO emre: fix this integration test, it does not work atm
    public void testRegister() {
        running(testServer(3333, Helpers.fakeApplication()), HTMLUNIT, new F.Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333/app/index.html#/register");
                browser.fill("input-username").with("test-user");
                browser.fill("input-password").with("1234");
                browser.fill("input-passwordRepeated").with("1234");
                browser.click("#btn-register");
                browser.await().atMost(5, TimeUnit.SECONDS).until(".text-success").isPresent();
            }
        });
    }

}
