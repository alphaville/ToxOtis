package org.opentox.toxotis.core;

/**
 * An interface for components that can be written to an output stream using the
 * Java streaming API for XML (StAX) using in particular some implementation of
 * <code>javax.xml.stream.XMLStreamWriter</code>.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IStAXWritable {

    /**
     * Serializes the Dataset object into an RDF/XML document and writes it to an
     * output stream using a <code>java.io.Writer</code>.
     *
     * @param writer
     *      A Writer used to write the RDF representation to an output destination.
     */
    void writeRdf(java.io.Writer writer);

    /**
     * Serializes the Dataset object into an RDF/XML document and writes it to
     * a given output stream.
     *
     * @param output
     *      OutputStream where the output should be written.
     */
    void writeRdf(java.io.OutputStream output);

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
     * @throws UnsupportedOperationException
     *      If the method is not implemented yet.
     */
    void writeRdf(javax.xml.stream.XMLStreamWriter writer) throws javax.xml.stream.XMLStreamException;
}