/*
 *
 * Jaqpot - version 3
 *
 * The JAQPOT-3 web services are OpenTox API-1.2 compliant web services. Jaqpot
 * is a web application that supports model training and data preprocessing algorithms
 * such as multiple linear regression, support vector machines, neural networks
 * (an in-house implementation based on an efficient algorithm), an implementation
 * of the leverage algorithm for domain of applicability estimation and various
 * data preprocessing algorithms like PLS and data cleanup.
 *
 * Copyright (C) 2009-2014 Pantelis Sopasakis & Charalampos Chomenides & Lampovas Nikolaos
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

package org.opentox.toxotis.client.http;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.util.json.DatasetJsonDownloader;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @author Nikolaos Lampovas
 */
public class testSubstance {

    @Test
    public void test() {
        try {
            
            VRI input = new VRI("http://apps.ideaconsult.net:8080/enanomapper/substanceowner/PRCR-A6103ACB-F2C6-3190-98D2-FF3404F1E36C/dataset");
            DatasetJsonDownloader jsn = new DatasetJsonDownloader(input);
            JSONObject obj = jsn.getJSON();
            List<String> keys = new ArrayList<String>();
            keys.add("feature");
            keys.add("http://apps.ideaconsult.net:8080/enanomapper/property/AD2880DCCB16852FB869B7E10223708801EB1B02");
            keys.add("units");
            String units = jsn.traverse(keys,obj);
            
            keys = new ArrayList<String>();
            keys.add("feature");
            keys.add("http://apps.ideaconsult.net:8080/enanomapper/property/AD2880DCCB16852FB869B7E10223708801EB1B02");
            keys.add("annotation");
            keys.add("o");
            String medium = jsn.traverse(keys,obj);
            
            
            keys = new ArrayList<String>();
            keys.add("feature");
            keys.add("http://apps.ideaconsult.net:8080/enanomapper/property/AD2880DCCB16852FB869B7E10223708801EB1B02");
            keys.add("sameAs");
            String title = jsn.traverse(keys,obj);
            
            Boolean eof;
            eof=true;
        } catch (Exception ex) {
            Boolean eofa;
            eofa=true;
        }
    }
    
}
