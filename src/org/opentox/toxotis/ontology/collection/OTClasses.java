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
public class OTClasses {

    private OTClasses() {
    }
    public static final String NS = "http://www.opentox.org/api/1.1#";
    private static OntologicalClass ms_OpenToxResource;
    private static OntologicalClass ms_Algorithm;
    private static OntologicalClass ms_Compound;
    private static OntologicalClass ms_Conformer;
    private static OntologicalClass ms_DataEntry;
    private static OntologicalClass ms_DataType;
    private static OntologicalClass ms_Nominal;
    private static OntologicalClass ms_Numeric;
    private static OntologicalClass ms_String;
    private static OntologicalClass ms_NominalFeature;
    private static OntologicalClass ms_NumericFeature;
    private static OntologicalClass ms_StringFeature;
    private static OntologicalClass ms_Dataset;
    private static OntologicalClass ms_Feature;
    private static OntologicalClass ms_FeatureValue;
    private static OntologicalClass ms_FeatureValueNumeric;
    private static OntologicalClass ms_FeatureValueString;
    private static OntologicalClass ms_FeatureValueNominal;
    private static OntologicalClass ms_FeatureValuePair;
    private static OntologicalClass ms_Model;
    private static OntologicalClass ms_Parameter;
    private static OntologicalClass ms_Task;
    private static OntologicalClass ms_ErrorReport;
    private static OntologicalClass ms_Thing;
    private static Map<String, Method> ms_methodCache;

