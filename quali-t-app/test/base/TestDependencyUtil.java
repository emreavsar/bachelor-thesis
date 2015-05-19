package base;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import logics.authentication.Authenticator;

/**
 * Created by emre on 19/05/15.
 */
public class TestDependencyUtil {
    public static Injector createInjector() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Authenticator.class);
            }
        });
        return injector;
    }
}