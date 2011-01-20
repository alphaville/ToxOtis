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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.IOTComponent;
import org.opentox.toxotis.core.OTPublishable;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;
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

    public Feature() {
        super();
        addOntologicalClasses(OTClasses.Feature());
    }

    public Feature(VRI uri) {
        super(uri);
        addOntologicalClasses(OTClasses.Feature());
    }

    public Set<LiteralValue> getAdmissibleValues() {
        return admissibleValues;
    }

    public void setAdmissibleValues(Set<LiteralValue> admissibleValue) {
        this.admissibleValues = admissibleValue;
    }    

    public Set<OntologicalClass> getLowLevelOntologies() {
        Set<OntologicalClass> lowLevel = new HashSet<OntologicalClass>();
        for (OntologicalClass oc : getOntologicalClasses()) {
            if (oc.equals(OTClasses.NominalFeature()) || oc.equals(OTClasses.NumericFeature()) || oc.equals(OTClasses.StringFeature())) {
                lowLevel.add(oc);
            }
        }
        if (lowLevel.isEmpty()) {
            lowLevel.add(OTClasses.StringFeature());
        }
        return lowLevel;
    }

    public void setLowLevelOntologies(Set<OntologicalClass> ontologies) {
        if (getOntologicalClasses() == null) {
            setOntologicalClasses(ontologies);
        } else {
            getOntologicalClasses().addAll(ontologies);
        }
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String featureUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Resource mainType = null;
        /* Check if the feature is either Numeric or String */
        if (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty()) {
            if (getOntologicalClasses().contains(OTClasses.StringFeature())) {
                mainType = (OTClasses.StringFeature().inModel(model));
            } else if (getOntologicalClasses().contains(OTClasses.NumericFeature())) {// << Assuming cannot be StringFeature and NumericFeature at the same time
                mainType = (OTClasses.NumericFeature().inModel(model));
            }
        }
        /* If the feature is not Numeric nor String, might be Nominal... */
        if (mainType == null && (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty())) {
            if (getOntologicalClasses().contains(OTClasses.NominalFeature())) {
                mainType = (OTClasses.NominalFeature().inModel(model));
            }
        }
        Individual indiv = model.createIndividual(featureUri, mainType != null ? mainType : OTClasses.Feature().inModel(model));
        /* Check again if the feature is additionaly nominal */
        if (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty()) {
            if (getOntologicalClasses().contains(OTClasses.NominalFeature())) {
                indiv.addRDFType(OTClasses.NominalFeature().inModel(model));
            }
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
        if (uri != null) {
            builder.append("URI...\n");
            builder.append(uri);
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
            builder.append("Units : " + units);
        }

        return new String(builder);
    }

    protected Feature loadFromRemote(VRI uri, AuthenticationToken token) throws ToxOtisException {
        FeatureSpider fSpider = new FeatureSpider(uri);
        Feature f = fSpider.parse();
        setMeta(f.getMeta());
        setOntologicalClasses(f.getOntologicalClasses());
        setUnits(f.getUnits());
        return this;
    }

    @Override
    public Task publishOnline(VRI vri, AuthenticationToken token) throws ToxOtisException {
        /** Handle provided token */
        if (token != null) {
            // Replace existing token with the new one
            vri.removeUrlParameter("tokenid").addUrlParameter("tokenid", token.stringValue());
        }
        PostHttpClient client = new PostHttpClient(vri);
        client.setContentType("application/rdf+xml");
        client.setPostable(asOntModel());
        client.setMediaType("text/uri-list");
        client.post();
        int status;
        try {
            status = client.getResponseCode();
            if (status == 200) {
                Task readyTask = new Task();
                readyTask.setPercentageCompleted(100);
                readyTask.setStatus(Task.Status.COMPLETED);
                try {
                    readyTask.setResultUri(new VRI(client.getResponseText()));
                    return readyTask;
                } catch (URISyntaxException ex) {
                    throw new ToxOtisException("Unexpected behaviour from the remote server at :'" + vri.getStringNoQuery()
                            + "'. Received status code 200 and messaage:");
                }
            }
        } catch (IOException ex) {
            logger.warn(null, ex);
        }
        return null;
    }

    @Override
    public Task publishOnline(AuthenticationToken token) throws ToxOtisException {
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
    public void writeRdf(javax.xml.stream.XMLStreamWriter writer) throws javax.xml.stream.XMLStreamException {
        initRdfWriter(writer);

        writeClass(writer, OTClasses.Feature());
        writeClass(writer, OTClasses.NominalFeature());
        writeClass(writer, OTClasses.NumericFeature());
        writeClass(writer, OTClasses.StringFeature());
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
        writer.writeAttribute("rdf:about", getUri().clearToken().toString()); // REFERS TO #NODE_FEATURE_DECLARATION: Feature URI
        writer.writeEmptyElement("rdf:type"); // #NODE_FEATURE_TYPE_DECL
        featureOntologies = getOntologicalClasses();
        boolean explicitTypeDeclaration = false;
        if (featureOntologies != null && !featureOntologies.isEmpty()) {
            if (featureOntologies.contains(OTClasses.NominalFeature()) || featureOntologies.contains(OTClasses.Nominal())) {
                explicitTypeDeclaration = true;
                writer.writeAttribute("rdf:resource", OTClasses.NominalFeature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
                for (LiteralValue admissibleVal : getAdmissibleValues()) {
                    writer.writeStartElement("ot:acceptValue"); // #NODE_ACCEPT_VALUE
                    // TODO: Include also the XSD datatype of the value...
                    writer.writeCharacters(admissibleVal.getValue().toString());// REFERS TO #NODE_ACCEPT_VALUE
                    writer.writeEndElement();// #__NODE_ACCEPT_VALUE
                }
            }
            if (featureOntologies.contains(OTClasses.NumericFeature()) || featureOntologies.contains(OTClasses.Numeric())) {
                explicitTypeDeclaration = true;
                writer.writeAttribute("rdf:resource", OTClasses.NumericFeature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
            }
            if (featureOntologies.contains(OTClasses.StringFeature()) || featureOntologies.contains(OTClasses.String())) {
                explicitTypeDeclaration = true;
                writer.writeAttribute("rdf:resource", OTClasses.StringFeature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
            }
        }
        if (!explicitTypeDeclaration) { // Declare as Feature
            writer.writeAttribute("rdf:resource", OTClasses.Feature().getUri());// REFERS TO #NODE_FEATURE_TYPE_DECL
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
            writer.writeAttribute("rdf:about", sameAsFeatureUri); // REFERS TO #NODE_ADDITIONAL_FEATURE
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
        if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.getUri() != null ? this.getUri().toString().hashCode() : 0);
        return hash;
    }
}
