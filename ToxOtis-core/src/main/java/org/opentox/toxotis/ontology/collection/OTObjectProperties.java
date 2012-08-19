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
public final class OTObjectProperties {

    private OTObjectProperties() {
    }
    private static OTObjectProperty compound;
    private static OTObjectProperty conformer;
    private static OTObjectProperty dataEntry;
    private static OTObjectProperty feature;
    private static OTObjectProperty model;
    private static OTObjectProperty algorithm;
    private static OTObjectProperty dependentVariables;
    private static OTObjectProperty independentVariables;
    private static OTObjectProperty predictedVariables;
    private static OTObjectProperty parameters;
    private static OTObjectProperty trainingDataset;
    private static OTObjectProperty values;
    private static OTObjectProperty trace;
    private static OTObjectProperty errorReport;
    private static OTObjectProperty bibtex;
    private static OTObjectProperty listElement;
    private static OTObjectProperty hasSource;
    private static OTObjectProperty variableInfo;
    private static OTObjectProperty parameterValues;
    private static OTObjectProperty variableValues;
    private static OTObjectProperty variable;
    private static OTObjectProperty multiParameter;


    private static Map<String, Method> methodCache;

    private synchronized static void initMethodCache() {
        if (methodCache == null) {
            methodCache = new HashMap<String, Method>();
            for (Method method : OTObjectProperties.class.getDeclaredMethods()) {
                if (OTObjectProperty.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    methodCache.put(method.getName(), method);
                }
            }
        }
    }

    public static OTObjectProperty forName(String name) {
        initMethodCache();
        try {
            Method method = methodCache.get(name);
            if (method == null) {
                throw new IllegalArgumentException("No such object property : " + name);
            }
            OTObjectProperty oc = (OTObjectProperty) method.invoke(null);
            return oc;
        } catch (final IllegalAccessException ex){
            throw new RuntimeException(ex);
        }catch (final  InvocationTargetException ex){
            throw new RuntimeException(ex);
        }

    }

    public static OTObjectProperty compound() {
        if (compound == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("compound");
            property.getDomain().add(OTClasses.dataEntry());
            property.getRange().add(OTClasses.compound());
            property.getMetaInfo().addComment("A DataEntry is defined with a "
                    + "single compound and multiple feature values. "
                    + "This property sets the relationship between a DataEntry and a Compound");
            compound = property;
        }
        return compound;
    }

    public static OTObjectProperty conformer() {
        if (conformer == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("conformer");
            property.getDomain().add(OTClasses.compound());
            property.getRange().add(OTClasses.conformer());
            conformer = property;
        }
        return conformer;
    }

    public static OTObjectProperty dataEntry() {
        if (dataEntry == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("dataEntry");
            property.getDomain().add(OTClasses.dataset());
            property.getRange().add(OTClasses.dataEntry());
            property.getMetaInfo().addComment("A Dataset contains multiple DataEntries.  "
                    + "This property specifies the relationship between Dataset and DataEntry.");
            dataEntry = property;
        }
        return dataEntry;
    }

    public static OTObjectProperty feature() {
        if (feature == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("feature");
            property.getDomain().add(OTClasses.featureValuePair());
            property.getRange().add(OTClasses.feature());
            property.getMetaInfo().addDescription("FeatureValue contains a value for "
                    + "specific Feature, specified by this relationship.");
            property.getMetaInfo().addComment("feature: FeatureValuePair --> Feature");
            feature = property;
        }
        return feature;
    }

    public static OTObjectProperty model() {
        if (model == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("model");
            property.getDomain().add(OTClasses.model());
            property.getRange().add(OTClasses.dataset());
            property.getMetaInfo().addComment("Superproperty for other properties such as "
                    + "algorithm:Model-->Algorithm, dependentVariables: Model --> Feature and other");
            model = property;
        }
        return model;
    }

    public static OTObjectProperty algorithm() {
        if (algorithm == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("algorithm");
            property.getDomain().add(OTClasses.model());
            property.getRange().add(OTClasses.algorithm());
            property.getSuperProperties().add(model());
            property.getMetaInfo().addDescription("The algorithm, used to create the Model");
            algorithm = property;
        }
        return algorithm;
    }

    public static OTObjectProperty predictedVariables() {
        if (predictedVariables == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("predictedVariables");
            property.getDomain().add(OTClasses.model());
            property.getRange().add(OTClasses.feature());
            property.getSuperProperties().add(model());
            property.getMetaInfo().addDescription("Variables, holding the predicted "
                    + "values, generated by the model");
            predictedVariables = property;
        }
        return predictedVariables;
    }

    public static OTObjectProperty dependentVariables() {
        if (dependentVariables == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("dependentVariables");
            property.getDomain().add(OTClasses.model());
            property.getRange().add(OTClasses.feature());
            property.getSuperProperties().add(model());
            property.getMetaInfo().addDescription("A model can have one or more "
                    + "dependent variables, described as multiple features, "
                    + "specified by this relationship.");
            dependentVariables = property;
        }
        return dependentVariables;
    }

    public static OTObjectProperty independentVariables() {
        if (independentVariables == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("independentVariables");
            property.getDomain().add(OTClasses.model());
            property.getRange().add(OTClasses.feature());
            property.getSuperProperties().add(model());
            property.getMetaInfo().addDescription("A model can have multiple "
                    + "independent variables, described as multiple features, "
                    + "specified by this relationship.");
            independentVariables = property;
        }
        return independentVariables;
    }

