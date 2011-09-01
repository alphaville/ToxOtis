/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.database.engine.bibtex;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.database.IDbIterator;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author chung
 */
public class CountBibTeXTest {

    public CountBibTeXTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testCountBibTeX() throws DbException {
        CountBibTeX counter = new CountBibTeX();
        int count = counter.count();
        assertTrue(count >= 0);
        counter.close();
        ListBibTeX lister = new ListBibTeX();
        IDbIterator<String> iterator = lister.list();
        int i = 0;
        while (iterator.hasNext()) {
            i++;
        }
        assertEquals(i, count);
    }
}
