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

import com.hp.hpl.jena.vocabulary.OWL;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
    private static OntologicalClass AlgorithmType;
    private static OntologicalClass ApplicabilityDomain;
    private static OntologicalClass MSDMTox;
    private static OntologicalClass Learning;
    private static OntologicalClass SingleTarget;
    private static OntologicalClass MultipleTargets;
    private static OntologicalClass EagerLearning;
    private static OntologicalClass LazyLearning;
    private static OntologicalClass Regression;
    private static OntologicalClass Classification;
    private static OntologicalClass Preprocessing;
    private static OntologicalClass DataCleanup;
    private static OntologicalClass FeatureSelection;
    private static OntologicalClass Normalization;
    private static OntologicalClass Supervised;
    private static OntologicalClass Unsupervised;
    private static Map<String, Method> methodCache;

    private synchronized static void initMethodCache() {
        if (methodCache == null) {
            methodCache = new HashMap<String, Method>();
            for (Method method : OTAlgorithmTypes.class.getDeclaredMethods()) {
                if (OntologicalClass.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    methodCache.put(method.getName(), method);
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
        if ("Thing".equals(name)){
            return OTClasses.Thing();
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



    public static OntologicalClass AlgorithmType() {
        if (AlgorithmType == null) {
            OntologicalClass clazz = new OntologicalClassImpl("AlgorithmType");
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().addComment("Generic class for all algorithm types in OpenTox");
            clazz.getMetaInfo().addTitle("Algorithm Types");
            clazz.getSuperClasses().add(OTClasses.Thing());
            AlgorithmType = clazz;
        }
        return AlgorithmType;
    }

    public static OntologicalClass ApplicabilityDomain() {
        if (ApplicabilityDomain == null) {
            OntologicalClass clazz = new OntologicalClassImpl("ApplicabilityDomain");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(AlgorithmType());
            clazz.getMetaInfo().addTitle("Appplicability Domain Algorithm");
            ApplicabilityDomain = clazz;
        }
        return ApplicabilityDomain;
    }

    public static OntologicalClass MSDMTox() {
        if (MSDMTox == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MSDMTox");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(AlgorithmType());
            MSDMTox = clazz;
        }
        return MSDMTox;
    }

    public static OntologicalClass Learning() {
        if (Learning == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Learning");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(MSDMTox());
            Learning = clazz;
        }
        return Learning;
    }

    public static OntologicalClass Classification() {
        if (Classification == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Classification");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(Learning());
            Classification = clazz;
        }
        return Classification;
    }

    public static OntologicalClass DataCleanup() {
        if (DataCleanup == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DataCleanup");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(AlgorithmType());
            DataCleanup = clazz;
        }
        return DataCleanup;
    }

    public static OntologicalClass EagerLearning() {
        if (EagerLearning == null) {
            OntologicalClass clazz = new OntologicalClassImpl("EagerLearning");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(Learning());
            clazz.getDisjointWith().add(LazyLearning());
            EagerLearning = clazz;
        }
        LazyLearning.getDisjointWith().add(EagerLearning);
        return EagerLearning;
    }

    public static OntologicalClass FeatureSelection() {
        if (FeatureSelection == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureSelection");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(Preprocessing());
            FeatureSelection = clazz;
        }
        return FeatureSelection;
    }

    public static OntologicalClass LazyLearning() {
        if (LazyLearning == null) {
            OntologicalClass clazz = new OntologicalClassImpl("LazyLearning");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(Learning());
            LazyLearning = clazz;
        }
        return LazyLearning;
    }

    public static OntologicalClass MultipleTargets() {
        if (MultipleTargets == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MultipleTargets");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(MSDMTox());
            MultipleTargets = clazz;
        }
        return MultipleTargets;
    }

    public static OntologicalClass Normalization() {
        if (Normalization == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Normalization");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(Preprocessing());
            Normalization = clazz;
        }
        return Normalization;
    }

    public static OntologicalClass Preprocessing() {
        if (Preprocessing == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Preprocessing");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(AlgorithmType());
            Preprocessing = clazz;
        }
        return Preprocessing;
    }

    public static OntologicalClass Regression() {
        if (Regression == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Regression");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(Learning());
            Regression = clazz;
        }
        return Regression;
    }

    public static OntologicalClass SingleTarget() {
        if (SingleTarget == null) {
            OntologicalClass clazz = new OntologicalClassImpl("SingleTarget");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(MSDMTox());
            clazz.getDisjointWith().add(MultipleTargets());
            SingleTarget = clazz;
        }
        return SingleTarget;
    }

    public static OntologicalClass Supervised() {
        if (Supervised == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Supervised");
            clazz.setNameSpace(NS);
            clazz.getDisjointWith().add(Unsupervised());
            Supervised = clazz;
        }
        Unsupervised.getDisjointWith().add(Supervised);
        return Supervised;
    }

    public static OntologicalClass Unsupervised() {
        if (Unsupervised == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Unsupervised");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(AlgorithmType());
            Unsupervised = clazz;
        }
        return Unsupervised;
    }
}
