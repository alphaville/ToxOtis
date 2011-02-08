package org.opentox.toxotis.database.pool;

import java.sql.Connection;
import java.sql.SQLException;
import junit.framework.TestCase;
import org.opentox.toxotis.database.LoginInfo;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author chung
 */
public class DataSourceFactoryTest extends TestCase {

    public DataSourceFactoryTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSomeMethod() {
        try {
            LoginInfo li = new LoginInfo();
            li.setPassword("opensess@me");
            DataSourceFactory factory = DataSourceFactory.getInstance();
            String connectionUri = factory.getConnectionURI(li);
            System.out.println(connectionUri);

            Connection connection = factory.getDataSource(connectionUri).getConnection();

            assertNotNull(connection);
            assertTrue(factory.ping(li, 5));

        } catch (DbException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (SQLException ex) {
            ex.printStackTrace();
            fail("Database is inaccessible!");
        }
    }
}
