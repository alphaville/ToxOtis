package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OTDatatypeProperty;
import org.opentox.toxotis.ontology.OTObjectProperty;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import org.opentox.toxotis.ontology.impl.SimpleOntModelImpl;

/**
 * Abstract class that includes all OpenTox components. This class is on the top
 * of the class hierarchy in this package.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OTComponent<T extends OTComponent> {

    /** URI of the component */
    protected VRI uri;
    /** Meta information (including DC and OT meta) about the component */
    protected MetaInfo meta = new MetaInfoImpl();
    protected static final String tokenid = "tokenid";

    /**
     * Constructor for an empty OpenTox Component
     */
    protected OTComponent() {
    }

    protected OTComponent(VRI uri) {
        this();
        this.uri = uri;
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
    public T setMeta(MetaInfo meta) {
        this.meta = meta;
        return (T) this;
    }

    /**
     * The OpenTox component as an individual.
     * @param model
     *      The ontological model to which the individual belongs.
     * @return
     *      The OpenTox component as an individual of a data model.
     */
    public abstract Individual asIndividual(OntModel model);

    /**
     * Creates a new Ontological Model (uses an instance of {@link SimpleOntModelImpl })
     * and assigns to it the Individual from the method
     * {@link OTComponent#asIndividual(com.hp.hpl.jena.ontology.OntModel) asIndividual(OntModel)}.
     * The ontological model will contain no other information apart from the representation
     * of the inderlying OpenTox component.
     * @return
     *      An ontological model for the current OpenTox component.
     */
    public OntModel asOntModel() {
        com.hp.hpl.jena.rdf.model.Model m;
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

    /**
     * Serializes the Dataset object into an RDF/XML document and writes it to
     * a given output stream.
     * @param output
     *      OutputStream where the output should be written.
     */
    public void writeRdf(java.io.OutputStream output) {
        javax.xml.stream.XMLOutputFactory factory = org.codehaus.stax2.XMLOutputFactory2.newInstance();
        try {
            javax.xml.stream.XMLStreamWriter writer = factory.createXMLStreamWriter(output, "UTF-8");
            writeRdf(writer);
        } catch (javax.xml.stream.XMLStreamException ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, "Unexpected Parsing Error!", ex);
        }
    }

    /**
     * Due to the large size of some objects like datasets, it is advisable to use this method
     * for serializing large objects to RDF/XML rather than the method <code>OntModel#write(OutputStream)</code>.
     * In the case of Datasets, it has been shown that this method performs much faster (about 7,5 times faster on a
     * dataset of 21 features and 1000 compounds).
     * 
     * @param writer
     *      XML Stream Writer used for the serialization of the dataset object.
     * @throws XMLStreamException
     *      In case the serialization is not possible due to syntax errors.
     */
    abstract void writeRdf(javax.xml.stream.XMLStreamWriter writer) throws javax.xml.stream.XMLStreamException;

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
        int hash = 7;
        hash = 79 * hash + (this.uri != null ? this.uri.hashCode() : 0);
        return hash;
    }
}
