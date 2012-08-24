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
package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.OTPublishable;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.MethodNotAllowed;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.Unauthorized;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.ErrorReportSpider;
import org.opentox.toxotis.util.spiders.FeatureSpider;

/**
 * A Feature is an object,representing any kind of property, assigned to a
 * Compound. The feature types are determined via their links to ontologies
 * (Feature ontologies, Decsriptor ontologies, Endpoints ontologies). OpenTox
 * has established an ontology for biological/toxicological and chemical features
 * that is <a href="http://opentox.org/dev/apis/api-1.1/feature_ontology">
 * available online</a>.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Feature extends OTPublishable<Feature> {

    private String units;
    private Set<LiteralValue> admissibleValues = new HashSet<LiteralValue>();
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Feature.class);
    private static final int HASH_OFFSET = 3, HASH_MOD = 19;

    public Feature() {
        super();
        addOntologicalClasses(OTClasses.feature());
    }

    public Feature(VRI uri) {
        super(uri);
        addOntologicalClasses(OTClasses.feature());
    }

    public Set<LiteralValue> getAdmissibleValues() {
        return admissibleValues;
    }

    public void setAdmissibleValues(Set<LiteralValue> admissibleValue) {
        this.admissibleValues = admissibleValue;
    }

    /**
     * The lower-level ontological classes that are necessary to characterize the
     * features. For example, if the feature is both an instance of {@link OTClasses#Feature ot:Feature}
     * and {@link OTClasses#NumericFeature ot:NumericFeature}, then the latter suffices to describe the
     * ontological properties of the entity - therefore {@link OTClasses#Feature ot:Feature} can 
     * be omitted as it offers no meaningful information.
     * 
     * @return 
     *      Set of lower-level ontologies
     */
    public Set<OntologicalClass> getLowLevelOntologies() {
        Set<OntologicalClass> lowLevel = new HashSet<OntologicalClass>();
        for (OntologicalClass oc : getOntologicalClasses()) {
            if (oc.equals(OTClasses.nominalFeature()) || oc.equals(OTClasses.numericFeature()) || oc.equals(OTClasses.stringFeature())) {
                lowLevel.add(oc);
            }
        }
        if (lowLevel.isEmpty()) {
            lowLevel.add(OTClasses.stringFeature());
        }
        return lowLevel;
    }

    /**
     * What this method does is that it sets the ontological classes of the
     * current entity to the prescribed values if there are no other ontological
     * classes already defined. In any other case, the ontological classes provided
     * are appended to the set of existing ones.
     * 
     * @param ontologies 
     *      A set of ontological classes.
     * @see OntologicalClass
     */
    public void setLowLevelOntologies(Set<OntologicalClass> ontologies) {
        if (getOntologicalClasses() == null) {
            setOntologicalClasses(ontologies);
        } else {
            getOntologicalClasses().addAll(ontologies);
        }
    }

    /**
     * Returns the units of the feature as a string.
     * @return 
     *      Units string.
     */
    public String getUnits() {
        return units;
    }

    /**
     * Setter method for the units of the feature.
     * @param units 
     *      Units of the feature.
     */
    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String featureUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Resource mainType = null;
        /* Check if the feature is either Numeric or String */
        if (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty()) {
            if (getOntologicalClasses().contains(OTClasses.stringFeature())) {
                mainType = (OTClasses.stringFeature().inModel(model));
            } else if (getOntologicalClasses().contains(OTClasses.numericFeature())) {// << Assuming cannot be StringFeature and NumericFeature at the same time
                mainType = (OTClasses.numericFeature().inModel(model));
            }
        }
        /* If the feature is not Numeric nor String, might be Nominal... */
        if (mainType == null && (getOntologicalClasses() != null
                && !getOntologicalClasses().isEmpty())
                && getOntologicalClasses().contains(OTClasses.nominalFeature())) {
            mainType = (OTClasses.nominalFeature().inModel(model));
        }
        Individual indiv = model.createIndividual(featureUri, mainType != null ? mainType : OTClasses.feature().inModel(model));
        /* Check again if the feature is additionaly nominal */
        if (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty()
                && getOntologicalClasses().contains(OTClasses.nominalFeature())) {
            indiv.addRDFType(OTClasses.nominalFeature().inModel(model));
        }

        /* Add admissible values in the RDF graph */
        if (admissibleValues != null && !admissibleValues.isEmpty()) {
            DatatypeProperty accepts = OTDatatypeProperties.acceptValue().asDatatypeProperty(model);
            for (LiteralValue tv : admissibleValues) {
                if (tv != null) {
                    indiv.addProperty(accepts, model.createTypedLiteral(tv.getValue(), tv.getType()));
                }
            }
        }
        /* Add units */
        if (units != null) {
            DatatypeProperty unitsProp = OTDatatypeProperties.units().asDatatypeProperty(model);
            indiv.addProperty(unitsProp, model.createTypedLiteral(units, XSDDatatype.XSDstring));
        }
        /* Add meta data */
        if (meta != null) {
            meta.attachTo(indiv, model);
        }
        return indiv;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (getUri() != null) {
            builder.append("URI...\n");
            builder.append(getUri());
            builder.append("\n\n");
        }
        if (meta != null) {
            builder.append("Meta information....\n");
            builder.append(meta);
            builder.append("\n");
        }
        if (!getOntologicalClasses().isEmpty()) {
            builder.append("Ontological Classes....\n");
            Iterator<OntologicalClass> i = getOntologicalClasses().iterator();
            while (i.hasNext()) {
                builder.append(i.next().getUri());
                builder.append("\n");
            }
        }
        if (units != null && !units.isEmpty()) {
            builder.append("Units : ").append(units);
        }

        return new String(builder);
    }

    @Override
    protected Feature loadFromRemote(VRI uri, AuthenticationToken token) throws ServiceInvocationException {
        FeatureSpider fSpider = new FeatureSpider(uri);
        Feature f = fSpider.parse();
        setMeta(f.getMeta());
        setOntologicalClasses(f.getOntologicalClasses());
        setUnits(f.getUnits());
        return this;
    }

    @Override
    public Task publishOnline(VRI vri, AuthenticationToken token) throws ServiceInvocationException {
        PostHttpClient client = new PostHttpClient(vri);
        client.authorize(token);
        client.setContentType(Media.APPLICATION_RDF_XML);
        client.setPostable(asOntModel());
        client.setMediaType("text/uri-list");
        client.post();
        int status = client.getResponseCode();
        if (status == HttpStatusCodes.Success.getStatus()) {
            Task readyTask = new Task();
            readyTask.setPercentageCompleted(100);
            readyTask.setStatus(Task.Status.COMPLETED);
            try {
                readyTask.setResultUri(new VRI(client.getResponseText()));
                return readyTask;
            } catch (URISyntaxException ex) {
                String message = client.getResponseText();
                throw new ServiceInvocationException("Unexpected behaviour from the remote server at :'" + vri.getStringNoQuery()
                        + "'. Received status code 200 and message:" + message, ex);
            }
        } else {
            ErrorReport remoteServiceErrorReport = null;
            try {
                OntModel om = client.getResponseOntModel();
                if (om != null) {
                    remoteServiceErrorReport = new ErrorReportSpider(om).parse();
                }
            } catch (ServiceInvocationException ex) {
                //No Error Report - No problem!
            }

            if (status == HttpStatusCodes.MethodNotAllowed.getStatus()) {
                MethodNotAllowed methodNotAllowed = new MethodNotAllowed("Method not allowed on the URI " + getUri());
                methodNotAllowed.setErrorReport(remoteServiceErrorReport);
                throw methodNotAllowed;
            } else if (status == HttpStatusCodes.Forbidden.getStatus()) {
                ForbiddenRequest forbidden = new ForbiddenRequest();
                forbidden.setDetails("The operation you tried to perform is forbidden");
                forbidden.setErrorReport(remoteServiceErrorReport);
                throw forbidden;
            } else if (status == HttpStatusCodes.Unauthorized.getStatus()) {
                Unauthorized unauth = new Unauthorized("You are not authorized to perform this request.");
                unauth.setActor("Client");
                unauth.setErrorReport(remoteServiceErrorReport);
                throw unauth;
            } else {
                throw new ServiceInvocationException();
            }

            //TODO: Important! If status!=200 throw proper exceptions
        }

    }

    @Override
    public Task publishOnline(AuthenticationToken token) throws ServiceInvocationException {
        return publishOnline(Services.ideaconsult().augment("feature"), token);
    }

    /**
     * This method is still under development, so maybe the created RDF/XML representation
     * is not fully compliant to the OpenTox API.
     * 
     * @param writer
     *      XML Stream Writer used for the serialization of the dataset object.
     * @throws javax.xml.stream.XMLStreamException
     *     In case the serialization is not possible due to syntax errors.
     */
    @Override
    public void writeRdf(javax.xml.stream.XMLStreamWriter writer) throws javax.xml.stream.XMLStreamException {
        initRdfWriter(writer);

        writeClass(writer, OTClasses.feature());
        writeClass(writer, OTClasses.nominalFeature());
        writeClass(writer, OTClasses.numericFeature());
        writeClass(writer, OTClasses.stringFeature());
        /*
         * Append Object Properties
         */
        writeObjectProperty(writer, OTObjectProperties.hasSource());

        /*
         * Append Annotation Properties (DC and RDFS)
         */
        writeAnnotationProperty(writer, DC.contributor.getURI());
        writeAnnotationProperty(writer, DC.creator.getURI());
        writeAnnotationProperty(writer, DC.date.getURI());
        writeAnnotationProperty(writer, DC.description.getURI());
        writeAnnotationProperty(writer, DC.title.getURI());
        writeAnnotationProperty(writer, DC.subject.getURI());
        writeAnnotationProperty(writer, RDFS.comment.getURI());
        writeAnnotationProperty(writer, DC.identifier.getURI());


        /**
         * Append Datatype Properties
         */
        writeDatatypeProperty(writer, OTDatatypeProperties.acceptValue());
        writeDatatypeProperty(writer, OTDatatypeProperties.units());

        Set<OntologicalClass> featureOntologies = null;
        writer.writeStartElement("ot:Feature"); // #NODE_FEATURE_DECLARATION
        writer.writeAttribute(RDF_ABOUT, getUri().toString()); // REFERS TO #NODE_FEATURE_DECLARATION: Feature URI
        writer.writeEmptyElement(RDF_TYPE); // #NODE_FEATURE_TYPE_DECL
        featureOntologies = getOntologicalClasses();
        boolean explicitTypeDeclaration = false;
        if (featureOntologies != null && !featureOntologies.isEmpty()) {
            if (featureOntologies.contains(OTClasses.nominalFeature()) || featureOntologies.contains(OTClasses.nominal())) {
                explicitTypeDeclaration = true;
                writer.writeAttribute(RDF_RESOURCE, OTClasses.nominalFeature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
                for (LiteralValue admissibleVal : getAdmissibleValues()) {
                    writer.writeStartElement("ot:acceptValue"); // #NODE_ACCEPT_VALUE
                    // TODO: Include also the XSD datatype of the value...
                    writer.writeCharacters(admissibleVal.getValue().toString());// REFERS TO #NODE_ACCEPT_VALUE
                    writer.writeEndElement();// #__NODE_ACCEPT_VALUE
                }
            }
            if (featureOntologies.contains(OTClasses.numericFeature()) || featureOntologies.contains(OTClasses.numeric())) {
                explicitTypeDeclaration = true;
                writer.writeAttribute(RDF_RESOURCE, OTClasses.numericFeature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
            }
            if (featureOntologies.contains(OTClasses.stringFeature()) || featureOntologies.contains(OTClasses.string())) {
                explicitTypeDeclaration = true;
                writer.writeAttribute(RDF_RESOURCE, OTClasses.stringFeature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
            }
        }
        if (!explicitTypeDeclaration) { // Declare as Feature
            writer.writeAttribute(RDF_RESOURCE, OTClasses.feature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
        }
        /* Units of the feature*/
        if (getUnits() != null) {
            if (getUnits().isEmpty()) {
                writer.writeEmptyElement("ot:units");
            } else {
                writer.writeStartElement("ot:units");// #NODE_UNITS_VALUE
                writer.writeCharacters(getUnits());// REFERS TO #NODE_UNITS_VALUE
                writer.writeEndElement();// #__NODE_UNITS_VALUE
            }
        }

        /* Feature Meta Data*/
        String sameAsFeatureUri = null;
        if (getMeta() != null) {
            getMeta().writeToStAX(writer);
//            if (getMeta().getSameAs() != null && getMeta().getSameAs(). != null) {
//                sameAsFeatureUri = getMeta().getSameAs().getValue().toString();
//                if (!sameAsFeatureUri.contains("http")) {
//                    sameAsFeatureUri = OTClasses.NS + sameAsFeatureUri;
//                }
//            }
        }
        writer.writeEndElement();// #__NODE_FEATURE_DECLARATION

        /** sameAs feautre type declaration (declared alwayes as ot:Feature) */
        if (sameAsFeatureUri != null) {
            writer.writeStartElement("ot:Feature");// #NODE_ADDITIONAL_FEATURE
            writer.writeAttribute(RDF_ABOUT, sameAsFeatureUri); // REFERS TO #NODE_ADDITIONAL_FEATURE
            writer.writeEndElement();// #__NODE_ADDITIONAL_FEATURE
        }
        endRdfWriter(writer);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Feature other = (Feature) obj;
        if (this.getUri() != other.getUri() && (this.getUri() == null || !this.getUri().equals(other.getUri()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = HASH_OFFSET;
        hash = HASH_MOD * hash + (this.getUri() != null ? this.getUri().toString().hashCode() : 0);
        return hash;
    }
}
