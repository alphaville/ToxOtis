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
package org.opentox.toxotis.ontology.collection;

import java.lang.reflect.Method;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 * Collection of all features defined in the OpenTox ontology.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTFeatures {

    public static final String NS = "http://www.opentox.org/api/1.1#";
    private static OntologicalClass ms_IUPACName;
    private static OntologicalClass ms_ChemicalName;
    private static OntologicalClass ms_CASRN;
    private static OntologicalClass ms_EINECS;
    private static OntologicalClass ms_SMILES;
    private static OntologicalClass ms_REACHRegistrationDate;
    private static OntologicalClass ms_InChI_std;
    private static OntologicalClass ms_InChIKey_std;
    private static java.util.Map<String, Method> ms_methodCache;

    private synchronized static void initMethodCache() {
        if (ms_methodCache == null) {
            ms_methodCache = new java.util.HashMap<String, Method>();
            for (Method method : OTFeatures.class.getDeclaredMethods()) {
                if (OntologicalClass.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    ms_methodCache.put(method.getName(), method);
                }
            }
        }
    }

    public static OntologicalClass IUPACName() {
        if (ms_IUPACName == null) {
            ms_IUPACName = new OntologicalClassImpl("IUPACName");
        }
        return ms_IUPACName;
    }

    public static OntologicalClass CASRN() {
        if (ms_CASRN == null) {
            ms_CASRN = new OntologicalClassImpl("CASRN");
        }
        return ms_CASRN;
    }

    public static OntologicalClass ChemicalName() {
        if (ms_ChemicalName == null) {
            ms_ChemicalName = new OntologicalClassImpl("ChemicalName");
        }
        return ms_ChemicalName;
    }

    public static OntologicalClass EINECS() {
        if (ms_EINECS == null) {
            ms_EINECS = new OntologicalClassImpl("EINECS");
        }
        return ms_EINECS;
    }

    public static OntologicalClass SMILES() {
        if (ms_SMILES == null) {
            ms_SMILES = new OntologicalClassImpl("SMILES");
        }
        return ms_SMILES;
    }

    public static OntologicalClass InChI_std() {
        if (ms_InChI_std == null) {
            ms_InChI_std = new OntologicalClassImpl("InChI_std");
        }
        return ms_InChI_std;
    }

    public static OntologicalClass InChIKey_std() {
        if (ms_InChIKey_std == null) {
            ms_InChIKey_std = new OntologicalClassImpl("InChIKey_std");
        }
        return ms_InChIKey_std;
    }

    public static OntologicalClass REACHRegistrationDate() {
        if (ms_REACHRegistrationDate == null) {
            ms_REACHRegistrationDate = new OntologicalClassImpl("REACHRegistrationDate");
        }
        return ms_REACHRegistrationDate;
    }
}
