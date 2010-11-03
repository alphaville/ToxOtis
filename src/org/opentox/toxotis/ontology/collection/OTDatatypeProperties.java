package org.opentox.toxotis.ontology.collection;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.ontology.OTDatatypeProperty;
import org.opentox.toxotis.ontology.impl.OTDatatypePropertyImpl;

/**
 * Collection of datatype properties used in OpenTox.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTDatatypeProperties {

    //TODO: Populate the metadata...
    private OTDatatypeProperties() {
    }
    private static OTDatatypeProperty ms_hasStatus;
    private static OTDatatypeProperty ms_acceptValue;
    private static OTDatatypeProperty ms_paramScope;
    private static OTDatatypeProperty ms_paramValue;
    private static OTDatatypeProperty ms_resultURI;
    private static OTDatatypeProperty ms_value;
    private static OTDatatypeProperty ms_units;
    private static OTDatatypeProperty ms_classificationStatistics;
    private static OTDatatypeProperty ms_percentageCompleted;
    private static OTDatatypeProperty ms_errorReportProperty;
    private static OTDatatypeProperty ms_actor;
    private static OTDatatypeProperty ms_details;
    private static OTDatatypeProperty ms_errorCode;
    private static OTDatatypeProperty ms_httpStatus;
    private static OTDatatypeProperty ms_message;
    private static OTDatatypeProperty ms_index;
    private static Map<String, Method> ms_methodCache;

    private synchronized static void initMethodCache() {
        if (ms_methodCache == null) {
            ms_methodCache = new HashMap<String, Method>();
            for (Method method : OTDatatypeProperties.class.getDeclaredMethods()) {
                if (OTDatatypeProperty.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0) {
                    ms_methodCache.put(method.getName(), method);
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
            Method method = ms_methodCache.get(name);
            if (method == null) {
                throw new ToxOtisException(ErrorCause.OTDatatypePropertyNotFound, "The property '"
                        + name + "' was not found in the cache.");
            }
            OTDatatypeProperty oc = (OTDatatypeProperty) method.invoke(null);
            return oc;
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
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
        if (ms_hasStatus == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasStatus");
            property.getDomain().add(OTClasses.Task());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().setTitle("has status");
            ms_hasStatus = property;
        }
        return ms_hasStatus;
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
        if (ms_acceptValue == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("acceptValue");
            property.getDomain().add(OTClasses.Nominal());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().setTitle("accepts value");
            ms_acceptValue = property;
        }
        return ms_acceptValue;
    }

    public static OTDatatypeProperty paramScope() {
        if (ms_paramScope == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("paramScope");
            property.getDomain().add(OTClasses.Parameter());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().setTitle("parameter scope");
            property.getMetaInfo().setDescription("specifies if a parameter is "
                    + "optional or mandatory");
            ms_paramScope = property;
        }
        return ms_paramScope;
    }

    public static OTDatatypeProperty paramValue() {
        if (ms_paramValue == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("paramValue");
            property.getDomain().add(OTClasses.Parameter());
            property.getMetaInfo().setTitle("parameter value");
            property.getMetaInfo().setDescription("The value of a Parameter");
            ms_paramValue = property;
        }
        return ms_paramValue;
    }

    public static OTDatatypeProperty resultURI() {
        if (ms_resultURI == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("resultURI");
            property.getDomain().add(OTClasses.Task());
            property.getRange().add(XSDDatatype.XSDanyURI);
            property.getMetaInfo().setDescription("URI of the new resource, "
                    + "created by the Task is stored here");
            ms_resultURI = property;
        }
        return ms_resultURI;
    }

    public static OTDatatypeProperty value() {
        if (ms_value == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("value");
            property.getDomain().add(OTClasses.FeatureValue());
            property.getMetaInfo().setDescription("Value of a feature value pair (class FeatureValuePair)");
            ms_value = property;
        }
        return ms_value;
    }

    public static OTDatatypeProperty units() {
        if (ms_units == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("units");
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().setTitle("Units");
            property.getMetaInfo().setDescription("Units of a feature value");
            ms_units = property;
        }
        return ms_units;
    }

    public static OTDatatypeProperty classificationStatistics() {
        if (ms_classificationStatistics == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("classificationStatistics");
            ms_classificationStatistics = property;
        }
        return ms_classificationStatistics;
    }

    public static OTDatatypeProperty percentageCompleted() {
        if (ms_percentageCompleted == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("percentageCompleted");
            property.getDomain().add(OTClasses.Task());
            property.getRange().add(XSDDatatype.XSDfloat);
            property.getSuperProperties().add(classificationStatistics());
            property.getMetaInfo().setDescription("Estimated percentage of completion of a "
                    + "running task.");
            ms_percentageCompleted = property;
        }
        return ms_percentageCompleted;
    }

    /**
     * This is a generic property. Subproperties usually used are: <code>ot:actor</code>
     * <code>ot:message</code>, <code>ot:details</code>, <code>ot:httpStatus</code> and
     * <code>ot:errorCode</code>.
     * @return
     *      The error report super-property.
     */
    public static OTDatatypeProperty errorReportProperty() {
        if (ms_errorReportProperty == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("errorReportProperty");
            property.getDomain().add(OTClasses.ErrorReport());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getMetaInfo().setDescription("Datatype property applied on an error report.");
            property.getMetaInfo().addComment("This is a generic property. Subproperties usually used are: actor, "
                    + "message, details, status and errorCode.");
            ms_errorReportProperty = property;
        }
        return ms_errorReportProperty;
    }

    public static OTDatatypeProperty actor() {
        if (ms_actor == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("actor");
            property.getDomain().add(OTClasses.ErrorReport());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getSuperProperties().add(errorReportProperty());
            property.getMetaInfo().setDescription("URI of the peer "
                    + "that produces the exception.");
            property.getMetaInfo().setTitle("error actor");
            ms_actor = property;
        }
        return ms_actor;
    }

    public static OTDatatypeProperty message() {
        if (ms_message == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("message");
            property.getDomain().add(OTClasses.ErrorReport());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getSuperProperties().add(errorReportProperty());
            property.getMetaInfo().setDescription("A simple message providing some "
                    + "simple description of the exceptional event.");
            property.getMetaInfo().addComment("For example: 'Prediction feature not provided'");
            property.getMetaInfo().setTitle("error message");
            ms_message = property;
        }
        return ms_message;
    }

    public static OTDatatypeProperty details() {
        if (ms_details == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("details");
            property.getDomain().add(OTClasses.ErrorReport());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getSuperProperties().add(errorReportProperty());
            property.getMetaInfo().setDescription("Detailed message including "
                    + "technical information about the exceptional event. "
                    + "Can be used to help both peers in the debugging.");
            property.getMetaInfo().setTitle("error details");
            ms_details = property;
        }
        return ms_details;
    }

    public static OTDatatypeProperty httpStatus() {
        if (ms_httpStatus == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("httpStatus");
            property.getDomain().add(OTClasses.ErrorReport());
            property.getRange().add(XSDDatatype.XSDint);
            property.getSuperProperties().add(errorReportProperty());
            property.getMetaInfo().setTitle("HTTP status");
            ms_httpStatus = property;
        }
        return ms_httpStatus;
    }

    public static OTDatatypeProperty errorCode() {
        if (ms_errorCode == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("errorCode");
            property.getDomain().add(OTClasses.ErrorReport());
            property.getRange().add(XSDDatatype.XSDstring);
            property.getSuperProperties().add(errorReportProperty());
            property.getMetaInfo().setDescription("Error codes of error reports "
                    + "are specified by the service providers that produce the"
                    + " error report. These are characteristic for every class of exceptional events.");
            property.getMetaInfo().setTitle("error code");
            ms_errorCode = property;
        }
        return ms_errorCode;
    }

    public static OTDatatypeProperty index(){
        if(ms_index == null){
           OTDatatypeProperty property = new OTDatatypePropertyImpl("index");
            property.getDomain().add(OTClasses.MultiValuedParameter());
            property.getRange().add(XSDDatatype.XSDinteger);
            ms_index = property;
        }
        return ms_index;
    }
}
