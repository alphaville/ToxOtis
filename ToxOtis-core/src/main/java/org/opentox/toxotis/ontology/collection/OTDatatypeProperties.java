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

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.OTDatatypeProperty;
import org.opentox.toxotis.ontology.impl.OTDatatypePropertyImpl;

/**
 * Collection of datatype properties used in OpenTox.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class OTDatatypeProperties {

    //TODO: Populate the metadata...
    private OTDatatypeProperties() {
    }
    private static OTDatatypeProperty msHasStatus;
    private static OTDatatypeProperty msAcceptValue;
    private static OTDatatypeProperty msParamScope;
    private static OTDatatypeProperty msParamValue;
    private static OTDatatypeProperty msResultURI;
    private static OTDatatypeProperty msValue;
    private static OTDatatypeProperty msUnits;
    private static OTDatatypeProperty msClassificationStatistics;
    private static OTDatatypeProperty msPercentageCompleted;
    private static OTDatatypeProperty msErrorReportProperty;
    private static OTDatatypeProperty msActor;
    private static OTDatatypeProperty msDetails;
    private static OTDatatypeProperty msErrorCode;
    private static OTDatatypeProperty msHttpStatus;
    private static OTDatatypeProperty msMessage;
    private static OTDatatypeProperty msIndex;
    private static OTDatatypeProperty msDuration;
    private static Map<String, Method> msMethodCache;

    private synchronized static void initMethodCache() {
        if (msMethodCache == null) {
            msMethodCache = new HashMap<String, Method>();
            for (Method method : OTDatatypeProperties.class.getDeclaredMethods()) {
                if (OTDatatypeProperty.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    msMethodCache.put(method.getName(), method);
                }
            }
        }
    }

    /**
     * Return a datatype property given its name. The input argument of this method
     * is case sensitive and this in some cases might cause an exception to be
     * thrown. This method performs reflective lookups only once (when invoked for
     * the first time)  for efficiency.
     * @param name
     *      Name of the datatype property to search for
     * @return
     *      The datatype property that was requested
     * @throws ToxOtisException
     *      In case the requested datatype property is not found in the cache.
     */
    public static OTDatatypeProperty forName(String name) throws ToxOtisException {
        initMethodCache();
        try {
            Method method = msMethodCache.get(name);
            if (method == null) {
                throw new ToxOtisException("OTDatatypePropertyNotFound: The property '"
                        + name + "' was not found in the cache.");
            }
            OTDatatypeProperty oc = (OTDatatypeProperty) method.invoke(null);
            return oc;
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
        }

    }

    /**
     * The property <code>ot:hasStatus</code> assigns status values to Tasks. The
     * domain of this property is the ontological class {@link OTClasses#Task() }
     * and its range is <code>xsd:string</code>. Acceptable values are <code>RUNNING</code>,
     * <code>COMPLETED</code>, <code>ERROR</code> and <code>CANCELED</code>.
     * @return
     *      The property ot:hasStatus
     */
    public static OTDatatypeProperty hasStatus() {
        if (msHasStatus == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasStatus");
            property.getDomain().add(OTClasses.task());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("has status");
            msHasStatus = property;
        }
        return msHasStatus;
    }

    /**
     * Datatype property used to link an <code>ot:Nominal</code> with its admissible
     * values. This property is also used to assign range values to Nominal Features.
     * The domain of this property is the class {@link OTClasses#Nominal() } and its
     * range is <code>xsd:string</code>.
     * @return
     *      The datatype property ot:acceptValue
     */
    public static OTDatatypeProperty acceptValue() {
        if (msAcceptValue == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("acceptValue");
            property.getDomain().add(OTClasses.nominal());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("accepts value");
            msAcceptValue = property;
        }
        return msAcceptValue;
    }

    public static OTDatatypeProperty paramScope() {
        if (msParamScope == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("paramScope");
            property.getDomain().add(OTClasses.parameter());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("parameter scope");
            property.getMetaInfo().addDescription("specifies if a parameter is "
                    + "optional or mandatory");
            msParamScope = property;
        }
        return msParamScope;
    }

    /**
     * A parameter value.
     */
    public static OTDatatypeProperty paramValue() {
        if (msParamValue == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("paramValue");
            property.getDomain().add(OTClasses.parameter());
            property.getDomain().add(OTClasses.variableValue());
            property.getMetaInfo().addTitle("parameter value");
            property.getMetaInfo().addDescription("The value of a Parameter or the value " +
                    "of a variable in a set of variables");
            msParamValue = property;
        }
        return msParamValue;
    }

    /**
     * The result URI of a task.
     */
    public static OTDatatypeProperty resultURI() {
        if (msResultURI == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("resultURI");
            property.getDomain().add(OTClasses.task());
            property.getRange().add(XSDDatatype.XSDanyURI);
            property.getMetaInfo().addDescription("URI of the new resource, "
                    + "created by the Task is stored here");
            msResultURI = property;
        }
        return msResultURI;
    }

    /**
     * An actual value.
     */
    public static OTDatatypeProperty value() {
        if (msValue == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("value");
            property.getDomain().add(OTClasses.featureValue());
            property.getDomain().add(OTClasses.variableValue());
            property.getMetaInfo().addDescription("Value of a feature value pair (class FeatureValuePair), also used to assign values to ot:VariableValue resources");
            msValue = property;
        }
        return msValue;
    }

    /**
     * Units of measure.
     */
    public static OTDatatypeProperty units() {
        if (msUnits == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("units");
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addTitle("Units");
            property.getMetaInfo().addDescription("Units of a feature value");
            msUnits = property;
        }
        return msUnits;
    }

    /**
     * Classification statistics.
     */
    public static OTDatatypeProperty classificationStatistics() {
        if (msClassificationStatistics == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("classificationStatistics");
            msClassificationStatistics = property;
        }
        return msClassificationStatistics;
    }

    /**
     * The percentage to which a task is complete.
     */
    public static OTDatatypeProperty percentageCompleted() {
        if (msPercentageCompleted == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("percentageCompleted");
            property.getDomain().add(OTClasses.task());
            property.getRange().add(XSDDatatype.XSDfloat);
            property.getSuperProperties().add(classificationStatistics());
            property.getMetaInfo().addDescription("Estimated percentage of completion of a "
                    + "running task.");
            msPercentageCompleted = property;
        }
        return msPercentageCompleted;
    }

    /**
     * This is a generic property. Subproperties usually used are: <code>ot:actor</code>
     * <code>ot:message</code>, <code>ot:details</code>, <code>ot:httpStatus</code> and
     * <code>ot:errorCode</code>.
     * @return
     *      The error report super-property.
     */
    public static OTDatatypeProperty errorReportProperty() {
        if (msErrorReportProperty == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("errorReportProperty");
            property.getDomain().add(OTClasses.errorReport());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().addDescription("Datatype property applied on an error report.");
            property.getMetaInfo().addComment("This is a generic property. Subproperties usually used are: actor, "
                    + "message, details, status and errorCode.");
            msErrorReportProperty = property;
        }
        return msErrorReportProperty;
    }

    /**
     * The actor of an exceptional event.
     */
    public static OTDatatypeProperty actor() {
        if (msActor == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("actor");
            property.getDomain().add(OTClasses.errorReport());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getSuperProperties().add(errorReportProperty());
            property.getMetaInfo().addDescription("URI of the peer "
                    + "that produces the exception.");
            property.getMetaInfo().addTitle("error actor");
            msActor = property;
        }
        return msActor;
    }

    /**
     * A message returned by an exceptional event providing
     * very essential information about it.
     */
    public static OTDatatypeProperty message() {
        if (msMessage == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("message");
            property.getDomain().add(OTClasses.errorReport());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getSuperProperties().add(errorReportProperty());
            property.getMetaInfo().addDescription("A simple message providing some "
                    + "simple description of the exceptional event.");
            property.getMetaInfo().addComment("For example: 'Prediction feature not provided'");
            property.getMetaInfo().addTitle("error message");
            msMessage = property;
        }
        return msMessage;
    }

    /**
     * A detailed message with debugging information returned to the
     * client when an exceptional even occurs.
     */
    public static OTDatatypeProperty details() {
        if (msDetails == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("details");
            property.getDomain().add(OTClasses.errorReport());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getSuperProperties().add(errorReportProperty());
            property.getMetaInfo().addDescription("Detailed message including "
                    + "technical information about the exceptional event. "
                    + "Can be used to help both peers in the debugging.");
            property.getMetaInfo().addTitle("error details");
            msDetails = property;
        }
        return msDetails;
    }

    /**
     * The HTTP status.
     */
    public static OTDatatypeProperty httpStatus() {
        if (msHttpStatus == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("httpStatus");
            property.getDomain().add(OTClasses.errorReport());
            property.getRange().add(XSDDatatype.XSDint);
            property.getSuperProperties().add(errorReportProperty());
            property.getMetaInfo().addTitle("HTTP status");
            msHttpStatus = property;
        }
        return msHttpStatus;
    }

    /**
     * An error code.
     */
    public static OTDatatypeProperty errorCode() {
        if (msErrorCode == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("errorCode");
            property.getDomain().add(OTClasses.errorReport());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getSuperProperties().add(errorReportProperty());
            property.getMetaInfo().addDescription("Error codes of error reports "
                    + "are specified by the service providers that produce the"
                    + " error report. These are characteristic for every class of exceptional events.");
            property.getMetaInfo().addTitle("error code");
            msErrorCode = property;
        }
        return msErrorCode;
    }

    /**
     * An index for a parameter value.
     */
    public static OTDatatypeProperty index(){
        if(msIndex == null){
           OTDatatypeProperty property = new OTDatatypePropertyImpl("index");
            property.getDomain().add(OTClasses.parameterValue());
            property.getRange().add(XSDDatatype.XSDinteger);
            msIndex = property;
        }
        return msIndex;
    }

    /**
     * The duration of a task.
     */
    public static OTDatatypeProperty duration(){
        if(msDuration == null){
           OTDatatypeProperty property = new OTDatatypePropertyImpl("duration");
            property.getDomain().add(OTClasses.task());
            property.getRange().add(XSDDatatype.XSDlong);
            msDuration = property;
        }
        return msDuration;
    }
}
