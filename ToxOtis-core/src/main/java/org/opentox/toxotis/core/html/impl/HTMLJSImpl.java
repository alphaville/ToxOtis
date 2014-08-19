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

package org.opentox.toxotis.core.html.impl;

import org.opentox.toxotis.core.html.HTMLTag;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @author Nikolaos Lampovas
 */
public class HTMLJSImpl extends HTMLExpandableComponentImpl {

    private String jsContent;


    public HTMLJSImpl(String content) {
        jsContent = content;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("<script type=\"text/javascript\">").append(jsContent);
        builder.append("</script>");
        return builder.toString();
    }
}
