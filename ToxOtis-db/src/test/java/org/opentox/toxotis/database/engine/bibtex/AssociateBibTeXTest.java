/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.database.engine.bibtex;

import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.model.FindModel;
import org.opentox.toxotis.database.engine.model.ListModel;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.global.DbConfiguration;
import org.opentox.toxotis.database.pool.DataSourceFactory;

/**
 *
 * @author chung
 */
public class AssociateBibTeXTest {

    public AssociateBibTeXTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        org.opentox.toxotis.database.TestUtils.setUpDB();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DataSourceFactory.getInstance().close();
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
            for (VRI bibtexUri : bibTexList) {
                assertNotNull(bibtexUri);
            }
        }
    }
}
