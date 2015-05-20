package unit.logic.search;

import base.AbstractDatabaseTest;
import base.AbstractTest;
import logics.search.Search;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by emre on 20/05/15.
 */
public class SearchLogicTest extends AbstractDatabaseTest {
    Search searchLogic;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        searchLogic = getInjector().getInstance(Search.class);
    }

    @Test
    public void testSearch() {
        searchLogic.search("cloud");

    }
}
