/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.database.engine.error;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.database.IDbIterator;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author chung
 */
public class ListErrorTest {

    public ListErrorTest() {
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
        ListError list = new ListError();
        IDbIterator<String> it = list.list();
        while (it.hasNext()){
            System.out.println(it.next());
        }
        it.close();
        list.close();
    }

}