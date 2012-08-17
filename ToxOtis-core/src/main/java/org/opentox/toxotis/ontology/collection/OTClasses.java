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
 * Collection of all Ontological Classes defined in OpenTox.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTClasses {

    private OTClasses() {
    }
    public static final String NS = "http://www.opentox.org/api/1.1#";
    private static OntologicalClass OpenToxResource;
    private static OntologicalClass Algorithm;
    private static OntologicalClass Compound;
    private static OntologicalClass Conformer;
    private static OntologicalClass DataEntry;
    private static OntologicalClass DataType;
    private static OntologicalClass Nominal;
    private static OntologicalClass Numeric;
    private static OntologicalClass String;
    private static OntologicalClass NominalFeature;
    private static OntologicalClass NumericFeature;
    private static OntologicalClass StringFeature;
    private static OntologicalClass Dataset;
    private static OntologicalClass Feature;
    private static OntologicalClass FeatureValue;
    private static OntologicalClass FeatureValueNumeric;
    private static OntologicalClass FeatureValueString;
    private static OntologicalClass FeatureValueNominal;
    private static OntologicalClass FeatureValuePair;
    private static OntologicalClass Model;
    private static OntologicalClass Parameter;
    private static OntologicalClass QPRFReport;
    private static OntologicalClass Report;
    private static OntologicalClass MultiParameter;
    private static OntologicalClass VectorParameter;
    private static OntologicalClass VariableInfo;
    private static OntologicalClass ParameterValue;
    private static OntologicalClass SetValuedParameter;
    private static OntologicalClass VariableValue;
    private static OntologicalClass Task;
    private static OntologicalClass ErrorReport;
    private static OntologicalClass Thing;
    private static Map<String, Method> methodCache;
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OTClasses.class);

    private synchronized static void initMethodCache() {
        if (methodCache == null) {
            methodCache = new HashMap<String, Method>();
            for (Method method : OTClasses.class.getDeclaredMethods()) {
                if (OntologicalClass.class.equals(method.getReturnType()) 
                        && method.getParameterTypes().length == 0) {
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
        if (Thing == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Thing");
            clazz.setNameSpace(OWL.NS);
            Thing = clazz;
        }
        return Thing;
    }

    /**
     * Superclass for all OpenTox resources. All classes of the OpenTox ontologies,
     * subclass ot:OpenToxResource
     * @return
     *      OpenTox superclass.
     */
    public static OntologicalClass OpenToxResource() {
        if (OpenToxResource == null) {
            OntologicalClass clazz = new OntologicalClassImpl("OpenToxResource");
            clazz.getMetaInfo().addComment("Generic OpenTox Resource");
            clazz.getSuperClasses().add(Thing());
            OpenToxResource = clazz;
        }
        return OpenToxResource;
    }

    /**
     * Ontological class that provides access to all OpenTox algorithms
     * @see OTAlgorithmTypes OpenTox Algorithm Ontology
     */
    public static OntologicalClass Algorithm() {
        if (Algorithm == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Algorithm");
            clazz.getMetaInfo().addComment("Provides access to OpenTox algorithms");
            clazz.getSuperClasses().add(OpenToxResource());
            Algorithm = clazz;
        }
        return Algorithm;
    }

    public static OntologicalClass Report() {
        if (Report == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Report");
            clazz.getMetaInfo().addComment("An OpenTox-relevant report");
            clazz.getSuperClasses().add(OpenToxResource());
            Report = clazz;
        }
        return Report;
    }

    public static OntologicalClass QPRFReport() {
        if (QPRFReport == null) {
            OntologicalClass clazz = new OntologicalClassImpl("QPRFReport");
            clazz.getMetaInfo().addComment("A QPRF report");
            clazz.getSuperClasses().add(Report());
            QPRFReport = clazz;
        }
        return QPRFReport;
    }

    public static OntologicalClass Compound() {
        if (Compound == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Compound");
            clazz.getMetaInfo().addComment("API for OpenTox compound webservices");
            clazz.getSuperClasses().add(OpenToxResource());
            Compound = clazz;
        }
        return Compound;
    }

    public static OntologicalClass Conformer() {
        if (Conformer == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Conformer");
            clazz.getMetaInfo().addComment("API for OpenTox conformer webservices");
            clazz.getSuperClasses().add(Compound());
            Conformer = clazz;
        }
        return Conformer;
    }

    public static OntologicalClass Model() {
        if (Model == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Model");
            clazz.getSuperClasses().add(OpenToxResource());
            Model = clazz;
        }
        return Model;
    }

    public static OntologicalClass DataEntry() {
        if (DataEntry == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DataEntry");
            clazz.getSuperClasses().add(OpenToxResource());
            DataEntry = clazz;
        }
        return DataEntry;
    }

    public static OntologicalClass DataType() {
        if (DataType == null) {
            OntologicalClass clazz = new OntologicalClassImpl("DataType");
            clazz.getSuperClasses().add(OpenToxResource());
            DataType = clazz;
        }
        return DataType;
    }

    /**
     * A Dataset Provides access to chemical compounds
     * and their features (e.g. structural, physical-chemical,
     * biological, toxicological properties)
     * @return
     *      Ontological class for Datasets
     */
    public static OntologicalClass Dataset() {
        if (Dataset == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Dataset");
            clazz.getMetaInfo().addComment("Provides access to chemical compounds "
                    + "and their features (e.g. structural, physical-chemical, "
                    + "biological, toxicological properties)");
            clazz.getSuperClasses().add(OpenToxResource());
            Dataset = clazz;
        }
        return Dataset;
    }

    public static OntologicalClass Feature() {
        if (Feature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Feature");
            clazz.getSuperClasses().add(OpenToxResource());
            Feature = clazz;
        }
        return Feature;
    }

    public static OntologicalClass FeatureValuePair() {
        if (FeatureValuePair == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValuePair");
            clazz.getSuperClasses().add(OpenToxResource());
            FeatureValuePair = clazz;
        }
        return FeatureValuePair;
    }

    public static OntologicalClass FeatureValue() {
        if (FeatureValue == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValue");
            clazz.getSuperClasses().add(FeatureValuePair());
            FeatureValue = clazz;
        }
        return FeatureValue;
    }

    public static OntologicalClass FeatureValueNumeric() {
        if (FeatureValueNumeric == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValueNumeric");
            clazz.getSuperClasses().add(FeatureValue());
            FeatureValueNumeric = clazz;
        }
        return FeatureValueNumeric;
    }

    public static OntologicalClass FeatureValueString() {
        if (FeatureValueString == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValue");
            clazz.getSuperClasses().add(FeatureValue());
            FeatureValueString = clazz;
        }
        return FeatureValueString;
    }

    public static OntologicalClass FeatureValueNominal() {
        if (FeatureValueNominal == null) {
            OntologicalClass clazz = new OntologicalClassImpl("FeatureValueNominal");
            clazz.getSuperClasses().add(FeatureValueString());
            FeatureValueNominal = clazz;
        }
        return FeatureValueNominal;
    }

    public static OntologicalClass Nominal() {
        if (Nominal == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Nominal");
            clazz.getSuperClasses().add(DataType());
            Nominal = clazz;
        }
        return Nominal;
    }

    public static OntologicalClass NominalFeature() {
        if (NominalFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("NominalFeature");
            clazz.getSuperClasses().add(Feature());
            clazz.getSuperClasses().add(Nominal());
            NominalFeature = clazz;
        }
        return NominalFeature;
    }

    public static OntologicalClass Numeric() {
        if (Numeric == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Numeric");
            clazz.getSuperClasses().add(DataType());
            Numeric = clazz;
        }
        return Numeric;
    }

    public static OntologicalClass NumericFeature() {
        if (NumericFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("NumericFeature");
            clazz.getSuperClasses().add(Feature());
            clazz.getSuperClasses().add(Numeric());
            NumericFeature = clazz;
        }
        return NumericFeature;
    }

    public static OntologicalClass String() {
        if (String == null) {
            OntologicalClass clazz = new OntologicalClassImpl("String");
            clazz.getSuperClasses().add(DataType());
            String = clazz;
        }
        return String;
    }

    public static OntologicalClass StringFeature() {
        if (StringFeature == null) {
            OntologicalClass clazz = new OntologicalClassImpl("StringFeature");
            clazz.getSuperClasses().add(Feature());
            clazz.getSuperClasses().add(String());
            StringFeature = clazz;
        }
        return StringFeature;
    }

    public static OntologicalClass Parameter() {
        if (Parameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Parameter");
            clazz.getSuperClasses().add(OpenToxResource());
            Parameter = clazz;
        }
        return Parameter;
    }

    public static OntologicalClass VectorParameter() {
        if (VectorParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("VectorParameter");
            clazz.getSuperClasses().add(MultiParameter());
            VectorParameter = clazz;
        }
        return VectorParameter;
    }

    public static OntologicalClass VariableInfo() {
        if (VariableInfo == null) {
            OntologicalClass clazz = new OntologicalClassImpl("VariableInfo");
            clazz.getSuperClasses().add(OpenToxResource());
            VariableInfo = clazz;
        }
        return VariableInfo;
    }

    public static OntologicalClass ParameterValue() {
        if (ParameterValue == null) {
            OntologicalClass clazz = new OntologicalClassImpl("ParameterValue");
            clazz.getSuperClasses().add(OpenToxResource());
            ParameterValue = clazz;
        }
        return ParameterValue;
    }

    public static OntologicalClass MultiParameter() {
        if (MultiParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("MultiParameter");
            clazz.getSuperClasses().add(OpenToxResource());
            clazz.getDisjointWith().add(Parameter());// << Different from Parameter
            MultiParameter = clazz;
        }
        return MultiParameter;
    }

    public static OntologicalClass SetValuedParameter() {
        if (SetValuedParameter == null) {
            OntologicalClass clazz = new OntologicalClassImpl("SetValuedParameter");
            clazz.getSuperClasses().add(MultiParameter());
            SetValuedParameter = clazz;
        }
        return SetValuedParameter;
    }

    public static OntologicalClass VariableValue() {
        if (VariableValue == null) {
            OntologicalClass clazz = new OntologicalClassImpl("VariableValue");
            clazz.getSuperClasses().add(MultiParameter());
            VariableValue = clazz;
        }
        return VariableValue;
    }

    public static OntologicalClass Task() {
        if (Task == null) {
            OntologicalClass clazz = new OntologicalClassImpl("Task");
            clazz.getSuperClasses().add(Feature());
            clazz.getSuperClasses().add(String());
            Task = clazz;
        }
        return Task;
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
        if (ErrorReport == null) {
            OntologicalClass clazz = new OntologicalClassImpl("ErrorReport");
            clazz.getSuperClasses().add(OpenToxResource());
            clazz.getMetaInfo().addDescription("Instances of the class ErrorReport "
                    + "appear in ontological models to report some exceptional event "
                    + "that happened during the transaction of data from one server "
                    + "to another, request errors or internal server errors. An error "
                    + "report is characterized by its actor, the corresponding status code, "
                    + "some unique identifier for the exceptional event and possibly "
                    + "another error report that triggered its creation.");
            ErrorReport = clazz;
        }
        return ErrorReport;
    }
}
