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

/**
 * An interface for components that can be written to an output stream using the
 * Java streaming API for XML (StAX) using in particular some implementation of
 * <code>javax.xml.stream.XMLStreamWriter</code>.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IStAXWritable extends IOTComponent {

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