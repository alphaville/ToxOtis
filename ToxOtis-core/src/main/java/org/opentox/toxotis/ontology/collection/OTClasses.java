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
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 * Collection of all Ontological Classes defined in OpenTox.
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

    private static OntologicalClass ms_MultiParameter;
    private static OntologicalClass ms_VectorParameter;
    private static OntologicalClass ms_VariableInfo;
    private static OntologicalClass ms_ParameterValue;
    private static OntologicalClass ms_SetValuedParameter;
    private static OntologicalClass ms_VariableValue;

    private static OntologicalClass ms_Task;
    private static OntologicalClass ms_ErrorReport;
    private static OntologicalClass ms_Thing;
    private static Map<String, Method> ms_methodCache;

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OTClasses.class);

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

    public static Set<OntologicalClass> getAll(){
        initMethodCache();
        Set<OntologicalClass> result = new HashSet<OntologicalClass>();
        Collection<Method> methods = ms_methodCache.values();
        for (Method m : methods){
            try {
                result.add((OntologicalClass)m.invoke(null));
            } catch (IllegalAccessException ex) {
                logger.warn(null, ex);
            } catch (IllegalArgumentException ex) {
                logger.warn(null, ex);
            } catch (InvocationTargetException ex) {
                logger.warn(null, ex);
            }
        }
        return result;
    }

    /**
     *
     * @param name
     * @return
     *      OntologicalClass instance for the given name of <code>null</code> if
     *      no such class is found.
     */
    public static OntologicalClass forName(String name) {
        initMethodCache();
        try {
            Method method = ms_methodCache.get(name);
            if (method == null) {
                return null;
            }
            OntologicalClass oc = (OntologicalClass) method.invoke(null);
            return oc;
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Every individual in the OWL world is a member of the class <code>owl:Thing</code>.
     * Every ontological class of the OpenTox ontology is a subclass of <code>owl:Thing</code>.
     * @return
     *      Universal class <code>owl:Thing</code>.
     */
    public static OntologicalClass Thing() {
        if (ms_Thing == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Thing");
            clazz.setNameSpace(OWL.NS);
            ms_Thing = clazz;
        }
        return ms_Thing;
    }

    /**
     * Superclass for all OpenTox resources. All classes of the OpenTox ontologies,
     * subclass ot:OpenToxResource
     * @return
     *      OpenTox superclass.
     */
    public static OntologicalClass OpenToxResource() {
        if (ms_OpenToxResource == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OpenToxResource");
            clazz.getMetaInfo().addComment("Generic OpenTox Resource");
            clazz.getSuperClasses().add(Thing());            
            ms_OpenToxResource = clazz;
        }
        return ms_OpenToxResource;
    }

    /**
     * Ontological class that provides access to all OpenTox algorithms
     * @see OTAlgorithmTypes OpenTox Algorithm Ontology
     */
    public static OntologicalClass Algorithm() {
        if (ms_Algorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Algorithm");
            clazz.getMetaInfo().addComment("Provides access to OpenTox algorithms");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_Algorithm = clazz;
        }
        return ms_Algorithm;
    }

    public static OntologicalClass Compound() {
        if (ms_Compound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Compound");
            clazz.getMetaInfo().addComment("API for OpenTox compound webservices");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_Compound = clazz;
        }
        return ms_Compound;
    }

    public static OntologicalClass Conformer() {
        if (ms_Conformer == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Conformer");
            clazz.getMetaInfo().addComment("API for OpenTox conformer webservices");
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

    /**
     * A Dataset Provides access to chemical compounds
     * and their features (e.g. structural, physical-chemical,
     * biological, toxicological properties)
     * @return
     *      Ontological class for Datasets
     */
    public static OntologicalClass Dataset() {
        if (ms_Dataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Dataset");
            clazz.getMetaInfo().addComment("Provides access to chemical compounds "
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
            OntologicalClass clazz = new OntologicalClassImpl("String");
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

    public static OntologicalClass VectorParameter() {
        if (ms_VectorParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("VectorParameter");
            clazz.getSuperClasses().add(MultiParameter());
            ms_VectorParameter = clazz;
        }
        return ms_VectorParameter;
    }

    public static OntologicalClass VariableInfo() {
        if (ms_VariableInfo == null) {
            OntologicalClass clazz = new OntologicalClassImpl("VariableInfo");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_VariableInfo = clazz;
        }
        return ms_VariableInfo;
    }

    public static OntologicalClass ParameterValue() {
        if (ms_ParameterValue == null) {
            OntologicalClass clazz = new OntologicalClassImpl("ParameterValue");
            clazz.getSuperClasses().add(OpenToxResource());
            ms_ParameterValue = clazz;
        }
        return ms_ParameterValue;
    }

    public static OntologicalClass MultiParameter() {
        if (ms_MultiParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MultiParameter");
            clazz.getSuperClasses().add(OpenToxResource());
            clazz.getDisjointWith().add(Parameter());// << Different from Parameter
            ms_MultiParameter = clazz;
        }
        return ms_MultiParameter;
    }

    public static OntologicalClass SetValuedParameter() {
        if (ms_SetValuedParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("SetValuedParameter");
            clazz.getSuperClasses().add(MultiParameter());
            ms_SetValuedParameter = clazz;
        }
        return ms_SetValuedParameter;
    }

    public static OntologicalClass VariableValue() {
        if (ms_VariableValue == null) {
            OntologicalClass clazz = new OntologicalClassImpl("VariableValue");
            clazz.getSuperClasses().add(MultiParameter());
            ms_VariableValue = clazz;
        }
        return ms_VariableValue;
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

    /**
     * Instances of the class ErrorReport
     * appear in ontological models to report some exceptional event
     * that happened during the transaction of data from one server
     * to another, request errors or internal server errors. An error
     * report is characterized by its actor, the corresponding status code,
     * some unique identifier for the exceptional event and possibly
     * another error report that triggered its creation.
     * 
     * @return
     *      Ontological class for Error Reports in OpenTox ontology.
     */
    public static OntologicalClass ErrorReport() {
        if (ms_ErrorReport == null) {
            OntologicalClass clazz = new OntologicalClassImpl("ErrorReport");
            clazz.getSuperClasses().add(OpenToxResource());
            clazz.getMetaInfo().addDescription("Instances of the class ErrorReport "
                    + "appear in ontological models to report some exceptional event "
                    + "that happened during the transaction of data from one server "
                    + "to another, request errors or internal server errors. An error "
                    + "report is characterized by its actor, the corresponding status code, "
                    + "some unique identifier for the exceptional event and possibly "
                    + "another error report that triggered its creation.");
            ms_ErrorReport = clazz;
        }
        return ms_ErrorReport;
    }
}


