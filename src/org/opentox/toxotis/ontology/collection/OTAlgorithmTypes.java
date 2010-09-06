package org.opentox.toxotis.ontology.collection;

import com.hp.hpl.jena.vocabulary.OWL;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTAlgorithmTypes {

    private OTAlgorithmTypes() {
        // Cannot be subclassed!
    }
    /**
     * Namespace of all algorithm ontological classes.
     */
    public static final String NS = "http://www.opentox.org/algorithmTypes.owl/#";
    private static OntologicalClass ms_AlgorithmType;
    private static OntologicalClass ms_ApplicabilityDomain;
    private static OntologicalClass ms_MSDMTox;
    private static OntologicalClass ms_Learning;
    private static OntologicalClass ms_SingleTarget;
    private static OntologicalClass ms_MultipleTargets;
    private static OntologicalClass ms_EagerLearning;
    private static OntologicalClass ms_LazyLearning;
    private static OntologicalClass ms_Regression;
    private static OntologicalClass ms_Classification;
    private static OntologicalClass ms_Preprocessing;
    private static OntologicalClass ms_DataCleanup;
    private static OntologicalClass ms_FeatureSelection;
    private static OntologicalClass ms_Normalization;
    private static OntologicalClass ms_Supervised;
    private static OntologicalClass ms_Unsupervised;
    private static OntologicalClass ms_Thing;
    private static Map<String, Method> ms_methodCache;

    private synchronized static void initMethodCache() {
        if (ms_methodCache == null) {
            ms_methodCache = new HashMap<String, Method>();
            for (Method method : OTAlgorithmTypes.class.getDeclaredMethods()) {
                if (OntologicalClass.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    ms_methodCache.put(method.getName(), method);
                }
            }
        }
    }

    public static OntologicalClass forName(String name) {
        initMethodCache();
        try {
            Method method = ms_methodCache.get(name);
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

    private static OntologicalClass Thing() {
        if (ms_Thing == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Thing");
            clazz.setNameSpace(OWL.NS);
            clazz.getMetaInfo().setComment("All classes are subclasses of owl:Thing");
            ms_Thing = clazz;
        }
        return ms_Thing;
    }

    public static OntologicalClass AlgorithmType() {
        if (ms_AlgorithmType == null) {
            OntologicalClass clazz = new OntologicalClassImpl("AlgorithmType");
            clazz.setNameSpace(NS);
            clazz.getMetaInfo().setComment("Generic class for all algorithm types in OpenTox");
            clazz.getMetaInfo().setTitle("Algorithm Types");
            clazz.getSuperClasses().add(Thing());
            ms_AlgorithmType = clazz;
        }
        return ms_AlgorithmType;
    }

    public static OntologicalClass ApplicabilityDomain() {
        if (ms_ApplicabilityDomain == null) {
            OntologicalClass clazz = new OntologicalClassImpl("ApplicabilityDomain");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(AlgorithmType());
            clazz.getMetaInfo().setTitle("Appplicability Domain Algorithm");
            ms_ApplicabilityDomain = clazz;
        }
        return ms_ApplicabilityDomain;
    }

    public static OntologicalClass MSDMTox() {
        if (ms_MSDMTox == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MSDMTox");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(AlgorithmType());
            ms_MSDMTox = clazz;
        }
        return ms_MSDMTox;
    }

    public static OntologicalClass Learning() {
        if (ms_Learning == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Learning");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(MSDMTox());
            ms_Learning = clazz;
        }
        return ms_Learning;
    }

    public static OntologicalClass Classification() {
        if (ms_Classification == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Classification");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(Learning());
            ms_Classification = clazz;
        }
        return ms_Classification;
    }

    public static OntologicalClass DataCleanup() {
        if (ms_DataCleanup == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DataCleanup");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(AlgorithmType());
            ms_DataCleanup = clazz;
        }
        return ms_DataCleanup;
    }

    public static OntologicalClass EagerLearning() {
        if (ms_EagerLearning == null) {
            OntologicalClass clazz = new OntologicalClassImpl("EagerLearning");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(Learning());
            clazz.getDisjointWith().add(LazyLearning());
            ms_EagerLearning = clazz;
        }
        ms_LazyLearning.getDisjointWith().add(ms_EagerLearning);
        return ms_EagerLearning;
    }

    public static OntologicalClass FeatureSelection() {
        if (ms_FeatureSelection == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureSelection");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(Preprocessing());
            ms_FeatureSelection = clazz;
        }
        return ms_FeatureSelection;
    }

    public static OntologicalClass LazyLearning() {
        if (ms_LazyLearning == null) {
            OntologicalClass clazz = new OntologicalClassImpl("LazyLearning");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(Learning());
            ms_LazyLearning = clazz;
        }
        return ms_LazyLearning;
    }

    public static OntologicalClass MultipleTargets() {
        if (ms_MultipleTargets == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MultipleTargets");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(MSDMTox());
            ms_MultipleTargets = clazz;
        }
        return ms_MultipleTargets;
    }

    public static OntologicalClass Normalization() {
        if (ms_Normalization == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Normalization");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(Preprocessing());
            ms_Normalization = clazz;
        }
        return ms_Normalization;
    }

    public static OntologicalClass Preprocessing() {
        if (ms_Preprocessing == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Preprocessing");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(AlgorithmType());
            ms_Preprocessing = clazz;
        }
        return ms_Preprocessing;
    }

    public static OntologicalClass Regression() {
        if (ms_Regression == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Regression");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(Learning());
            ms_Regression = clazz;
        }
        return ms_Regression;
    }

    public static OntologicalClass SingleTarget() {
        if (ms_SingleTarget == null) {
            OntologicalClass clazz = new OntologicalClassImpl("SingleTarget");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(MSDMTox());
            clazz.getDisjointWith().add(MultipleTargets());
            ms_SingleTarget = clazz;
        }
        return ms_SingleTarget;
    }

    public static OntologicalClass Supervised() {
        if (ms_Supervised == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Supervised");
            clazz.setNameSpace(NS);
            clazz.getDisjointWith().add(Unsupervised());
            ms_Supervised = clazz;
        }
        ms_Unsupervised.getDisjointWith().add(ms_Supervised);
        return ms_Supervised;
    }

    public static OntologicalClass Unsupervised() {
        if (ms_Unsupervised == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Unsupervised");
            clazz.setNameSpace(NS);
            clazz.getSuperClasses().add(AlgorithmType());
            ms_Unsupervised = clazz;
        }
        return ms_Unsupervised;
    }
}
