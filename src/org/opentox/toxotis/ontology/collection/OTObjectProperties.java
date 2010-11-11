package org.opentox.toxotis.ontology.collection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.ontology.OTObjectProperty;
import org.opentox.toxotis.ontology.impl.OTObjectPropertyImpl;

/**
 * Collection of all object propertied used in OpenTox (API version 1.1).
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTObjectProperties {

    private OTObjectProperties() {
    }
    private static OTObjectProperty ms_compound;
    private static OTObjectProperty ms_conformer;
    private static OTObjectProperty ms_dataEntry;
    private static OTObjectProperty ms_feature;
    private static OTObjectProperty ms_model;
    private static OTObjectProperty ms_algorithm;
    private static OTObjectProperty ms_dependentVariables;
    private static OTObjectProperty ms_independentVariables;
    private static OTObjectProperty ms_predictedVariables;
    private static OTObjectProperty ms_parameters;
    private static OTObjectProperty ms_trainingDataset;
    private static OTObjectProperty ms_values;
    private static OTObjectProperty ms_trace;
    private static OTObjectProperty ms_errorReport;
    private static OTObjectProperty ms_bibtex;
    private static OTObjectProperty ms_listElement;
    private static OTObjectProperty ms_hasSource;
    private static OTObjectProperty ms_variableInfo;
    private static OTObjectProperty ms_parameterValues;
    private static OTObjectProperty ms_variableValues;
    private static OTObjectProperty ms_variable;
    private static OTObjectProperty ms_multiParameter;


    private static Map<String, Method> ms_methodCache;

    private synchronized static void initMethodCache() {
        if (ms_methodCache == null) {
            ms_methodCache = new HashMap<String, Method>();
            for (Method method : OTObjectProperties.class.getDeclaredMethods()) {
                if (OTObjectProperty.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    ms_methodCache.put(method.getName(), method);
                }
            }
        }
    }

    public static OTObjectProperty forName(String name) {
        initMethodCache();
        try {
            Method method = ms_methodCache.get(name);
            if (method == null) {
                throw new IllegalArgumentException("No such object property : " + name);
            }
            OTObjectProperty oc = (OTObjectProperty) method.invoke(null);
            return oc;
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static OTObjectProperty compound() {
        if (ms_compound == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("compound");
            property.getDomain().add(OTClasses.DataEntry());
            property.getRange().add(OTClasses.Compound());
            property.getMetaInfo().addComment("A DataEntry is defined with a "
                    + "single compound and multiple feature values. "
                    + "This property sets the relationship between a DataEntry and a Compound");
            ms_compound = property;
        }
        return ms_compound;
    }

    public static OTObjectProperty conformer() {
        if (ms_conformer == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("conformer");
            property.getDomain().add(OTClasses.Compound());
            property.getRange().add(OTClasses.Conformer());
            ms_conformer = property;
        }
        return ms_conformer;
    }

    public static OTObjectProperty dataEntry() {
        if (ms_dataEntry == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("dataEntry");
            property.getDomain().add(OTClasses.Dataset());
            property.getRange().add(OTClasses.DataEntry());
            property.getMetaInfo().addComment("A Dataset contains multiple DataEntries.  "
                    + "This property specifies the relationship between Dataset and DataEntry.");
            ms_dataEntry = property;
        }
        return ms_dataEntry;
    }

    public static OTObjectProperty feature() {
        if (ms_feature == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("feature");
            property.getDomain().add(OTClasses.FeatureValuePair());
            property.getRange().add(OTClasses.Feature());
            property.getMetaInfo().addDescription("FeatureValue contains a value for "
                    + "specific Feature, specified by this relationship.");
            property.getMetaInfo().addComment("feature: FeatureValuePair --> Feature");
            ms_feature = property;
        }
        return ms_feature;
    }

    public static OTObjectProperty model() {
        if (ms_model == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("model");
            property.getDomain().add(OTClasses.Model());
            property.getRange().add(OTClasses.Dataset());
            property.getMetaInfo().addComment("Superproperty for other properties such as "
                    + "algorithm:Model-->Algorithm, dependentVariables: Model --> Feature and other");
            ms_model = property;
        }
        return ms_model;
    }

    public static OTObjectProperty algorithm() {
        if (ms_algorithm == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("algorithm");
            property.getDomain().add(OTClasses.Model());
            property.getRange().add(OTClasses.Algorithm());
            property.getSuperProperties().add(model());
            property.getMetaInfo().addDescription("The algorithm, used to create the Model");
            ms_algorithm = property;
        }
        return ms_algorithm;
    }

    public static OTObjectProperty predictedVariables() {
        if (ms_predictedVariables == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("predictedVariables");
            property.getDomain().add(OTClasses.Model());
            property.getRange().add(OTClasses.Feature());
            property.getSuperProperties().add(model());
            property.getMetaInfo().addDescription("Variables, holding the predicted "
                    + "values, generated by the model");
            ms_predictedVariables = property;
        }
        return ms_predictedVariables;
    }

    public static OTObjectProperty dependentVariables() {
        if (ms_dependentVariables == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("dependentVariables");
            property.getDomain().add(OTClasses.Model());
            property.getRange().add(OTClasses.Feature());
            property.getSuperProperties().add(model());
            property.getMetaInfo().addDescription("A model can have one or more "
                    + "dependent variables, described as multiple features, "
                    + "specified by this relationship.");
            ms_dependentVariables = property;
        }
        return ms_dependentVariables;
    }

    public static OTObjectProperty independentVariables() {
        if (ms_independentVariables == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("independentVariables");
            property.getDomain().add(OTClasses.Model());
            property.getRange().add(OTClasses.Feature());
            property.getSuperProperties().add(model());
            property.getMetaInfo().addDescription("A model can have multiple "
                    + "independent variables, described as multiple features, "
                    + "specified by this relationship.");
            ms_independentVariables = property;
        }
        return ms_independentVariables;
    }

    public static OTObjectProperty parameters() {
        if (ms_parameters == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("parameters");
            property.getDomain().add(OTClasses.Model());
            property.getDomain().add(OTClasses.Algorithm());
            property.getRange().add(OTClasses.Parameter());
            property.getSuperProperties().add(model());
            property.getMetaInfo().addDescription("An object property that assigns a parameter to a Model "
                    + "(parameters: Model --> Parameter) or an Algorithm (parameters: Algorithm --> Parameter)");
            property.getMetaInfo().addComment("Algorithms and Models can have multiple parameters");
            ms_parameters = property;
        }
        return ms_parameters;
    }

    public static OTObjectProperty trainingDataset() {
        if (ms_trainingDataset == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("trainingDataset");
            property.getDomain().add(OTClasses.Model());
            property.getRange().add(OTClasses.Dataset());
            property.getSuperProperties().add(model());
            property.getMetaInfo().addDescription("A model is derived by applying "
                    + "an Algorithm on a training Dataset.");
            ms_trainingDataset = property;
        }
        return ms_trainingDataset;
    }

    public static OTObjectProperty values() {
        if (ms_values == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("values");
            property.getDomain().add(OTClasses.DataEntry());
            property.getRange().add(OTClasses.FeatureValuePair());
            property.getMetaInfo().addDescription("A DataEntry is defined with "
                    + "a single compound and multiple feature values. This property "
                    + "sets the relationship between a DataEntry and multiple FeatureValues");
            ms_values = property;
        }
        return ms_values;
    }

    public static OTObjectProperty trace() {
        if (ms_trace == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("values");
            property.getDomain().add(OTClasses.DataEntry());
            property.getRange().add(OTClasses.FeatureValuePair());
            property.getMetaInfo().addDescription("Some other ErrorReport corresponding "
                    + "to an exceptional event that triggered this one. Is particularly "
                    + "useful when a service acts as a proxy or gateway to other services.");
            property.getMetaInfo().addComment("trace: ErrorReport --> ErrorReport");
            ms_trace = property;
        }
        return ms_trace;
    }

    public static OTObjectProperty errorReport() {
        if (ms_errorReport == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("errorReport");
            property.getDomain().add(OTClasses.Task());
            property.getRange().add(OTClasses.FeatureValuePair());
            property.getMetaInfo().addDescription("Assigns an error report generated by a failed task.");
            property.getMetaInfo().addComment("errorReport: Task --> ErrorReport");
            ms_errorReport = property;
        }
        return ms_errorReport;
    }

    public static OTObjectProperty bibTex() {
        if (ms_bibtex == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("bibtex");
            property.getDomain().add(OTClasses.Algorithm());
            property.getDomain().add(OTClasses.Model());
            property.getDomain().add(OTClasses.Dataset());
            property.getDomain().add(OTClasses.Compound());
            property.getDomain().add(OTClasses.Feature());
            property.getRange().add(KnoufBibTex.Entry());
            property.getMetaInfo().addDescription("Assigns a bibliographic reference to an OpenTox resource "
                    + "such as an algorith, a model, a dataset or other resource of interest.");
            property.getMetaInfo().addComment("errorReport: OpeTox BibTex Subject --> BibTeX");
            ms_bibtex = property;
        }
        return ms_bibtex;
    }

    public static OTObjectProperty hasSource() {
        if (ms_hasSource == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("hasSource");
            property.getDomain().add(OTClasses.Feature());
            property.getRange().add(OTClasses.Algorithm());
            property.getRange().add(OTClasses.Dataset());
            property.getRange().add(OTClasses.Model());
            ms_hasSource = property;
        }
        return ms_hasSource;
    }

    public static OTObjectProperty variableInfo() {
        if (ms_variableInfo == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("variableInfo");
            property.getDomain().add(OTClasses.SetValuedParameter());
            property.getRange().add(OTClasses.VariableInfo());
            ms_variableInfo = property;
        }
        return ms_variableInfo;
    }

    public static OTObjectProperty parameterValues() {
        if (ms_parameterValues == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("parameterValues");
            property.getDomain().add(OTClasses.MultiParameter());
            property.getRange().add(OTClasses.ParameterValue());
            ms_parameterValues = property;
        }
        return ms_parameterValues;
    }

    public static OTObjectProperty variableValues() {
        if (ms_variableValues == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("variableValues");
            property.getDomain().add(OTClasses.ParameterValue());
            property.getRange().add(OTClasses.VariableValue());
            ms_variableValues = property;
        }
        return ms_variableValues;
    }

    /**
     * Assigns a multi-parameter (ot:MultiParameter) to a Model or an Algorithm
     * @return
     *      Object Property ot:multiParameter
     */
    public static OTObjectProperty multiParameter() {
        if (ms_multiParameter == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("multiParameter");
            property.getDomain().add(OTClasses.Model());
            property.getDomain().add(OTClasses.Algorithm());
            property.getRange().add(OTClasses.MultiParameter());
            ms_multiParameter = property;
        }
        return ms_multiParameter;
    }

    public static OTObjectProperty variable() {
        if (ms_variable == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("variable");
            property.getDomain().add(OTClasses.VariableValue());
            property.getRange().add(OTClasses.VariableInfo());
            ms_variable = property;
        }
        return ms_variable;
    }

    public static OTObjectProperty listElement() {
        if (ms_listElement == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("listElement");

            ms_listElement = property;
        }

        return ms_listElement;
    }
}
