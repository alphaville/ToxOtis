/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.database.engine.bibtex;

import java.util.List;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.model.FindModel;
import org.opentox.toxotis.database.engine.model.ListModel;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author chung
 */
public class AssociateBibTeXTest {

    public AssociateBibTeXTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSomeMethod() throws Exception {
        ListModel listModels = new ListModel();
        IDbIterator<String> modelIt = listModels.list();
        String modelID = null;
        if (modelIt.hasNext()) {
            modelID = modelIt.next();
        } else {
            return;
        }
        modelIt.close();
        listModels.close();

        ListBibTeX listBib = new ListBibTeX();
        IDbIterator<String> bibIt = listBib.list();
        String bibID = null;
        if (bibIt.hasNext()) {
            bibID = bibIt.next();
        } else {
            return;
        }

        AssociateBibTeX bibTeXAssciator = new AssociateBibTeX(modelID,
                Services.ntua().augment("bibtex", bibID).toString());
        bibTeXAssciator.write();
        bibTeXAssciator.close();

        FindModel finder = new FindModel(Services.ntua());
        finder.setSearchById(modelID);
        IDbIterator<Model> modelList = finder.list();
        if (modelList.hasNext()) {
            Model found = modelList.next();
            Set<VRI> bibTexList = found.getBibTeXReferences();            
            assertNotNull(bibTexList);
            assertFalse(bibTexList.isEmpty());
            for (VRI bibtexUri : bibTexList){
                System.out.println(bibTexList);
            }
        }
    }
}
