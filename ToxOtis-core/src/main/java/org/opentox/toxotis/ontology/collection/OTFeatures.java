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
    private static OntologicalClass IUPACName;
    private static OntologicalClass ChemicalName;
    private static OntologicalClass CASRN;
    private static OntologicalClass EINECS;
    private static OntologicalClass SMILES;
    private static OntologicalClass REACHRegistrationDate;
    private static OntologicalClass InChI_std;
    private static OntologicalClass InChIKey_std;
    private static java.util.Map<String, Method> methodCache;

    private synchronized static void initMethodCache() {
        if (methodCache == null) {
            methodCache = new java.util.HashMap<String, Method>();
            for (Method method : OTFeatures.class.getDeclaredMethods()) {
                if (OntologicalClass.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    methodCache.put(method.getName(), method);
                }
            }
        }
    }

    public static OntologicalClass IUPACName() {
        if (IUPACName == null) {
            IUPACName = new OntologicalClassImpl("IUPACName");
        }
        return IUPACName;
    }

    public static OntologicalClass CASRN() {
        if (CASRN == null) {
            CASRN = new OntologicalClassImpl("CASRN");
        }
        return CASRN;
    }

    public static OntologicalClass ChemicalName() {
        if (ChemicalName == null) {
            ChemicalName = new OntologicalClassImpl("ChemicalName");
        }
        return ChemicalName;
    }

    public static OntologicalClass EINECS() {
        if (EINECS == null) {
            EINECS = new OntologicalClassImpl("EINECS");
        }
        return EINECS;
    }

    public static OntologicalClass SMILES() {
        if (SMILES == null) {
            SMILES = new OntologicalClassImpl("SMILES");
        }
        return SMILES;
    }

    public static OntologicalClass InChI_std() {
        if (InChI_std == null) {
            InChI_std = new OntologicalClassImpl("InChI_std");
        }
        return InChI_std;
    }

    public static OntologicalClass InChIKey_std() {
        if (InChIKey_std == null) {
            InChIKey_std = new OntologicalClassImpl("InChIKey_std");
        }
        return InChIKey_std;
    }

    public static OntologicalClass REACHRegistrationDate() {
        if (REACHRegistrationDate == null) {
            REACHRegistrationDate = new OntologicalClassImpl("REACHRegistrationDate");
        }
        return REACHRegistrationDate;
    }
}