    private synchronized static void initMethodCache() {
        if (ms_methodCache == null) {
            ms_methodCache = new HashMap<String, Method>();
            for (Method method : OTClasses.class.getDeclaredMethods()) {
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
                throw new IllegalArgumentException("No such ontological class : " + name);
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
            ms_Thing = clazz;
        }
        return ms_Thing;
    }

    public static OntologicalClass OpenToxResource() {
        if (ms_OpenToxResource == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OpenToxResource");
            clazz.getMetaInfo().setComment("Generic OpenTox Resource");
            clazz.getSuperClasses().add(Thing());
            ms_OpenToxResource = clazz;
        }
        return ms_OpenToxResource;
    }

    public static OntologicalClass Algorithm() {
        if (ms_Algorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Algorithm");
            clazz.getMetaInfo().setComment("Provides access to OpenTox algorithms");
            clazz.getMetaInfo().setVersionInfo("1.1");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_Algorithm = clazz;
        }
        return ms_Algorithm;
    }

    public static OntologicalClass Compound() {
        if (ms_Compound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Compound");
            clazz.getMetaInfo().setComment("API for OpenTox compound webservices");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_Compound = clazz;
        }
        return ms_Compound;
    }

    public static OntologicalClass Conformer() {
        if (ms_Conformer == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Conformer");
            clazz.getMetaInfo().setComment("API for OpenTox conformer webservices");
            clazz.getSuperClasses().add(Compound());
            ms_Conformer = clazz;
        }
        return ms_Conformer;
    }

    public static OntologicalClass Model() {
        if (ms_Model == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Model");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_Model = clazz;
        }
        return ms_Model;
    }

    public static OntologicalClass DataEntry() {
        if (ms_DataEntry == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DataEntry");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_DataEntry = clazz;
        }
        return ms_DataEntry;
    }

    public static OntologicalClass DataType() {
        if (ms_DataType == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DataType");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_DataType = clazz;
        }
        return ms_DataType;
    }

    public static OntologicalClass Dataset() {
        if (ms_Dataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Dataset");
            clazz.getMetaInfo().setComment("Provides access to chemical compounds "
                    + "and their features (e.g. structural, physical-chemical, "
                    + "biological, toxicological properties)");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_Dataset = clazz;
        }
        return ms_Dataset;
    }

    public static OntologicalClass Feature() {
        if (ms_Feature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Feature");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_Feature = clazz;
        }
        return ms_Feature;
    }

    public static OntologicalClass FeatureValuePair() {
        if (ms_FeatureValuePair == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValuePair");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_FeatureValuePair = clazz;
        }
        return ms_FeatureValuePair;
    }

    public static OntologicalClass FeatureValue() {
        if (ms_FeatureValue == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValue");
            clazz.getSuperClasses().add(FeatureValuePair());
            ms_FeatureValue = clazz;
        }
        return ms_FeatureValue;
    }

    public static OntologicalClass FeatureValueNumeric() {
        if (ms_FeatureValueNumeric == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValueNumeric");
            clazz.getSuperClasses().add(FeatureValue());
            ms_FeatureValueNumeric = clazz;
        }
        return ms_FeatureValueNumeric;
    }

    public static OntologicalClass FeatureValueString() {
        if (ms_FeatureValueString == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValue");
            clazz.getSuperClasses().add(FeatureValue());
            ms_FeatureValueString = clazz;
        }
        return ms_FeatureValueString;
    }

    public static OntologicalClass FeatureValueNominal() {
        if (ms_FeatureValueNominal == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValueNominal");
            clazz.getSuperClasses().add(FeatureValueString());
            ms_FeatureValueNominal = clazz;
        }
        return ms_FeatureValueNominal;
    }

    public static OntologicalClass Nominal() {
        if (ms_Nominal == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Nominal");
            clazz.getSuperClasses().add(DataType());
            ms_Nominal = clazz;
        }
        return ms_Nominal;
    }

    public static OntologicalClass NominalFeature() {
        if (ms_NominalFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("NominalFeature");
            clazz.getSuperClasses().add(Feature());
            clazz.getSuperClasses().add(Nominal());
            ms_NominalFeature = clazz;
        }
        return ms_NominalFeature;
    }

    public static OntologicalClass Numeric() {
        if (ms_Numeric == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Numeric");
            clazz.getSuperClasses().add(DataType());
            ms_Numeric = clazz;
        }
        return ms_Numeric;
    }

    public static OntologicalClass NumericFeature() {
        if (ms_NumericFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("NumericFeature");
            clazz.getSuperClasses().add(Feature());
            clazz.getSuperClasses().add(Numeric());
            ms_NumericFeature = clazz;
        }
        return ms_NumericFeature;
    }

    public static OntologicalClass String() {
        if (ms_String == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Srtring");
            clazz.getSuperClasses().add(DataType());
            ms_String = clazz;
        }
        return ms_String;
    }

    public static OntologicalClass StringFeature() {
        if (ms_StringFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("StringFeature");
            clazz.getSuperClasses().add(Feature());
            clazz.getSuperClasses().add(String());
            ms_StringFeature = clazz;
        }
        return ms_StringFeature;
    }

    public static OntologicalClass Parameter() {
        if (ms_Parameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Parameter");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_Parameter = clazz;
        }
        return ms_Parameter;
    }

    public static OntologicalClass Task() {
        if (ms_Task == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Task");
            clazz.getSuperClasses().add(Feature());
            clazz.getSuperClasses().add(String());
            ms_Task = clazz;
        }
        return ms_Task;
    }

    public static OntologicalClass ErrorReport() {
        if (ms_ErrorReport == null) {
            OntologicalClass clazz = new OntologicalClassImpl("ErrorReport");
            clazz.getSuperClasses().add(OpenToxResource());
            clazz.getMetaInfo().setDescription("Instances of the class ErrorReport "
                    + "appear in ontological models to report some exceptional event "
                    + "that happened during the transaction of data from one server "
                    + "to another, request errors or internal server errors. An error "
                    + "report is characterized by its actor, the corresponding status code, "
                    + "some unique identifier for the exceptional event and possibly "
                    + "another error report that triggered its creation.");
            clazz.getMetaInfo().setVersionInfo("Latest modification of OpenTox ontology version 1.1 (May 31, 2010)");
            ms_ErrorReport = clazz;
        }
        return ms_ErrorReport;
    }
}


