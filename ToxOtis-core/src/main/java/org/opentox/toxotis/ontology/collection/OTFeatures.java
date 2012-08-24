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
public final class OTFeatures {

    public static final String NS = "http://www.opentox.org/api/1.1#";
    private static OntologicalClass iupacName;
    private static OntologicalClass chemicalName;
    private static OntologicalClass casRN;
    private static OntologicalClass einecs;
    private static OntologicalClass smiles;
    private static OntologicalClass reachRegistrationDate;
    private static OntologicalClass inChIStd;
    private static OntologicalClass inChIKeyStd;
    private static java.util.Map<String, Method> methodCache;

    private synchronized static void initMethodCache() {
        if (methodCache == null) {
            methodCache = new java.util.HashMap<String, Method>();
            for (Method method : OTFeatures.class.getDeclaredMethods()) {
                if (OntologicalClass.class.equals(method.getReturnType()) 
                        && method.getParameterTypes().length == 0) {
                    methodCache.put(method.getName(), method);
                }
            }
        }
    }

    public static OntologicalClass iupacName() {
        if (iupacName == null) {
            iupacName = new OntologicalClassImpl("IUPACName");
        }
        return iupacName;
    }

    public static OntologicalClass casRN() {
        if (casRN == null) {
            casRN = new OntologicalClassImpl("CASRN");
        }
        return casRN;
    }

    public static OntologicalClass chemicalName() {
        if (chemicalName == null) {
            chemicalName = new OntologicalClassImpl("ChemicalName");
        }
        return chemicalName;
    }

    public static OntologicalClass einecs() {
        if (einecs == null) {
            einecs = new OntologicalClassImpl("EINECS");
        }
        return einecs;
    }

    public static OntologicalClass smiles() {
        if (smiles == null) {
            smiles = new OntologicalClassImpl("SMILES");
        }
        return smiles;
    }

    public static OntologicalClass inchiStd() {
        if (inChIStd == null) {
            inChIStd = new OntologicalClassImpl("InChI_std");
        }
        return inChIStd;
    }

    public static OntologicalClass inchiKeyStd() {
        if (inChIKeyStd == null) {
            inChIKeyStd = new OntologicalClassImpl("InChIKey_std");
        }
        return inChIKeyStd;
    }

    public static OntologicalClass reachRegistrationDate() {
        if (reachRegistrationDate == null) {
            reachRegistrationDate = new OntologicalClassImpl("REACHRegistrationDate");
        }
        return reachRegistrationDate;
    }
}
