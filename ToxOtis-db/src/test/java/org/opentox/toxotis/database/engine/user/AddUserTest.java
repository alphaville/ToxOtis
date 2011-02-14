

package org.opentox.toxotis.database.engine.user;

import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.core.component.User;

import static org.junit.Assert.*;


/**
 *
 * @author chung
 */
public class AddUserTest {

    public AddUserTest() {
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
    public void testSomeMethod() throws Exception {
        User mockUser = new User();
        mockUser.setHashedPass("x");
        mockUser.setMail(UUID.randomUUID().toString()+"a@b.net");
        mockUser.setName("abc");
        mockUser.setUid(UUID.randomUUID().toString());
        AddUser au = new AddUser(mockUser);
        au.write();
        au.close();

    }

}