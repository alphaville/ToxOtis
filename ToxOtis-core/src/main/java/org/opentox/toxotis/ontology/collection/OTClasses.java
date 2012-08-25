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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 * Collection of all Ontological Classes defined in OpenTox.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class OTClasses {

    private OTClasses() {
    }
    /**
     * The OpenTox ontology NameSpace.
     */
    public static final String NS = "http://www.opentox.org/api/1.1#";
    private static OntologicalClass openToxResource;
    private static OntologicalClass algorithm;
    private static OntologicalClass compound;
    private static OntologicalClass conformer;
    private static OntologicalClass dataEntry;
    private static OntologicalClass dataType;
    private static OntologicalClass nominal;
    private static OntologicalClass numeric;
    private static OntologicalClass string;
    private static OntologicalClass nominalFeature;
    private static OntologicalClass numericFeature;
    private static OntologicalClass stringFeature;
    private static OntologicalClass dataset;
    private static OntologicalClass feature;
    private static OntologicalClass featureValue;
    private static OntologicalClass featureValueNumeric;
    private static OntologicalClass featureValueString;
    private static OntologicalClass featureValueNominal;
    private static OntologicalClass featureValuePair;
    private static OntologicalClass model;
    private static OntologicalClass parameter;
    private static OntologicalClass qprfReport;
    private static OntologicalClass report;
    private static OntologicalClass multiParameter;
    private static OntologicalClass vectorParameter;
    private static OntologicalClass variableInfo;
    private static OntologicalClass parameterValue;
    private static OntologicalClass setValuedParameter;
    private static OntologicalClass variableValue;
    private static OntologicalClass task;
    private static OntologicalClass errorReport;
    private static OntologicalClass thing;
    private static Map<String, Method> methodCache;
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OTClasses.class);

    private synchronized static void initMethodCache() {
        if (methodCache == null) {
            methodCache = new HashMap<String, Method>();
            for (Method method : OTClasses.class.getDeclaredMethods()) {
                if (OntologicalClass.class.equals(method.getReturnType())
                        && method.getParameterTypes().length == 0) {
                    try {
                        OntologicalClass oc = (OntologicalClass) method.invoke(null);
                        methodCache.put(oc.getName(), method);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(OTClasses.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(OTClasses.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(OTClasses.class.getName()).log(Level.SEVERE, null, ex);
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
            Method method = methodCache.get(name);
            if (method == null) {
                return null;
            }
            return (OntologicalClass) method.invoke(null);
        } catch (final IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        } catch (final InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Every individual in the OWL world is a member of the class <code>owl:Thing</code>.
     * Every ontological class of the OpenTox ontology is a subclass of <code>owl:Thing</code>.
     * @return
     *      Universal class <code>owl:Thing</code>.
     */
    public static OntologicalClass thing() {
        if (thing == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Thing");
            clazz.setNameSpace(OWL.NS);
            thing = clazz;
        }
        return thing;
    }

    /**
     * Superclass for all OpenTox resources. All classes of the OpenTox ontologies,
     * subclass ot:OpenToxResource
     * @return
     *      OpenTox superclass.
     */
    public static OntologicalClass openToxResource() {
        if (openToxResource == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OpenToxResource");
            clazz.getMetaInfo().addComment("Generic OpenTox Resource");
            clazz.getSuperClasses().add(thing());
            openToxResource = clazz;
        }
        return openToxResource;
    }

    /**
     * Ontological class that provides access to all OpenTox algorithms
     * @see OTAlgorithmTypes OpenTox Algorithm Ontology
     */
    public static OntologicalClass algorithm() {
        if (algorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Algorithm");
            clazz.getMetaInfo().addComment("Provides access to OpenTox algorithms");
            clazz.getSuperClasses().add(openToxResource());
            algorithm = clazz;
        }
        return algorithm;
    }

    /**
     * Any kind of report (QPRF, QMRP or other).
     * @return 
     *      Ontological class for reports.
     */
    public static OntologicalClass report() {
        if (report == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Report");
            clazz.getMetaInfo().addComment("An OpenTox-relevant report");
            clazz.getSuperClasses().add(openToxResource());
            report = clazz;
        }
        return report;
    }

    /**
     * A QPRF report.
     */
    public static OntologicalClass qprfReport() {
        if (qprfReport == null) {
            OntologicalClass clazz = new OntologicalClassImpl("QPRFReport");
            clazz.getMetaInfo().addComment("A QPRF report");
            clazz.getSuperClasses().add(report());
            qprfReport = clazz;
        }
        return qprfReport;
    }

    /**
     * A compound.
     */
    public static OntologicalClass compound() {
        if (compound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Compound");
            clazz.getMetaInfo().addComment("API for OpenTox compound webservices");
            clazz.getSuperClasses().add(openToxResource());
            compound = clazz;
        }
        return compound;
    }

    /**
     * A conformer.
     */
    public static OntologicalClass conformer() {
        if (conformer == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Conformer");
            clazz.getMetaInfo().addComment("API for OpenTox conformer webservices");
            clazz.getSuperClasses().add(compound());
            conformer = clazz;
        }
        return conformer;
    }

    /**
     * A model.
     */
    public static OntologicalClass model() {
        if (model == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Model");
            clazz.getSuperClasses().add(openToxResource());
            model = clazz;
        }
        return model;
    }

    public static OntologicalClass dataEntry() {
        if (dataEntry == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DataEntry");
            clazz.getSuperClasses().add(openToxResource());
            dataEntry = clazz;
        }
        return dataEntry;
    }

    public static OntologicalClass dataType() {
        if (dataType == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DataType");
            clazz.getSuperClasses().add(openToxResource());
            dataType = clazz;
        }
        return dataType;
    }

    /**
     * A Dataset Provides access to chemical compounds
     * and their features (e.g. structural, physical-chemical,
     * biological, toxicological properties)
     * @return
     *      Ontological class for Datasets
     */
    public static OntologicalClass dataset() {
        if (dataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Dataset");
            clazz.getMetaInfo().addComment("Provides access to chemical compounds "
                    + "and their features (e.g. structural, physical-chemical, "
                    + "biological, toxicological properties)");
            clazz.getSuperClasses().add(openToxResource());
            dataset = clazz;
        }
        return dataset;
    }

    public static OntologicalClass feature() {
        if (feature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Feature");
            clazz.getSuperClasses().add(openToxResource());
            feature = clazz;
        }
        return feature;
    }

    public static OntologicalClass featureValuePair() {
        if (featureValuePair == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValuePair");
            clazz.getSuperClasses().add(openToxResource());
            featureValuePair = clazz;
        }
        return featureValuePair;
    }

    public static OntologicalClass featureValue() {
        if (featureValue == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValue");
            clazz.getSuperClasses().add(featureValuePair());
            featureValue = clazz;
        }
        return featureValue;
    }

    public static OntologicalClass featureValueNumeric() {
        if (featureValueNumeric == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValueNumeric");
            clazz.getSuperClasses().add(featureValue());
            featureValueNumeric = clazz;
        }
        return featureValueNumeric;
    }

    public static OntologicalClass featureValueString() {
        if (featureValueString == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValue");
            clazz.getSuperClasses().add(featureValue());
            featureValueString = clazz;
        }
        return featureValueString;
    }

    public static OntologicalClass featureValueNominal() {
        if (featureValueNominal == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValueNominal");
            clazz.getSuperClasses().add(featureValueString());
            featureValueNominal = clazz;
        }
        return featureValueNominal;
    }

    public static OntologicalClass nominal() {
        if (nominal == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Nominal");
            clazz.getSuperClasses().add(dataType());
            nominal = clazz;
        }
        return nominal;
    }

    public static OntologicalClass nominalFeature() {
        if (nominalFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("NominalFeature");
            clazz.getSuperClasses().add(feature());
            clazz.getSuperClasses().add(nominal());
            nominalFeature = clazz;
        }
        return nominalFeature;
    }

    public static OntologicalClass numeric() {
        if (numeric == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Numeric");
            clazz.getSuperClasses().add(dataType());
            numeric = clazz;
        }
        return numeric;
    }

    public static OntologicalClass numericFeature() {
        if (numericFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("NumericFeature");
            clazz.getSuperClasses().add(feature());
            clazz.getSuperClasses().add(numeric());
            numericFeature = clazz;
        }
        return numericFeature;
    }

    public static OntologicalClass string() {
        if (string == null) {
            OntologicalClass clazz = new OntologicalClassImpl("String");
            clazz.getSuperClasses().add(dataType());
            string = clazz;
        }
        return string;
    }

    public static OntologicalClass stringFeature() {
        if (stringFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("StringFeature");
            clazz.getSuperClasses().add(feature());
            clazz.getSuperClasses().add(string());
            stringFeature = clazz;
        }
        return stringFeature;
    }

    public static OntologicalClass parameter() {
        if (parameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Parameter");
            clazz.getSuperClasses().add(openToxResource());
            parameter = clazz;
        }
        return parameter;
    }

    public static OntologicalClass vectorParameter() {
        if (vectorParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("VectorParameter");
            clazz.getSuperClasses().add(multiParameter());
            vectorParameter = clazz;
        }
        return vectorParameter;
    }

    public static OntologicalClass variableInfo() {
        if (variableInfo == null) {
            OntologicalClass clazz = new OntologicalClassImpl("VariableInfo");
            clazz.getSuperClasses().add(openToxResource());
            variableInfo = clazz;
        }
        return variableInfo;
    }

    public static OntologicalClass parameterValue() {
        if (parameterValue == null) {
            OntologicalClass clazz = new OntologicalClassImpl("ParameterValue");
            clazz.getSuperClasses().add(openToxResource());
            parameterValue = clazz;
        }
        return parameterValue;
    }

    public static OntologicalClass multiParameter() {
        if (multiParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MultiParameter");
            clazz.getSuperClasses().add(openToxResource());
            clazz.getDisjointWith().add(parameter());// << Different from Parameter
            multiParameter = clazz;
        }
        return multiParameter;
    }

    public static OntologicalClass setValuedParameter() {
        if (setValuedParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("SetValuedParameter");
            clazz.getSuperClasses().add(multiParameter());
            setValuedParameter = clazz;
        }
        return setValuedParameter;
    }

    public static OntologicalClass variableValue() {
        if (variableValue == null) {
            OntologicalClass clazz = new OntologicalClassImpl("VariableValue");
            clazz.getSuperClasses().add(multiParameter());
            variableValue = clazz;
        }
        return variableValue;
    }

    public static OntologicalClass task() {
        if (task == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Task");
            clazz.getSuperClasses().add(feature());
            clazz.getSuperClasses().add(string());
            task = clazz;
        }
        return task;
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
    public static OntologicalClass errorReport() {
        if (errorReport == null) {
            OntologicalClass clazz = new OntologicalClassImpl("ErrorReport");
            clazz.getSuperClasses().add(openToxResource());
            clazz.getMetaInfo().addDescription("Instances of the class ErrorReport "
                    + "appear in ontological models to report some exceptional event "
                    + "that happened during the transaction of data from one server "
                    + "to another, request errors or internal server errors. An error "
                    + "report is characterized by its actor, the corresponding status code, "
                    + "some unique identifier for the exceptional event and possibly "
                    + "another error report that triggered its creation.");
            errorReport = clazz;
        }
        return errorReport;
    }
}
