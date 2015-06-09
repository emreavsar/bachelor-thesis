package unit.logic.search;

import base.AbstractDatabaseTest;
import exceptions.EntityNotFoundException;
import logics.search.SearchLogic;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by emre on 20/05/15.
 */
public class SearchLogicTest extends AbstractDatabaseTest {
    SearchLogic searchLogic;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        searchLogic = getInjector().getInstance(SearchLogic.class);
    }

    @Test
    public void testSearch() throws EntityNotFoundException {
        searchLogic.search("cloud");

    }
}
