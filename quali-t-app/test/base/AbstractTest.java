package base;

import com.google.inject.Injector;

/**
 * Credits: EEPPI Project
 */

public abstract class AbstractTest {
    private Injector injector = TestDependencyUtil.createInjector();

    public Injector getInjector() {
        return injector;
    }
}