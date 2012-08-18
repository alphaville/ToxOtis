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
package org.opentox.toxotis.core;

import java.util.Arrays;
import java.util.Set;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.io.Serializable;
import java.util.HashSet;
import javax.xml.stream.XMLStreamException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OTDatatypeProperty;
import org.opentox.toxotis.ontology.OTObjectProperty;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import org.opentox.toxotis.ontology.impl.SimpleOntModelImpl;

/**
 * Abstract class that includes all OpenTox components. This class is on the top
 * of the class hierarchy in this package.
 *
 * @param <T> 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OTComponent<T extends IOTComponent>
        implements IOTComponent, IStAXWritable, Serializable {

    /** URI of the component */
    protected VRI uri;
    /** Meta information (including DC and OT meta) about the component */
    protected MetaInfo meta = new MetaInfoImpl();
    private Set<OntologicalClass> ontologies = new HashSet<OntologicalClass>();
    protected static final String tokenid = "tokenid";
    /* Every component is enabled by default */
    private boolean enabled = true;
    private Set<OntologicalClass> ontologicalClassVault = new HashSet<OntologicalClass>();
    private transient org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OTComponent.class);
    private static final int hashOffset = 7, hashMod = 79;

    /**
     * Constructor for an empty OpenTox Component
     */
    protected OTComponent() {
        this.enabled = true;
    }

    protected OTComponent(VRI uri) {
        this();
        this.uri = uri;
    }

    /**
     * When a component is deleted, then it is disabled.
     * @return
     *      <code>true</code> if the component is enabled, or <code>false</code>
     *      otherwise.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public T setEnabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    /**
     * Obtain meta information about the underlying OpenTox resource/component.
     * These meta information include various fields of the Dublin Core ontology
     * (title, identifier), properties from the OpenTox ontology (see
     * http://opentox.org/data/documents/development/RDF%20Files/OpenToxOntology)
     * and some elements from other ontologies like rdfs:comment
     * @return
     *      Meta information about the resource
     */
    @Override
    public MetaInfo getMeta() {
        return meta;
    }

    /**
     * Set the meta-information of the component
     * @param meta
     *      Meta information for the component
     * @return
     *      The component itself with the updated meta-information
     * @see MetaInfo Meta Information
     */
    @Override
    public T setMeta(MetaInfo meta) {
        this.meta = meta;
        return (T) this;
    }

    /**
     * Creates a new Ontological Model (uses an instance of {@link SimpleOntModelImpl })
     * and assigns to it the Individual from the method
     * {@link OTComponent#asIndividual(com.hp.hpl.jena.ontology.OntModel) asIndividual(OntModel)}.
     * The ontological model will contain no other information apart from the representation
     * of the inderlying OpenTox component.
     * @return
     *      An ontological model for the current OpenTox component.
     */
    @Override
    public OntModel asOntModel() {
//        com.hp.hpl.jena.rdf.model.Model m;
        OntModel om = new SimpleOntModelImpl();
        asIndividual(om).inModel(om);
        return om;
    }

    /**
     * Get the URI of the resource (as an instance of {@link VRI }. Anonymous
     * resources, return <code>null</code>
     * @return
     *      URI of the component or <code>null</code> if not any.
     */
    @Override
    public VRI getUri() {
        return uri;
    }

    /**
     * Set the URI of the component which has to be an instance of VRI
     * @param uri
     *      URI of component
     * @return
     *      The component itself with the updated URI
     */
    public T setUri(VRI uri) {
        this.uri = uri;
        return (T) this;
    }

    @Override
    public void writeRdf(java.io.Writer writer) {
        javax.xml.stream.XMLOutputFactory factory = org.codehaus.stax2.XMLOutputFactory2.newInstance();
        javax.xml.stream.XMLStreamWriter streamWriter = null;
        try {
            streamWriter = factory.createXMLStreamWriter(writer);
            writeRdf(streamWriter);
        } catch (javax.xml.stream.XMLStreamException ex) {
            logger.error("Unexpected Parsing Error!", ex);
        } finally {
            if (streamWriter != null) {
                try {
                    streamWriter.close();
                } catch (XMLStreamException ex) {
                    logger.warn("StAX writer could not close!", ex);
                }
            }
        }
    }

    @Override
    public void writeRdf(java.io.OutputStream output) {
        javax.xml.stream.XMLOutputFactory factory = org.codehaus.stax2.XMLOutputFactory2.newInstance();
        javax.xml.stream.XMLStreamWriter writer = null;
        try {
            writer = factory.createXMLStreamWriter(output, "UTF-8");
            writeRdf(writer);
        } catch (javax.xml.stream.XMLStreamException ex) {
            logger.error("Unexpected Parsing Error!", ex);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (XMLStreamException ex) {
                    logger.warn("StAX writer could not close!", ex);
                }
            }
        }
    }

    /**
     * Appends declarations for all metadata properties at the current cursor
     * position of the writer.
     *
     * @param writer
     *      XML Stream Writer used for the serialization of the dataset object.
     * @throws javax.xml.stream.XMLStreamException
     *      In case the serialization is not possible due to syntax errors.
     */
    protected void writeMetaDataProperties(javax.xml.stream.XMLStreamWriter writer) throws javax.xml.stream.XMLStreamException {
        /*
         * Object Properties
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
        writeAnnotationProperty(writer, "http://purl.org/dc/elements/1.1/audience");
    }

    protected void initRdfWriter(javax.xml.stream.XMLStreamWriter writer) throws javax.xml.stream.XMLStreamException {
        writer.writeStartElement("rdf:RDF"); // #NODE_rdf:RDF_CORE_ELEMENT
        writer.writeNamespace("ot", OTClasses.NS);
        writer.writeNamespace("rdfs", RDFS.getURI());
        writer.writeNamespace("rdf", RDF.getURI());
        writer.writeNamespace("dc", DC.NS);
        writer.writeNamespace("owl", OWL.NS);
        writer.setPrefix("ot", OTClasses.NS);
        writer.setPrefix("rdfs", RDFS.getURI());
        writer.setPrefix("rdf", RDF.getURI());
        writer.setPrefix("dc", DC.NS);
        writer.setPrefix("owl", OWL.NS);
    }

    protected void endRdfWriter(javax.xml.stream.XMLStreamWriter writer) throws javax.xml.stream.XMLStreamException {
        writer.writeEndElement();// #__NODE_rdf:RDF_CORE_ELEMENT
        writer.writeEndDocument();// # .....1......
        writer.flush();
    }

    protected void writeClass(javax.xml.stream.XMLStreamWriter writer, OntologicalClass clazz) throws javax.xml.stream.XMLStreamException {
        writer.writeEmptyElement(OWL.NS, "Class");//1
        writer.writeAttribute("rdf:about", clazz.getUri());
        ontologicalClassVault.add(clazz);
        for (OntologicalClass superClass : clazz.getSuperClasses()) {
            if (!ontologicalClassVault.contains(superClass)) {
                writeClass(writer, superClass);
            }
        }
    }

    protected void writeSuperClassRelationships(javax.xml.stream.XMLStreamWriter writer)
            throws javax.xml.stream.XMLStreamException {
        for (OntologicalClass oc : ontologicalClassVault) {
            writer.writeStartElement("rdf:Description");
            writer.writeAttribute("rdf:about", oc.getUri());
            for (OntologicalClass ocSuper : oc.getSuperClasses()) {
                writer.writeEmptyElement("rdfs:subClassOf");
                writer.writeAttribute("rdf:resource", ocSuper.getUri());
            }
            writer.writeEndElement();
        }
    }

    protected void writeObjectProperty(javax.xml.stream.XMLStreamWriter writer, OTObjectProperty property) throws javax.xml.stream.XMLStreamException {
        writer.writeEmptyElement(OWL.NS, "ObjectProperty");//1
        writer.writeAttribute("rdf:about", property.getUri());
    }

    protected void writeDatatypeProperty(javax.xml.stream.XMLStreamWriter writer, OTDatatypeProperty property) throws javax.xml.stream.XMLStreamException {
        writer.writeEmptyElement(OWL.NS, "DatatypeProperty");//1
        writer.writeAttribute("rdf:about", property.getUri());
    }

    protected void writeAnnotationProperty(javax.xml.stream.XMLStreamWriter writer, String annotationPropertyUri) throws javax.xml.stream.XMLStreamException {
        writer.writeEmptyElement(OWL.NS, "AnnotationProperty");//1
        writer.writeAttribute("rdf:about", annotationPropertyUri);
    }

    @Override
    public Set<OntologicalClass> getOntologicalClasses() {
        return ontologies;
    }

    @Override
    public T setOntologicalClasses(Set ontClasses) {
        this.ontologies = ontClasses;
        return (T) this;
    }

    @Override
    public T addOntologicalClasses(OntologicalClass... ontClasses) {
        if (getOntologicalClasses() == null) {
            setOntologicalClasses(new HashSet<OntologicalClass>(ontClasses.length));
        }
        getOntologicalClasses().addAll(Arrays.asList(ontClasses));
        return (T) this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OTComponent<T> other = (OTComponent<T>) obj;
        if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = hashOffset;
        hash = hashMod * hash + (this.uri != null ? this.uri.hashCode() : 0);
        return hash;
    }
}
