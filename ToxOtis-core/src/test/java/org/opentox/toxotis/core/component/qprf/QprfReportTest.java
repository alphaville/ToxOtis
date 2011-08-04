/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.core.component.qprf;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class QprfReportTest {
    
    public QprfReportTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getReportMeta method, of class QprfReport.
     */
    @Test
    public void testSerialize() throws IOException {
        QprfReport qprf = new QprfReport();
        qprf.setReportMeta(new QprfReportMeta());
        qprf.setMeta(new MetaInfoImpl());
        qprf.setCompound(new Compound());
        qprf.setModel(new Model());
        qprf.setAuthors(new HashSet<QprfAuthor>());
        qprf.setStructuralAnalogues(new ArrayList<Compound>());
        ObjectOutputStream oos = new ObjectOutputStream(System.out);
        oos.writeObject(qprf);
    }

    
}
