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
package org.opentox.toxotis.core.html.impl;

import java.util.ArrayList;
import java.util.List;
import org.opentox.toxotis.core.html.HTMLBody;
import org.opentox.toxotis.core.html.HTMLComponent;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLBodyImpl  extends HTMLExpandableComponentImpl implements HTMLBody{

    private List<String> bodyComps = new ArrayList<String>();
    private String footer="";
    private String header="";
    
    @Override
    public void addBodyComponent(String hd) {
        bodyComps.add(hd);
    }
        
    @Override
    public void setFooter(String footer) {
        this.footer = footer;
    }
    
    @Override
    public void setHeader(String header) {
        this.header = header;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<body>\n");
        builder.append(header);
        builder.append("<div class=\"panel\">\n");
        
        for (String temp : bodyComps){
            builder.append(temp);
            builder.append("\n");
        }
        for (HTMLComponent component : getComponents()){
            builder.append(component.toString());
            builder.append("\n");
        }
        builder.append("</div></body>\n");
        return builder.toString();
    }


}