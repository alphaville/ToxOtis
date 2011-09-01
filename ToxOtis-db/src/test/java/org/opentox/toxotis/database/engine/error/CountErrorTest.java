/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.database.engine.error;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.DisableComponent;
import org.opentox.toxotis.database.engine.ROG;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author chung
 */
public class CountErrorTest {

    public CountErrorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testCountError() throws Exception {
        CountError counter = new CountError();
        int count = counter.count();
        assertTrue(count >= 0);
        counter.close();

        ListError lister = new ListError();
        int flag = 0;
        IDbIterator<String> iterator = lister.list();
        while (iterator.hasNext()) {
            flag++;
        }
        assertEquals(flag, count);
        iterator.close();
        lister.close();
    }

    @Test
    public void testCountError2() throws Exception {
        CountError counter = new CountError();
        int countBefore = counter.count();
        assertTrue(countBefore >= 0);
        counter.close();

        ErrorReport er = new ErrorReport(502, "sfd", "34gtdgdf", "23ffsg43tsgff", "bg");
        er.setMeta(null);
        er.setErrorCause(null);

        AddErrorReport adder = new AddErrorReport(er);
        assertEquals(2, adder.write());
        adder.close();

        DisableComponent disabler = new DisableComponent(er.getUri().getId());
        disabler.disable();
        disabler.close();

        counter = new CountError();
        int countAfter = counter.count();
        assertEquals(countBefore, countAfter);
        counter.close();

        disabler = new DisableComponent(er.getUri().getId());
        disabler.enable();
        disabler.close();

        counter = new CountError();
        countAfter = counter.count();
        assertEquals(countBefore + 1, countAfter);
        counter.close();
    }
}