    public static OTObjectProperty parameters() {
        if (parameters == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("parameters");
            property.getDomain().add(OTClasses.model());
            property.getDomain().add(OTClasses.algorithm());
            property.getRange().add(OTClasses.parameter());
            property.getSuperProperties().add(model());
            property.getMetaInfo().addDescription("An object property that assigns a parameter to a Model "
                    + "(parameters: Model --> Parameter) or an Algorithm (parameters: Algorithm --> Parameter)");
            property.getMetaInfo().addComment("Algorithms and Models can have multiple parameters");
            parameters = property;
        }
        return parameters;
    }

    public static OTObjectProperty trainingDataset() {
        if (trainingDataset == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("trainingDataset");
            property.getDomain().add(OTClasses.model());
            property.getRange().add(OTClasses.dataset());
            property.getSuperProperties().add(model());
            property.getMetaInfo().addDescription("A model is derived by applying "
                    + "an Algorithm on a training Dataset.");
            trainingDataset = property;
        }
        return trainingDataset;
    }

    public static OTObjectProperty values() {
        if (values == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("values");
            property.getDomain().add(OTClasses.dataEntry());
            property.getRange().add(OTClasses.featureValuePair());
            property.getMetaInfo().addDescription("A DataEntry is defined with "
                    + "a single compound and multiple feature values. This property "
                    + "sets the relationship between a DataEntry and multiple FeatureValues");
            values = property;
        }
        return values;
    }

    public static OTObjectProperty trace() {
        if (trace == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("trace");
            property.getDomain().add(OTClasses.dataEntry());
            property.getRange().add(OTClasses.featureValuePair());
            property.getMetaInfo().addDescription("Some other ErrorReport corresponding "
                    + "to an exceptional event that triggered this one. Is particularly "
                    + "useful when a service acts as a proxy or gateway to other services.");
            property.getMetaInfo().addComment("trace: ErrorReport --> ErrorReport");
            trace = property;
        }
        return trace;
    }

    public static OTObjectProperty errorReport() {
        if (errorReport == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("errorReport");
            property.getDomain().add(OTClasses.task());
            property.getRange().add(OTClasses.featureValuePair());
            property.getMetaInfo().addDescription("Assigns an error report generated by a failed task.");
            property.getMetaInfo().addComment("errorReport: Task --> ErrorReport");
            errorReport = property;
        }
        return errorReport;
    }

    public static OTObjectProperty bibTex() {
        if (bibtex == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("bibtex");
            property.getDomain().add(OTClasses.algorithm());
            property.getDomain().add(OTClasses.model());
            property.getDomain().add(OTClasses.dataset());
            property.getDomain().add(OTClasses.compound());
            property.getDomain().add(OTClasses.feature());
            property.getRange().add(KnoufBibTex.entry());
            property.getMetaInfo().addDescription("Assigns a bibliographic reference to an OpenTox resource "
                    + "such as an algorith, a model, a dataset or other resource of interest.");
            property.getMetaInfo().addComment("errorReport: OpeTox BibTex Subject --> BibTeX");
            bibtex = property;
        }
        return bibtex;
    }

    public static OTObjectProperty hasSource() {
        if (hasSource == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("hasSource");
            property.getDomain().add(OTClasses.task());
            property.getDomain().add(OTClasses.feature());
            property.getRange().add(OTClasses.algorithm());
            property.getRange().add(OTClasses.dataset());
            property.getRange().add(OTClasses.model());
            hasSource = property;
        }
        return hasSource;
    }

    public static OTObjectProperty variableInfo() {
        if (variableInfo == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("variableInfo");
            property.getDomain().add(OTClasses.setValuedParameter());
            property.getRange().add(OTClasses.variableInfo());
            variableInfo = property;
        }
        return variableInfo;
    }

    public static OTObjectProperty parameterValues() {
        if (parameterValues == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("parameterValues");
            property.getDomain().add(OTClasses.multiParameter());
            property.getRange().add(OTClasses.parameterValue());
            parameterValues = property;
        }
        return parameterValues;
    }

    public static OTObjectProperty variableValues() {
        if (variableValues == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("variableValues");
            property.getDomain().add(OTClasses.parameterValue());
            property.getRange().add(OTClasses.variableValue());
            variableValues = property;
        }
        return variableValues;
    }

    /**
     * Assigns a multi-parameter (ot:MultiParameter) to a Model or an Algorithm
     * @return
     *      Object Property ot:multiParameter
     */
    public static OTObjectProperty multiParameter() {
        if (multiParameter == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("multiParameter");
            property.getDomain().add(OTClasses.model());
            property.getDomain().add(OTClasses.algorithm());
            property.getRange().add(OTClasses.multiParameter());
            multiParameter = property;
        }
        return multiParameter;
    }

    public static OTObjectProperty variable() {
        if (variable == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("variable");
            property.getDomain().add(OTClasses.variableValue());
            property.getRange().add(OTClasses.variableInfo());
            variable = property;
        }
        return variable;
    }

    public static OTObjectProperty listElement() {
        if (listElement == null) {
            OTObjectProperty property = new OTObjectPropertyImpl("listElement");

            listElement = property;
        }

        return listElement;
    }
}
