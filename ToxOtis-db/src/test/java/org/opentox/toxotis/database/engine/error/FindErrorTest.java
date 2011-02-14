package org.opentox.toxotis.database.engine.error;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.database.IDbIterator;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author chung
 */
public class FindErrorTest {

    public FindErrorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() throws DbException {
        FindError fe = new FindError(Services.ntua());
        fe.setPageSize(10);
        IDbIterator<ErrorReport> it = fe.list();
        while (it.hasNext()){
            System.out.println(it.next().getMessage());
        }
        it.close();
        fe.close();
    }

}