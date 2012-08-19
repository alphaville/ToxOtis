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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 * A collection of all algorithm types specified in OpenTox. The namespace for these
 * algorithms is <code>http://www.opentox.org/algorithmTypes.owl/</code> and is usually
 * abbreviated as <code><ota</code>.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class OTAlgorithmTypes {

    private OTAlgorithmTypes() {
        // Cannot be subclassed!
    }
    /**
     * Namespace of all algorithm ontological classes.
     */
    public static final String NS = "http://www.opentox.org/algorithmTypes.owl/#";
    private static OntologicalClass msAlgorithmType;
    private static OntologicalClass msApplicabilityDomain;
    private static OntologicalClass msMSDMTox;
    private static OntologicalClass msLearning;
    private static OntologicalClass msSingleTarget;
    private static OntologicalClass msMultipleTargets;
    private static OntologicalClass msEagerLearning;
    private static OntologicalClass msLazyLearning;
    private static OntologicalClass msRegression;
    private static OntologicalClass msClassification;
    private static OntologicalClass msPreprocessing;
    private static OntologicalClass msDataCleanup;
    private static OntologicalClass msFeatureSelection;
    private static OntologicalClass msNormalization;
    private static OntologicalClass msSupervised;
    private static OntologicalClass msUnsupervised;
    private static Map<String, Method> methodCache;

    private synchronized static void initMethodCache() {
        if (methodCache == null) {
            methodCache = new HashMap<String, Method>();
            for (Method method : OTAlgorithmTypes.class.getDeclaredMethods()) {
                if (OntologicalClass.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    try {
                        OntologicalClass oc = (OntologicalClass) method.invoke(null);
                        methodCache.put(oc.getName(), method);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(OTAlgorithmTypes.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(OTAlgorithmTypes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public static Set<OntologicalClass> getAll() {
        initMethodCache();
        Set<OntologicalClass> result = new HashSet<OntologicalClass>();
        Collection<Method> methods = methodCache.values();
        for (Method m : methods) {
            try {
                result.add((OntologicalClass) m.invoke(null));
            } catch (IllegalAccessException ex) {
            } catch (IllegalArgumentException ex) {
            } catch (InvocationTargetException ex) {
            }
        }
        return result;
    }

    public static OntologicalClass forName(String name) {
        initMethodCache();
        if ("Thing".equals(name)) {
            return OTClasses.thing();
        }
        try {
            Method method = methodCache.get(name);
            if (method == null) {
                throw new IllegalArgumentException("No such Algorithm Type : " + name);
            }
            OntologicalClass oc = (OntologicalClass) method.invoke(null);
            return oc;
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static OntologicalClass algorithmType() {
        if (msAlgorithmType == null) {
            OntologicalClass clazz = new OntologicalClassImpl("AlgorithmType");
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("Generic class for all algorithm types in OpenTox");
            clazz.getMetaInfo().addTitle("Algorithm Types");
            clazz.getSuperClasses().add(OTClasses.thing());
            msAlgorithmType = clazz;
        }
        return msAlgorithmType;
    }

    public static OntologicalClass applicabilityDomain() {
        if (msApplicabilityDomain == null) {
            OntologicalClass clazz = new OntologicalClassImpl("ApplicabilityDomain");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(algorithmType());
            clazz.getMetaInfo().addTitle("Appplicability Domain Algorithm");
            msApplicabilityDomain = clazz;
        }
        return msApplicabilityDomain;
    }

    public static OntologicalClass msdmTox() {
        if (msMSDMTox == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MSDMTox");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(algorithmType());
            msMSDMTox = clazz;
        }
        return msMSDMTox;
    }

    /**
     * Generic learning algorithm
     */
    public static OntologicalClass learning() {
        if (msLearning == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Learning");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(msdmTox());
            msLearning = clazz;
        }
        return msLearning;
    }

    /**
     * Classification algorithm.
     */
    public static OntologicalClass classification() {
        if (msClassification == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Classification");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(learning());
            msClassification = clazz;
        }
        return msClassification;
    }

    /**
     * Data-cleanup preprocessing algorithm.
     */
    public static OntologicalClass dataCleanup() {
        if (msDataCleanup == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DataCleanup");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(algorithmType());
            msDataCleanup = clazz;
        }
        return msDataCleanup;
    }

    /**
     * Eager Learning algorithm.
     */
    public static OntologicalClass eagerLearning() {
        if (msEagerLearning == null) {
            OntologicalClass clazz = new OntologicalClassImpl("EagerLearning");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(learning());
            clazz.getDisjointWith().add(lazyLearning());
            msEagerLearning = clazz;
        }
        msLazyLearning.getDisjointWith().add(msEagerLearning);
        return msEagerLearning;
    }

    /**
     * Feature selection algorithm.
     */
    public static OntologicalClass featureSelection() {
        if (msFeatureSelection == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureSelection");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(preprocessing());
            msFeatureSelection = clazz;
        }
        return msFeatureSelection;
    }

    /**
     * Lazy learning algorithm.
     */
    public static OntologicalClass lazyLearning() {
        if (msLazyLearning == null) {
            OntologicalClass clazz = new OntologicalClassImpl("LazyLearning");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(learning());
            msLazyLearning = clazz;
        }
        return msLazyLearning;
    }

    /**
     * Multiple Targets Trainer.
     */
    public static OntologicalClass multipleTargets() {
        if (msMultipleTargets == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MultipleTargets");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(msdmTox());
            msMultipleTargets = clazz;
        }
        return msMultipleTargets;
    }

    public static OntologicalClass normalization() {
        if (msNormalization == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Normalization");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(preprocessing());
            msNormalization = clazz;
        }
        return msNormalization;
    }

    public static OntologicalClass preprocessing() {
        if (msPreprocessing == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Preprocessing");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(algorithmType());
            msPreprocessing = clazz;
        }
        return msPreprocessing;
    }

    public static OntologicalClass regression() {
        if (msRegression == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Regression");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(learning());
            msRegression = clazz;
        }
        return msRegression;
    }

    public static OntologicalClass singleTarget() {
        if (msSingleTarget == null) {
            OntologicalClass clazz = new OntologicalClassImpl("SingleTarget");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(msdmTox());
            clazz.getDisjointWith().add(multipleTargets());
            msSingleTarget = clazz;
        }
        return msSingleTarget;
    }

    public static OntologicalClass supervised() {
        if (msSupervised == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Supervised");
            clazz.setNameSpace(NS);
            clazz.getDisjointWith().add(unsupervised());
            msSupervised = clazz;
        }
        msUnsupervised.getDisjointWith().add(msSupervised);
        return msSupervised;
    }

    public static OntologicalClass unsupervised() {
        if (msUnsupervised == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Unsupervised");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(algorithmType());
            msUnsupervised = clazz;
        }
        return msUnsupervised;
    }
}
